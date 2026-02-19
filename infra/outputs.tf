output "alb_dns" {
  description = "k6 테스트 대상 URL"
  value       = "http://${aws_lb.app.dns_name}"
}

output "app_1_public_ip" {
  value = aws_instance.app[0].public_ip
}

output "app_2_public_ip" {
  value = aws_instance.app[1].public_ip
}

output "k6_public_ip" {
  value = aws_instance.k6.public_ip
}

output "rds_endpoint" {
  value = aws_db_instance.mysql.endpoint
}

output "redis_endpoint" {
  value = var.enable_elasticache ? aws_elasticache_replication_group.valkey[0].primary_endpoint_address : "N/A"
}

output "ssh_app_1" {
  value = "ssh -i ~/.ssh/${var.key_name}.pem ec2-user@${aws_instance.app[0].public_ip}"
}

output "ssh_app_2" {
  value = "ssh -i ~/.ssh/${var.key_name}.pem ec2-user@${aws_instance.app[1].public_ip}"
}

output "ssh_k6" {
  value = "ssh -i ~/.ssh/${var.key_name}.pem ec2-user@${aws_instance.k6.public_ip}"
}
