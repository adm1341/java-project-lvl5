matest:
	gradle test

start:
	gradle bootRun

setup:
	gradle wrapper --gradle-version 7.3

clean:
	./gradlew clean

build:
	./gradlew clean build
test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates
.PHONY: build