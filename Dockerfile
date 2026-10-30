# 빌드 단계: 의존성 레이어를 소스보다 먼저 받아 두어, 소스만 바뀌면 의존성 다운로드를 건너뛴다.
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon -q > /dev/null

COPY src src
# 테스트는 CI에서 MySQL과 함께 따로 돌리므로 이미지 빌드에서는 건너뛴다.
RUN ./gradlew bootJar --no-daemon -x test

# 실행 단계: JDK 없이 JRE만 둬서 이미지를 작게 한다. 비밀값은 넣지 않고 실행할 때 환경변수로 받는다.
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd --system --uid 10001 app
COPY --from=build /workspace/build/libs/*.jar app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
