up:
	docker compose --env-file .env -f ./.docker/compose/docker-compose.yml up -d
down:
	docker compose --env-file .env -f ./.docker/compose/docker-compose.yml down
