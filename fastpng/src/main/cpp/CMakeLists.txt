# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)
# Declares and names the project.

project("libpng")

include_directories(
        libpng/
)

add_library(
        fastpng

        SHARED

        libpng/pngget.c
        libpng/pngread.c
        libpng/pngrutil.c
        libpng/pngtrans.c
        libpng/pngwtran.c
        libpng/png.c
        libpng/pngmem.c
        libpng/pngrio.c
        libpng/pngset.c
        libpng/pngwio.c
        libpng/pngwutil.c
        libpng/pngerror.c
        libpng/pngpread.c
        libpng/pngrtran.c
        libpng/pngwrite.c
        native-lib.cpp
)
#set_target_properties(
#        png
#        PROPERTIES
#        ARCHIVE_OUTPUT_DIRECTORY
#        "${CMAKE_CURRENT_SOURCE_DIR}/${ANDROID_ABI}"
#)

find_library(
        log-lib
        log
)
target_link_libraries(
        fastpng
        z
        jnigraphics
        ${log-lib}
)
