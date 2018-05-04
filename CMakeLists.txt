cmake_minimum_required(VERSION 3.0.2)
project(pngquant-java)

# Setup JNI headers
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

# Setup sources & headers
file(GLOB INCLUDES "jni/*.h" "libimagequant/*.h")
file(GLOB SOURCES "jni/*.cpp" "libimagequant/*.c")
list(REMOVE_ITEM SOURCES "${CMAKE_CURRENT_LIST_DIR}/libimagequant/example.c")

# Flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")

# Define build output (shared library)
add_library(pngquant-java SHARED ${SOURCES})

# Copy output to src/main/resources
add_custom_command(TARGET pngquant-java POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:pngquant-java> "${CMAKE_CURRENT_LIST_DIR}/src/main/resources"
        )