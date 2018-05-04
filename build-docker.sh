#!/bin/bash
docker build -t pngquant-docker . && docker run -v "$(pwd):/code" -it pngquant-docker