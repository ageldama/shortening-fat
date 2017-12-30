# shortening-fat

## Requirements
1. Java 1.8+
1. Maven 3+
1. Modern Web browser : because web interfaces are implemented with HTML5 / Vue.js

## Run tests
1. `mvn test`
1. To check test coverage: open `./target/jacoco-ut/index.html` in web browser.

## Run standalone
1. `mvn spring-boot:run`
1. Open `http://localhost:8080` or `http://localhost:8080/vue-ui.html` in your web browser.

## API definitions
1. (Run `mvn spring-boot:run`)
1. Open `http://localhost:8080/swagger-ui.html` to check Swagger-UI.

## 사용한 오픈소스 라이브러리
1. Spring Boot
1. Flyway : Hibernate DDL 자동생성 대신에 직접 스키마 생성하고 마이그레이션 가능하도록 적용.
1. Spring Security : 로그인 기능을 위해 사용
1. Guava, Commons-io, Commons-lang3 : Java 기본 라이브러리로 사용
1. SpringFox Swagger2 : Swagger2 
1. Javafaker : 간단히 랜덤 아이디, 이메일 등 생성하기 위해 사용
1. JaCoCo : Test Code Coverage Report 생성위해 Maven 플러그인으로 사용
1. HSQLDB : 애플리케이션 디비로 인메모리 디비 구현으로 사용
1. JUnit 4, Mockito, Assertj : 테스트케이스 작성에 사용, spring-boot-starter-test 의존성으로 사용 및 WebMVC등을 위한 `spring-*-test` 등도 함께 사용
