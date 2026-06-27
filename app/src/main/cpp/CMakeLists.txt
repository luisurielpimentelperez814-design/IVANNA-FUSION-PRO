cmake_minimum_required(VERSION 3.22.1)
project(ivanna_fusion_dsp)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(
    ivanna_dsp
    SHARED
    ivanna_dsp_jni.cpp
    dsp/ParametricEQ.cpp
    dsp/Compressor.cpp
    dsp/HarmonicExciter.cpp
    dsp/StereoWidener.cpp
    dsp/GainStage.cpp
)

# Flags comunes — válidos para todos los ABIs
target_compile_options(ivanna_dsp PRIVATE
    -O3
    -ffast-math
    -fno-exceptions
    -Wall
    -Wno-unused-result
)

# -march=armv8-a solo tiene sentido en ARM; en x86_64 el compilador lo rechaza.
# ANDROID_ABI es la variable que el toolchain de NDK expone en cada subtarget.
if(ANDROID_ABI STREQUAL "arm64-v8a")
    target_compile_options(ivanna_dsp PRIVATE -march=armv8-a)
elseif(ANDROID_ABI STREQUAL "armeabi-v7a")
    target_compile_options(ivanna_dsp PRIVATE -march=armv7-a -mfpu=neon)
endif()
# x86_64 / x86: sin -march explícito — el compilador elige el más adecuado.

find_library(log-lib log)
find_library(android-lib android)

target_link_libraries(
    ivanna_dsp
    ${log-lib}
    ${android-lib}
)

target_include_directories(ivanna_dsp PRIVATE ${CMAKE_SOURCE_DIR}/include)
