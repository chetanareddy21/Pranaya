FROM ubuntu:latest
LABEL authors="hemu"

ENTRYPOINT ["top", "-b"]