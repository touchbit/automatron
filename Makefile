.PHONY: test
test:
	@mvn clean test

.PHONY: build
build:
	@mvn clean package

.PHONY: run
run: build
	@java -jar ./target/automatron.jar

.PHONY: d
d: build
	@docker build -t touchbit/automatron .
	@docker run -p 8080:8080 -p 9092:9092 touchbit/automatron

gen:
	@mvn clean compile -P admin-config-view-generation
	@mvn clean package
	@#java -jar target/automatron.jar

