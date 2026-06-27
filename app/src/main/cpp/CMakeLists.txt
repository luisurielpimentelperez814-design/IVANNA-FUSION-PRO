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

# FIX #9: flags de optimización centralizados aquí (no en build.gradle.kts).
# -O3 -ffast-math activos en release; el build de debug recibe -O0 -g por defecto de CMake.
target_compile_options(ivanna_dsp PRIVATE
    -O3
    -march=armv8-a
    -ffast-math
    -fno-exceptions  # no se usan excepciones C++ en el DSP — reduce tamaño
    -Wall
    -Wno-unused-result
)

find_library(log-lib log)
find_library(android-lib android)

target_link_libraries(
    ivanna_dsp
    ${log-lib}
    ${android-lib}
)

target_include_directories(ivanna_dsp PRIVATE ${CMAKE_SOURCE_DIR}/include)
