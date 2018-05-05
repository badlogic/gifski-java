#!/bin/bash
docker build -t pngquant-docker . && docker run -v "$(pwd):/code" -v "$(pwd)/jni/gifski-fork/cargo-config:/root/.cargo/config" -it pngquant-docker