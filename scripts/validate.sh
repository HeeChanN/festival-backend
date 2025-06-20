#!/bin/bash
set -euo pipefail
exec > >(tee /tmp/validate.stdout) 2> >(tee /tmp/validate.stderr >&2)

echo "[Validate] 시작"

# 1) 애플리케이션 프로세스가 뜨는지 확인
if ! pgrep -f "java.*app.jar" >/dev/null; then
  echo "앱 프로세스가 안 떠 있음"
  exit 1
fi

# 2) 헬스 체크
if ! curl -sf http://127.0.0.1:9998/; then
  echo "헬스 체크 실패"
  exit 1
fi

echo "[Validate] 성공"
exit 0