FROM ubuntu:16.04

RUN apt-get update && apt-get -y --force-yes install gcc g++ gcc-multilib g++-multilib mingw-w64 lib32z1 git cmake
WORKDIR /code
ENV IS_DOCKER=true
CMD ./build.sh --target=windows-x86 && \
    ./build.sh --target=windows-x86_64 && \
    ./build.sh --target=linux-x86 && \
	./build.sh --target=linux-x86_64
