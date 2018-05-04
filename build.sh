#!/bin/bash
set -e

BUILD=debug
CC=gcc
CXX=g++
CC_FLAGS="-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -std=c99"
CXX_FLAGS="-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -std=c++11"
LINKER_FLAGS="-shared"
LIBRARIES="-lm"
OUTPUT_DIR="src/main/resources/"
OUTPUT_PREFIX="lib"
OUTPUT_NAME="pngquant-java"
OUTPUT_SUFFIX=".so"

function usage {
  cat <<EOF
Usage: $SELF [options]
Options:
  --build=[release|debug] Specifies the build type. If not set both release
                          and debug versions of the libraries will be built.
  --target=...            Specifies the target to build for. Supported
                          targets are macosx, linux-x86_64, linux-x86,
                          windows-x86, and windows-x86_64. If not set the
                          current host OS determines the targets.
  --help                  Displays this information and exits.
EOF
  exit $1
}

while [ "${1:0:2}" = '--' ]; do
  NAME=${1%%=*}
  VALUE=${1#*=}
  case $NAME in
    '--target') TARGET="$VALUE" ;;
    '--build') BUILD="$VALUE" ;;
    '--help')
      usage 0
      ;;
    *)
      echo "Unrecognized option or syntax error in option '$1'"
      usage 1
      ;;
  esac
  shift
done

if [ "x$TARGET" = 'x' ]; then
    echo "Please specify a target: macosx, linux-x86, linux-x86_64, windows-x86, windows-x86_64"
    exit 1
fi

if [ "x$TARGET" != 'x' ]; then
    OS=${TARGET%%-*}
    ARCH=${TARGET#*-}

    if [ "$ARCH" = "x86" ]; then
        CC_FLAGS="$CC_FLAGS -m32"
        CXX_FLAGS="$CXX_FLAGS -m32"
        LINKER_FLAGS="$LINKER_FLAGS -m32"
    else
        CC_FLAGS="$CC_FLAGS -m64"
        CXX_FLAGS="$CXX_FLAGS -m64"
        LINKER_FLAGS="$LINKER_FLAGS -m64"
    fi

    if [ "$OS" = "windows" ]; then
        CC="i686-w64-mingw32-gcc"
        CXX="i686-w64-mingw32-g++"
        LINKER_FLAGS="-Wl,--kill-at -static-libgcc -static-libstdc++ $LINKER_FLAGS"
        JNI_MD="win32"
        OUTPUT_PREFIX=""
        OUTPUT_SUFFIX=".dll"
        if [ "$ARCH" = "x86_64" ]; then
            OUTPUT_NAME="$OUTPUT_NAME""64"
        fi
    fi

    if [ "$OS" = "linux" ]; then
        CC_FLAGS="$CC_FLAGS -fPIC"
        CXX_FLAGS="$CXX_FLAGS -fPIC"
        JNI_MD="linux"
    fi

    if [ "$OS" = "macosx" ]; then
        CC_FLAGS="$CC_FLAGS -fPIC"
        CXX_FLAGS="$CXX_FLAGS -fPIC"
        JNI_MD="mac"
        OUTPUT_SUFFIX=".dylib"
        OUTPUT_NAME="$OUTPUT_NAME""64"
    fi

    if [ "$BUILD" = "debug" ]; then
        CC_FLAGS="$CC_FLAGS -g"
        CXX_FLAGS="$CXX_FLAGS -g"
    else
        CC_FLAGS="$CC_FLAGS -O2"
        CXX_FLAGS="$CXX_FLAGS -O2"
    fi
fi

# mvn compile
# javah -cp target/classes -o jni/PngQuant.h com.badlogicgames.pngquant.PngQuant

C_SOURCES="`find libimagequant -name *.c`"
C_EXCLUDES="libimagequant/example.c libimagequant/org/pngquant/PngQuant.c"
CXX_SOURCES=`find jni -name *.cpp`
CXX_EXCLUDES=
HEADERS="-Ijni -Ijni/headers -Ijni/headers/${JNI_MD} -Ilibimagequant"

rm -rf tmp
mkdir -p tmp

echo ""
echo "--- Compiling for $TARGET, build type $BUILD"

echo "--- Compiling C sources"
for f in $C_SOURCES; do
    if ! echo $C_EXCLUDES | grep -w "$f"> /dev/null; then
        echo "$CC $CC_FLAGS $HEADERS -o tmp/`basename $f .c`.o"
        $CC $CC_FLAGS $HEADERS "$f" -o tmp/`basename $f .c`.o
    fi
done

echo "--- Compiling C++ sources"
echo $CXX_SOURCES;
for f in $CXX_SOURCES; do
   echo "$CXX $CXX_FLAGS $HEADERS -o tmp/`basename $f .c`.o"
   $CXX $CXX_FLAGS $HEADERS "$f" -o tmp/`basename $f .c`.o
done

echo "--- Linking"
LINKER=$CXX
OBJ_FILES=`find tmp -name *.o`
echo "$LINKER $LINKER_FLAGS $LIBRARIES -o $OUTPUT_DIR$OUTPUT_PREFIX$OUTPUT_NAME$OUTPUT_SUFFIX $OBJ_FILES"
$LINKER $LINKER_FLAGS $LIBRARIES -o "$OUTPUT_DIR$OUTPUT_PREFIX$OUTPUT_NAME$OUTPUT_SUFFIX" $OBJ_FILES

echo "--- Clean up"
rm -rf tmp