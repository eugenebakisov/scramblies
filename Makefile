IMAGE_NAME := scramblies
IMAGE_PATH := eugenebakisov
IMAGE      := $(IMAGE_PATH)/$(IMAGE_NAME)

TAG := $(or $(COMMIT_REF_NAME), `git describe --tags --always`)
TAG := $(IMAGE):$(TAG)

BUILD_TAG := $(IMAGE):build

clean:
	rm -rf target

run:
	clj -M:dev

repl:
	clj -M:dev:nrepl

clj-test:
	clj -M:test

cljs-test:
	npx shadow-cljs compile test && ./node_modules/karma/bin/karma start --single-run

test: clj-test cljs-test

lint:
	clj -M:dev:eastwood

uberjar:
	clj -T:build all

build: uberjar docker-build docker-tag

deploy: docker-login docker-push

shell:
	docker run --interactive --tty --rm --name testing $(BUILD_TAG) /bin/bash

docker-build:
	docker build --tag $(BUILD_TAG) --rm --compress $(PWD)

docker-tag:
	docker tag $(BUILD_TAG) $(TAG)

docker-login:
	@$(if $(and $(REGISTRY_USER), $(REGISTRY_PASSWORD)),\
	docker login -u $(REGISTRY_USER) -p $(REGISTRY_PASSWORD), \
	docker login)

docker-push:
	docker push $(TAG)

debug:
	@echo "IMAGE:     $(IMAGE)"
	@echo "BUILD_TAG: $(BUILD_TAG)"
	@echo "TAG:       $(TAG)"
