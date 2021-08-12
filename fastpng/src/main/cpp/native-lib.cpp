#include <jni.h>
#include <string>
#include "libpng/png.h"
#include <android/log.h>
#include <android/bitmap.h>
#include <sys/time.h>
#include <threads.h>
#include <unistd.h>

#define LOG_TAG    "test===="
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,__VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


//#define USE_PREMULTIPLY_APLHA (0)//使用预乘
#define USE_RGBA (1)//使用 r g b a
//#define USE_ABGR (0)//使用 a b g r

//r g b a
#define CC_RGB_PREMULTIPLY_APLHA_RGBA(vr, vg, vb, va)\
 ( (unsigned)(vr))|\
    ( (unsigned)(vg) << 8)|\
  ( (unsigned)(vb) << 16)|\
  ((unsigned)(va) << 24)


//读取png图片，并返回宽高，若出错则返回NULL
unsigned char *read_png(const char *path, int *length, int *width, int *height) {
    FILE *file = fopen(path, "rb");
    if (file == NULL)return NULL;
    png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, 0, 0, 0);
    if (png_ptr == NULL) {
        fclose(file);
        return NULL;
    }
    png_infop info_ptr = png_create_info_struct(png_ptr);
    if (info_ptr == NULL) {
        fclose(file);
        png_destroy_write_struct(&png_ptr, NULL);
        return NULL;
    }
    if (setjmp(png_jmpbuf(png_ptr))) {
        fclose(file);
        png_destroy_write_struct(&png_ptr, &info_ptr);
        return NULL;
    }
    //开始读文件
    png_init_io(png_ptr, file);
    png_read_png(png_ptr, info_ptr, PNG_TRANSFORM_EXPAND, 0);
    //获取文件的宽高色深
    int m_width = *width = png_get_image_width(png_ptr, info_ptr);
    int m_height = *height = png_get_image_height(png_ptr, info_ptr);
    //获取图像的色彩类型
    int color_type = png_get_color_type(png_ptr, info_ptr);

    int bytesPerComponent = 3, i = 0, j = 0, p = 0;
    if (color_type & PNG_COLOR_MASK_ALPHA) {
        bytesPerComponent = 4;
        p = 1;
    }
    int size = *length = m_height * m_width * bytesPerComponent;
    LOGI("size=%d", size);
    unsigned char *pImateRawData = (unsigned char *) malloc(size);
    png_bytep *rowPointers = png_get_rows(png_ptr, info_ptr);
    int bytesPerRow = m_width * bytesPerComponent;
    if (p == 1) {
        unsigned int *tmp = (unsigned int *) pImateRawData;
        for (i = 0; i < m_height; i++) {
            for (j = 0; j < bytesPerRow; j += 4) {
                *tmp++ = CC_RGB_PREMULTIPLY_APLHA_RGBA(rowPointers[i][j],
                                                       rowPointers[i][j + 1], rowPointers[i][j + 2],
                                                       rowPointers[i][j + 3]);
            }
        }
    } else {
        for (j = 0; j < m_height; ++j) {
            memcpy(pImateRawData + j * bytesPerRow, rowPointers[j],
                   bytesPerRow);
        }
    }
    return pImateRawData;
}

