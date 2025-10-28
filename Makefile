# This Makefile manages docker compose to run & stop easily.
.PHONY: run down

run:
	docker-compose up -d --build

down:
	docker-compose down -v