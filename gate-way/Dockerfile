# Docker 환경
FROM azul/zulu-openjdk:11
# 이미지를 관리하는 사람
LABEL maintainer="ddng"
# 컨테이너가 필요한 데이터를 저장하는 곳
VOLUME /tmp
# 외부에 노출되는 포트 번호
EXPOSE 18090
# 현재 JAR 파일 변수 설정
ARG JAR_FILE=target/gate-way-0.0.1-SNAPSHOT.jar
# demo.jar의 이름으로 JAR 파일 추가
ADD ${JAR_FILE} gate-way.jar
# 컨테이너 실행 시 실행될 명령어 "java -Dspring.profiles.active=dev -jar gate-way.jar"
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "gate-way.jar"]
