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

docker:
	clj -T:build docker

docker-push:
	clj -T:build docker-push
