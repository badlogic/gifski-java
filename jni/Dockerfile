FROM debian:stretch-slim

RUN apt-get update && apt-get -y install curl gcc g++ gcc-multilib g++-multilib mingw-w64 lib32z1 git cmake && \
	curl https://sh.rustup.rs -sSf | sh -s -- -y && \
	export PATH=$PATH:$HOME/.cargo/bin && \
	rustup target add i686-pc-windows-gnu && \
	rustup target add x86_64-pc-windows-gnu && \
	rustup target add i686-unknown-linux-gnu

WORKDIR /code/jni
CMD export PATH=$PATH:$HOME/.cargo/bin && \
	./build.sh --target=windows-x86 --build=release && \
	./build.sh --target=windows-x86_64 --build=release && \
	./build.sh --target=linux-x86 --build=release && \
	./build.sh --target=linux-x86_64 --build=release