int writePng(const char *png_file_name, unsigned char *pixels, int width, int height,
             int bit_depth) {

    png_structp png_ptr;
    png_infop info_ptr;
    FILE *png_file = fopen(png_file_name, "wb");
    if (!png_file) {
        return -1;
    }
    png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
    if (png_ptr == NULL) {
        LOGE("ERROR:png_create_write_struct/n");
        fclose(png_file);
        return 0;
    }
    info_ptr = png_create_info_struct(png_ptr);
    if (info_ptr == NULL) {
        LOGE("ERROR:png_create_info_struct/n");
        png_destroy_write_struct(&png_ptr, NULL);
        return 0;
    }
    png_init_io(png_ptr, png_file);
    png_set_IHDR(png_ptr,
                 info_ptr,
                 width,
                 height,
                 bit_depth,//颜色深度，也就是每个颜色成分占用位数（8表示8位红8位绿8位蓝，如果有透明通道则还会有8位不透明度）
                 PNG_COLOR_TYPE_RGBA,//颜色类型，PNG_COLOR_TYPE_RGB表示24位真彩深色，PNG_COLOR_TYPE_RGBA表示32位带透明通道真彩色
                 PNG_INTERLACE_NONE,//不交错。PNG_INTERLACE_ADAM7表示这个PNG文件是交错格式。交错格式的PNG文件在网络传输的时候能以最快速度显示出图像的大致样子。
                 PNG_COMPRESSION_TYPE_DEFAULT,//压缩方式
                 PNG_FILTER_TYPE_DEFAULT);//这个不知道，总之填写PNG_FILTER_TYPE_BASE即可。


    png_colorp palette = (png_colorp) png_malloc(png_ptr,
                                                 PNG_MAX_PALETTE_LENGTH * sizeof(png_color));
    if (!palette) {
        fclose(png_file);
        png_destroy_write_struct(&png_ptr, &info_ptr);
        return false;
    }
    //调节速度
    png_set_filter(png_ptr, PNG_FILTER_TYPE_BASE, PNG_FILTER_SUB);
    png_set_compression_level(png_ptr, 2);
    //压缩等级
    png_set_compression_strategy(png_ptr, 3);
    png_set_PLTE(png_ptr, info_ptr, palette, PNG_MAX_PALETTE_LENGTH);
    png_write_info(png_ptr, info_ptr);
    png_set_packing(png_ptr);
    //这里就是图像数据了
    png_bytepp rows = (png_bytepp) png_malloc(png_ptr, height * sizeof(png_bytep));

    for (int i = 0; i < height; ++i) {
        rows[i] = (png_bytep) (pixels + (i) * width * 4);
    }
    delete[] rows;
    //获取时间
//    struct timeval tv;
//    gettimeofday(&tv, NULL);
//    long start = tv.tv_sec * 1000 + tv.tv_usec / 1000;

//    png_write_image_mutiThread(png_ptr, rows);
    png_write_image(png_ptr, rows);

    png_write_end(png_ptr, info_ptr);
    png_free(png_ptr, palette);
    palette = NULL;
    png_destroy_write_struct(&png_ptr, &info_ptr);
    fclose(png_file);

    return 0;
}


int test();


extern "C" JNIEXPORT jstring JNICALL
Java_com_mayohn_fastpng_ImgUtils_write(JNIEnv *env, jclass obj, jstring jpngPath,
                                               jobject bitmap) {
    if (NULL == jpngPath) {
        LOGE("savePath is NULL!");
        return NULL;
    }
    const char *path = env->GetStringUTFChars(jpngPath, 0);
    if (NULL == bitmap) {
        LOGE("bitmap is null!");
        return NULL;
    }
    AndroidBitmapInfo info; // create a AndroidBitmapInfo
    int result;
    // 获取图片信息
    result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_getInfo failed, result: %d", result);
        return NULL;
    }
    // 获取像素信息
    unsigned char *addrPtr;
    result = AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&addrPtr));
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_lockPixels failed, result: %d", result);
        return NULL;
    }
    // 执行图片操作的逻辑
    writePng(path, addrPtr, info.width, info.height, 8);
    // 像素信息不再使用后需要解除锁定
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_unlockPixels failed, result: %d", result);
    }
    return jpngPath;
}
extern "C" JNIEXPORT jobject JNICALL
Java_com_mayohn_fastpng_ImgUtils_read(JNIEnv *env, jclass obj, jstring jpngPath) {
    const char *path = env->GetStringUTFChars(jpngPath, 0);
    LOGI("%s", path);
    int length;
    int width;
    int height;
    unsigned char *imgData = read_png(path, &length, &width, &height);

    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(bitmapConfigClass, "valueOf",
                                                                   "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;");
    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass,
                                                       valueOfBitmapConfigFunction,
                                                       bitmapConfigClass, configName);

    // Bitmap newBitmap = Bitmap.createBitmap(int width,int height,Bitmap.Config config);
    jclass bitmap = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmap, "createBitmap",
                                                            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jobject newBitmap = env->CallStaticObjectMethod(bitmap, createBitmapFunction, width, height,
                                                    bitmapConfig);

    int ret;
    unsigned char *newBitmapPixels;

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, (void **) &newBitmapPixels)) < 0) {
        LOGD("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    for (int i = 0; i < width * height; i++) {
        newBitmapPixels[i * 4] = imgData[i * 4];
        newBitmapPixels[i * 4 + 1] = imgData[i * 4 + 1];
        newBitmapPixels[i * 4 + 2] = imgData[i * 4 + 2];
        newBitmapPixels[i * 4 + 3] = imgData[i * 4 + 3];
    }
    AndroidBitmap_unlockPixels(env, newBitmap);
    return newBitmap;
}
