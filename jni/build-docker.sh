#!/bin/bash
docker build -t pngquant-docker . && docker run --rm -v "$(pwd):/code" -it pngquant-docker