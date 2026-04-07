# =========================
# アプリ名（※ここだけ定義）
# =========================

APP := springboot-thymeleaf-react

# =========================
# docker compose
# =========================

COMPOSE := docker compose

# =========================
# バージョン（日時）
# =========================

VERSION := $(shell date +%Y%m%d-%H%M%S)
export IMAGE_VERSION := $(VERSION)

VERSION_FILE := .current_version

# =========================
# ヘルプ
# =========================

.PHONY: help
help:
	@echo "make build        : Dockerイメージをビルド（compose）"
	@echo "make up           : コンテナ起動"
	@echo "make deploy       : ビルド＋再起動（appのみ）"
	@echo "make down         : コンテナ停止"
	@echo "make logs         : ログ表示"
	@echo "make exec         : app コンテナに入る"
	@echo "make clean        : 未使用イメージ削除"

# =========================
# ビルド
# =========================

.PHONY: build
build:
	@echo "▶ イメージをビルドします: $(APP):$(IMAGE_VERSION)"
	DOCKER_BUILDKIT=1 $(COMPOSE) build app
	@echo "$(APP):$(IMAGE_VERSION)" > $(VERSION_FILE)

# =========================
# 起動 / デプロイ
# =========================

.PHONY: up
up:
	$(COMPOSE) up -d

.PHONY: down
down:
	$(COMPOSE) down

# 🔥 一番よく使う
.PHONY: deploy
deploy: build
	@echo "▶ Javaアプリのみ再デプロイ"
	$(COMPOSE) up -d app
	@echo "✅ デプロイ完了: $(APP):$(IMAGE_VERSION)"

# =========================
# ログ / デバッグ
# =========================

.PHONY: logs
logs:
	$(COMPOSE) logs -f app

.PHONY: exec
exec:
	$(COMPOSE) exec app /bin/sh

# =========================
# クリーンアップ
# =========================

.PHONY: clean
clean:
	@echo "▶ 未使用のDockerイメージを削除します"
	docker image prune -a -f
