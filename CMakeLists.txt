cmake_minimum_required(VERSION 3.0.2)
project(pngquant-java)

if(NOT OS)
    message(WARNING "OS not set, trying to determine automatically")
    IF(${CMAKE_SYSTEM_NAME} MATCHES "Darwin")
        SET(OS "macosx")
        SET(ARCH "x86_64")
    ENDIF()
    IF(${CMAKE_SYSTEM_NAME} MATCHES "Linux")
        SET(OS "linux")
        SET(ARCH "x86_64")
    ENDIF()
    IF(${CMAKE_SYSTEM_NAME} MATCHES "Windows")
        SET(OS "windows")
        SET(ARCH "x86")
    ENDIF()
endif()

if (OS STREQUAL "linux")
    SET(JNI_MD "linux")
elseif(OS STREQUAL "macosx")
    SET(JNI_MD "mac")
elseif(OS STREQUAL "windows")
    SET(JNI_MD "win32")
endif()

if (ARCH STREQUAL "x86_64")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -m64")
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -m64")
elseif (ARCH STREQUAL "x86")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -m32")
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -m32")
endif()

include_directories("jni/headers" "jni/headers/${JNI_MD}")

# Setup sources & headers
file(GLOB SOURCES "jni/*.cpp" "libimagequant/*.c")
list(REMOVE_ITEM SOURCES "${CMAKE_CURRENT_LIST_DIR}/libimagequant/example.c")

# Common Flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")

# Define build output (shared library)
add_library(pngquant-java SHARED ${SOURCES})

# Copy output to src/main/resources
add_custom_command(TARGET pngquant-java POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:pngquant-java> "${CMAKE_CURRENT_LIST_DIR}/src/main/resources"
        )

message("Building for ${OS}-${ARCH}, build type: ${CMAKE_BUILD_TYPE}, JNI_MD: ${JNI_MD}")
message("CC: ${CMAKE_C_COMPILER}, CXX: ${CMAKE_CXX_COMPILER}")
message("C_FLAGS: ${CMAKE_C_FLAGS}, CXX_FLAGS: ${CMAKE_CXX_FLAGS}")