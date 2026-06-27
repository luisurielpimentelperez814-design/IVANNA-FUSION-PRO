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

find_library(log-lib log)
find_library(android-lib android)

target_link_libraries(
    ivanna_dsp
    ${log-lib}
    ${android-lib}
)

target_include_directories(ivanna_dsp PRIVATE ${CMAKE_SOURCE_DIR}/include)

# Optimizaciones arm64 (Snapdragon SM6375 y superiores).
# -ffast-math + march=armv8-a habilita auto-vectorización NEON sin declararlo explícitamente.
# NEON es obligatorio en aarch64 — no se declara con -mfpu/-mfloat-abi (esos son ARMv7).
target_compile_options(ivanna_dsp PRIVATE
    -O3
    -march=armv8-a
    -ffast-math
    -funroll-loops
    -fvisibility=hidden
)
