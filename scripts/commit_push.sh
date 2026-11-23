#!/usr/bin/env bash
# Siteyi yeniden üretir, data/ ve docs/ değişikliklerini commit edip push eder.
set -euo pipefail
msg="${1:-Güncelleme}"
git config user.name "bulten-bot"
git config user.email "41898282+github-actions[bot]@users.noreply.github.com"
python -m bulten render
git add data docs
if git diff --cached --quiet; then
  echo "Değişiklik yok"
  exit 0
fi
git commit -q -m "$msg"
for i in 1 2 3 4 5; do
  if git pull --rebase -q origin main && git push -q origin HEAD:main; then
    exit 0
  fi
  git rebase --abort 2>/dev/null || true
  sleep $((i * 5))
done
echo "Push başarısız" >&2
exit 1
