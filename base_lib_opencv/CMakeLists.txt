cmake_minimum_required(VERSION 3.4.1)

include_directories(src/main/cpp/libyuv/include)

find_library(log-lib log)
add_subdirectory(src/main/cpp/libyuv ./build)
aux_source_directory(src/main/cpp SRC_FILE)

add_library(
        yuvutils_lib
        SHARED
        ${SRC_FILE}
)
target_link_libraries(yuvutils_lib ${log-lib} yuv)

