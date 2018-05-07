#!/bin/bash
docker build -t pngquant-docker . && docker run --rm -v "$(pwd):/code" -v "$(pwd)/cargo-config:/root/.cargo/config" -it pngquant-docker