//
// Created by glumes on 2018/7/24.
//

#include <jni.h>


#include <logutil.h>
#include <android/bitmap.h>
#include <cstring>

jobject generateBitmap(JNIEnv *env, uint32_t width, uint32_t height);

extern "C"
JNIEXPORT jobject JNICALL Java_jp_co_cyberagent_android_gpuimage_GPUImageNativeLibrary_rotateBitmap
        (JNIEnv *env,  jclass thiz, jobject bitmap) {
    LOGD("rotate bitmap");

    AndroidBitmapInfo bitmapInfo;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return NULL;
    }

    // 读取 bitmap 的像素内容到 native 内存
    void *bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    uint32_t newWidth = bitmapInfo.height;
    uint32_t newHeight = bitmapInfo.width;

    uint32_t *newBitmapPixels = new uint32_t[newWidth * newHeight];

    int whereToGet = 0;

    // 弄明白 bitmapPixels 的排列，这里不同于二维数组了。
    for (int x = newWidth; x >= 0; --x) {
        for (int y = 0; y < newHeight; ++y) {
            uint32_t pixel = ((uint32_t *) bitmapPixels)[whereToGet++];
            newBitmapPixels[newWidth * y + x] = pixel;
        }
    }

    jobject newBitmap = generateBitmap(env, newWidth, newHeight);

    void *resultBitmapPixels;

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &resultBitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    int pixelsCount = newWidth * newHeight;

    memcpy((uint32_t *) resultBitmapPixels, newBitmapPixels, sizeof(uint32_t) * pixelsCount);

    AndroidBitmap_unlockPixels(env, newBitmap);

    delete[] newBitmapPixels;
    return newBitmap;
}

extern "C"
JNIEXPORT jobject JNICALL Java_jp_co_cyberagent_android_gpuimage_GPUImageNativeLibrary_convertBitmap
        (JNIEnv *env,  jclass thiz, jobject bitmap) {
    LOGD("convert bitmap");
    AndroidBitmapInfo bitmapInfo;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return NULL;
    }

    // 读取 bitmap 的像素内容到 native 内存
    void *bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    uint32_t newWidth = bitmapInfo.width;
    uint32_t newHeight = bitmapInfo.height;

    uint32_t *newBitmapPixels = new uint32_t[newWidth * newHeight];

    int whereToGet = 0;

    for (int y = 0; y < newHeight; ++y) {
        for (int x = 0; x < newWidth; x++) {
            uint32_t pixel = ((uint32_t *) bitmapPixels)[whereToGet++];
            newBitmapPixels[newWidth * (newHeight - 1 - y) + x] = pixel;
        }
    }


    jobject newBitmap = generateBitmap(env, newWidth, newHeight);

    void *resultBitmapPixels;

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &resultBitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    int pixelsCount = newWidth * newHeight;

    memcpy((uint32_t *) resultBitmapPixels, newBitmapPixels, sizeof(uint32_t) * pixelsCount);

    AndroidBitmap_unlockPixels(env, newBitmap);

    delete[] newBitmapPixels;

    return newBitmap;
}

extern "C"
JNIEXPORT jobject JNICALL Java_jp_co_cyberagent_android_gpuimage_GPUImageNativeLibrary_mirrorBitmap
        (JNIEnv *env,  jclass thiz, jobject bitmap) {
    LOGD("mirror bitmap");
    AndroidBitmapInfo bitmapInfo;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return NULL;
    }

    // 读取 bitmap 的像素内容到 native 内存
    void *bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    uint32_t newWidth = bitmapInfo.width;
    uint32_t newHeight = bitmapInfo.height;

    uint32_t *newBitmapPixels = new uint32_t[newWidth * newHeight];

    int whereToGet = 0;

    for (int y = 0; y < newHeight; ++y) {
        for (int x = newWidth - 1; x >= 0; x--) {
            uint32_t pixel = ((uint32_t *) bitmapPixels)[whereToGet++];
            newBitmapPixels[newWidth * y + x] = pixel;
        }
    }


    jobject newBitmap = generateBitmap(env, newWidth, newHeight);

    void *resultBitmapPixels;

    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &resultBitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    int pixelsCount = newWidth * newHeight;

    memcpy((uint32_t *) resultBitmapPixels, newBitmapPixels, sizeof(uint32_t) * pixelsCount);

    AndroidBitmap_unlockPixels(env, newBitmap);

    delete[] newBitmapPixels;

    return newBitmap;
}
JNIEXPORT jobjectArray JNICALL Java_jp_co_cyberagent_android_gpuimage_GPUImageNativeLibrary_returnBitmapArray (JNIEnv* env, jobject obj, jobject bitmap) {
    LOGE("reading bitmap info...");
    AndroidBitmapInfo info;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0){
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return NULL;
    }
    LOGE("width:%d height:%d stride:%d", info.width, info.height, info.stride);
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        LOGE("Bitmap format is not RGBA_8888!");
        return NULL;
    }

    LOGE("reading bitmap pixels...");
    unsigned char* bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, (void**)&bitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    LOGE("creating new array...");
    jclass bitmapCls = env->GetObjectClass(bitmap);
    jobjectArray infos = env->NewObjectArray(BITMAPS_SIZE, bitmapCls, NULL);
    IMAGE images[BITMAPS_SIZE];
    int s = parseWaveToArray(bitmapPixels, info.width, info.height, images);
    LOGE("s = %d", s);
    int i;
    for(i = 0; i < s; i++) {
        jobject bp = createBitmap(env,  bitmapCls, images[i]);
        env->SetObjectArrayElement(infos, i, bp);
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    return infos;
}

jobject generateBitmap(JNIEnv *env, uint32_t width, uint32_t height) {

    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmapCls,
                                                            "createBitmap",
                                                            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(
            bitmapConfigClass, "valueOf",
            "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");

    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass,
                                                       valueOfBitmapConfigFunction, configName);

    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls,
                                                    createBitmapFunction,
                                                    width,
                                                    height, bitmapConfig);

    return newBitmap;
}
