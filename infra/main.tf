terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.region
}

# ──────────────────────────────────────────────
# 내 IP 자동 감지 (SSH 접근 제한용)
# ──────────────────────────────────────────────
data "http" "my_ip" {
  url = "https://checkip.amazonaws.com"
}

locals {
  my_ip = "${chomp(data.http.my_ip.response_body)}/32"
}

# ──────────────────────────────────────────────
# AMI (Amazon Linux 2023 - ARM64 for Graviton)
# ──────────────────────────────────────────────
data "aws_ami" "al2023_arm" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["al2023-ami-*-arm64"]
  }
  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

data "aws_availability_zones" "available" {
  state = "available"
}

# ──────────────────────────────────────────────
# VPC
# ──────────────────────────────────────────────
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  tags = { Name = "festimap-load-test" }
}

resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
}

resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = data.aws_availability_zones.available.names[0]
  map_public_ip_on_launch = true
  tags = { Name = "public-a" }
}

resource "aws_subnet" "public_b" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = data.aws_availability_zones.available.names[1]
  map_public_ip_on_launch = true
  tags = { Name = "public-b" }
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
}

resource "aws_route_table_association" "a" {
  subnet_id      = aws_subnet.public_a.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table_association" "b" {
  subnet_id      = aws_subnet.public_b.id
  route_table_id = aws_route_table.public.id
}

# ──────────────────────────────────────────────
# Security Groups
# ──────────────────────────────────────────────
resource "aws_security_group" "alb" {
  name   = "alb-sg"
  vpc_id = aws_vpc.main.id

  ingress {
    description = "HTTP from anywhere (load test)"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "app" {
  name   = "app-sg"
  vpc_id = aws_vpc.main.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [local.my_ip]
  }
  ingress {
    description     = "Spring Boot from ALB"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "k6" {
  name   = "k6-sg"
  vpc_id = aws_vpc.main.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [local.my_ip]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "rds" {
  name   = "rds-sg"
  vpc_id = aws_vpc.main.id

  ingress {
    description     = "MySQL from App"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.app.id]
  }
  ingress {
    description = "MySQL from my IP (seed data)"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = [local.my_ip]
  }
}

resource "aws_security_group" "redis" {
  count  = var.enable_elasticache ? 1 : 0
  name   = "redis-sg"
  vpc_id = aws_vpc.main.id

  ingress {
    description     = "Redis from App"
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = [aws_security_group.app.id]
  }
}

# ──────────────────────────────────────────────
# ALB (Application Load Balancer)
# ──────────────────────────────────────────────
resource "aws_lb" "app" {
  name               = "festimap-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = [aws_subnet.public_a.id, aws_subnet.public_b.id]

  tags = { Name = "festimap-alb" }
}

resource "aws_lb_target_group" "app" {
  name     = "festimap-tg"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = aws_vpc.main.id

  health_check {
    path                = "/home/1"
    port                = "8080"
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 10
  }
}

resource "aws_lb_listener" "app" {
  load_balancer_arn = aws_lb.app.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app.arn
  }
}

resource "aws_lb_target_group_attachment" "app" {
  count            = 2
  target_group_arn = aws_lb_target_group.app.arn
  target_id        = aws_instance.app[count.index].id
  port             = 8080
}

# ──────────────────────────────────────────────
# RDS MySQL
# ──────────────────────────────────────────────
resource "aws_db_subnet_group" "main" {
  name       = "festimap-db"
  subnet_ids = [aws_subnet.public_a.id, aws_subnet.public_b.id]
}

resource "aws_db_instance" "mysql" {
  identifier             = "festimap-test"
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  db_name                = "eventerdb"
  username               = "admin"
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  publicly_accessible    = true
  skip_final_snapshot    = true
  multi_az               = false

  tags = { Name = "festimap-test" }
}

# ──────────────────────────────────────────────
# ElastiCache Valkey (조건부)
# ──────────────────────────────────────────────
resource "aws_elasticache_subnet_group" "main" {
  count      = var.enable_elasticache ? 1 : 0
  name       = "festimap-cache"
  subnet_ids = [aws_subnet.public_a.id, aws_subnet.public_b.id]
}

resource "aws_elasticache_replication_group" "valkey" {
  count                = var.enable_elasticache ? 1 : 0
  replication_group_id = "festimap-cache"
  description          = "Festimap Valkey cache"
  engine               = "valkey"
  engine_version       = "8.0"
  node_type            = "cache.t3.micro"
  num_cache_clusters   = 1
  port                 = 6379
  subnet_group_name    = aws_elasticache_subnet_group.main[0].name
  security_group_ids   = [aws_security_group.redis[0].id]

  tags = { Name = "festimap-cache" }
}

# ──────────────────────────────────────────────
# EC2 - App Server x2 (각 AZ에 1대씩)
# ──────────────────────────────────────────────
resource "aws_instance" "app" {
  count                  = 2
  ami                    = data.aws_ami.al2023_arm.id
  instance_type          = var.app_instance_type
  key_name               = var.key_name
  subnet_id              = count.index == 0 ? aws_subnet.public_a.id : aws_subnet.public_b.id
  vpc_security_group_ids = [aws_security_group.app.id]

  user_data = <<-EOF
    #!/bin/bash
    dnf install -y java-17-amazon-corretto-headless mysql
  EOF

  tags = { Name = "app-server-${count.index + 1}" }
}

# ──────────────────────────────────────────────
# EC2 - k6 Runner
# ──────────────────────────────────────────────
resource "aws_instance" "k6" {
  ami                    = data.aws_ami.al2023_arm.id
  instance_type          = var.k6_instance_type
  key_name               = var.key_name
  subnet_id              = aws_subnet.public_a.id
  vpc_security_group_ids = [aws_security_group.k6.id]

  user_data = <<-EOF
    #!/bin/bash
    dnf install -y nodejs
    curl -sL https://github.com/grafana/k6/releases/download/v0.55.0/k6-v0.55.0-linux-arm64.tar.gz | tar xz
    mv k6-v0.55.0-linux-arm64/k6 /usr/local/bin/
    chmod +x /usr/local/bin/k6
  EOF

  tags = { Name = "k6-runner" }
}
