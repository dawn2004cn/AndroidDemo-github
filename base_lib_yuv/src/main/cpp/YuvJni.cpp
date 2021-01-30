#include <jni.h>
#include <string>
#include "libyuv.h"
#include "libyuv_utils.h"
//分别用来存储1420，1420缩放，I420旋转和镜像的数据
static jbyte *Src_i420_data;
static jbyte *Src_i420_data_scale;
static jbyte *Src_i420_data_rotate;

JNIEXPORT void JNI_OnUnload(JavaVM *jvm, void *reserved) {
    //进行释放
    free(Src_i420_data);
    free(Src_i420_data_scale);
    free(Src_i420_data_rotate);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_init(JNIEnv *env, jclass type, jint width, jint height, jint dst_width,
                                  jint dst_height) {
    Src_i420_data = (jbyte *) malloc(sizeof(jbyte) * width * height * 3 / 2);
    Src_i420_data_scale = (jbyte *) malloc(sizeof(jbyte) * dst_width * dst_height * 3 / 2);
    Src_i420_data_rotate = (jbyte *) malloc(sizeof(jbyte) * dst_width * dst_height * 3 / 2);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_compressYUV(JNIEnv *env, jclass type,
                                         jbyteArray src_, jint width,
                                         jint height, jbyteArray dst_,
                                         jint dst_width, jint dst_height,
                                         jint mode, jint degree,
                                         jboolean isMirror) {
    jbyte *Src_data = env->GetByteArrayElements(src_, NULL);
    jbyte *Dst_data = env->GetByteArrayElements(dst_, NULL);

    jbyte * Src_i420_data = (jbyte *) malloc(sizeof(jbyte) * width * height * 3 / 2);
    jbyte * Src_i420_data_scale = (jbyte *) malloc(sizeof(jbyte) * dst_width * dst_height * 3 / 2);
    jbyte * Src_i420_data_rotate = (jbyte *) malloc(sizeof(jbyte) * dst_width * dst_height * 3 / 2);
    //nv21转化为i420
    libyuvNV21ToI420(reinterpret_cast<unsigned char *>(Src_data), width, height,
               reinterpret_cast<unsigned char *>(Src_i420_data));
    //进行缩放的操作
    scaleI420(reinterpret_cast<unsigned char *>(Src_i420_data), width, height,
              reinterpret_cast<unsigned char *>(Src_i420_data_scale), dst_width, dst_height, mode);
    if (isMirror) {
        //进行旋转的操作
        rotateI420(reinterpret_cast<unsigned char *>(Src_i420_data_scale), dst_width, dst_height,
                   reinterpret_cast<unsigned char *>(Src_i420_data_rotate), degree);
        //因为旋转的角度都是90和270，那后面的数据width和height是相反的
        mirrorI420(reinterpret_cast<unsigned char *>(Src_i420_data_rotate), dst_height, dst_width,
                   reinterpret_cast<unsigned char *>(Dst_data));
    } else {
        rotateI420(reinterpret_cast<unsigned char *>(Src_i420_data_scale), dst_width, dst_height,
                   reinterpret_cast<unsigned char *>(Dst_data), degree);
    }
    if (Src_i420_data != NULL){
        free(Src_i420_data);
        Src_i420_data = NULL;
    }
    if (Src_i420_data_scale != NULL){
        free(Src_i420_data_scale);
        Src_i420_data_scale = NULL;
    }
    if (Src_i420_data_rotate != NULL){
        free(Src_i420_data_rotate);
        Src_i420_data_rotate = NULL;
    }
    env->ReleaseByteArrayElements(dst_, Dst_data, 0);
    env->ReleaseByteArrayElements(src_, Src_data, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_cropYUV(JNIEnv *env, jclass type, jbyteArray src_, jint width,
                                     jint height, jbyteArray dst_, jint dst_width, jint dst_height,
                                     jint left, jint top) {
    jbyte *src_i420_data = env->GetByteArrayElements(src_, NULL);
    jbyte *dst_i420_data = env->GetByteArrayElements(dst_, NULL);

    cropI420(reinterpret_cast<unsigned char *>(src_i420_data), width, height,
             reinterpret_cast<unsigned char *>(dst_i420_data), dst_width, dst_height, left, top);

    env->ReleaseByteArrayElements(dst_, dst_i420_data, 0);
    env->ReleaseByteArrayElements(src_, src_i420_data, 0);
}
/**
 * 已测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvI420ToNV21(JNIEnv *env, jclass type, jbyteArray i420Src,
                                           jbyteArray nv21Src,
                                           jint width, jint height) {

    jbyte *src_i420_data = env->GetByteArrayElements(i420Src, NULL);
    jbyte *src_nv21_data = env->GetByteArrayElements(nv21Src, NULL);

    libyuvI420ToNV21(reinterpret_cast<unsigned char *>(src_i420_data), width, height,
               reinterpret_cast<unsigned char *>(src_nv21_data));

    env->ReleaseByteArrayElements(nv21Src, src_nv21_data, 0);
    env->ReleaseByteArrayElements(i420Src, src_i420_data, 0);
}
/**
 * 已测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvI420Scale(JNIEnv *env, jclass type, jbyteArray i420Src,
                                            jint width, jint height,
                                            jbyteArray i420Dst,
                                            jint dstW,jint dstH) {

    jbyte *src_i420_data = env->GetByteArrayElements(i420Src, NULL);
    jbyte *dst_i420_data = env->GetByteArrayElements(i420Dst, NULL);

    scaleI420(reinterpret_cast<unsigned char *>(src_i420_data), width, height,
              reinterpret_cast<unsigned char *>(dst_i420_data), dstW, dstH, 0);

    env->ReleaseByteArrayElements(i420Src, src_i420_data, 0);
    env->ReleaseByteArrayElements(i420Dst, dst_i420_data, 0);
}
/**
 * 已测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvI420Mirror(JNIEnv *env, jclass type, jbyteArray i420Src,
                                            jbyteArray i420Dst,
                                           jint width, jint height) {

    jbyte *src_i420_data = env->GetByteArrayElements(i420Src, NULL);
    jbyte *dst_i420_data = env->GetByteArrayElements(i420Dst, NULL);

    mirrorI420(reinterpret_cast<unsigned char *>(src_i420_data), width, height,
               reinterpret_cast<unsigned char *>(dst_i420_data));

    env->ReleaseByteArrayElements(i420Src, src_i420_data, 0);
    env->ReleaseByteArrayElements(i420Dst, dst_i420_data, 0);
}
/**
 * 已测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvNV21ToI420(JNIEnv *env, jclass type, jbyteArray nv21Src,
                                            jbyteArray i420Src,
                                            jint width, jint height) {


    jbyte *Src_data = env->GetByteArrayElements(nv21Src, NULL);
    jbyte *Dst_data = env->GetByteArrayElements(i420Src, NULL);
    //nv21转化为i420
    libyuvNV21ToI420(reinterpret_cast<unsigned char *>(Src_data), width, height,
               reinterpret_cast<unsigned char *>(Dst_data));

    env->ReleaseByteArrayElements(nv21Src, Src_data, 0);
    env->ReleaseByteArrayElements(i420Src, Dst_data, 0);
}

/**
 * 已测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvProcess(JNIEnv *env, jclass type, jbyteArray nv21Src,
                                            jint width, jint height,
                                             jintArray rgbDst,
                                             jint dst_width, jint dst_height) {
    jbyte *Src_data = env->GetByteArrayElements(nv21Src, NULL);
    jint *Dst_data = env->GetIntArrayElements(rgbDst, NULL);

    jbyte * src_i420_data = (jbyte *) malloc(sizeof(jbyte) * width * height * 3 / 2);
    jbyte * src_i420_data_scale = (jbyte *) malloc(sizeof(jbyte) * dst_width * dst_height * 3 / 2);
    jbyte * src_i420_data_mirror = (jbyte *) malloc(sizeof(jbyte) * dst_width * dst_height * 3 / 2);

    //nv21转化为i420
    libyuvNV21ToI420(reinterpret_cast<unsigned char *>(Src_data), width, height,
               reinterpret_cast<unsigned char *>(src_i420_data));

    //缩放
    scaleI420(reinterpret_cast<unsigned char *>(src_i420_data), width, height,
              reinterpret_cast<unsigned char *>(src_i420_data_scale), dst_width, dst_height, 0);
    //镜像
    mirrorI420(reinterpret_cast<unsigned char *>(src_i420_data_scale), dst_width, dst_height,
               reinterpret_cast<unsigned char *>(src_i420_data_mirror));

    /*libyuvI420ToNV21(reinterpret_cast<unsigned char *>(src_i420_data_mirror), dst_width, dst_height,
               reinterpret_cast<unsigned char *>(src_i420_data_scale));

    libyuvNV21ToRGBA(reinterpret_cast<unsigned char *>(src_i420_data_scale),
                     reinterpret_cast<unsigned char *>(Dst_data),
                     (dst_width), dst_height);*/

    //转换为argb
    libyuvI420ToARGB(reinterpret_cast<unsigned char *>(src_i420_data_mirror),
                     reinterpret_cast<unsigned char *>(Dst_data),
                     (dst_width), dst_height);

    if (src_i420_data != NULL){
        free(src_i420_data);
        src_i420_data = NULL;
    }
    if (src_i420_data_scale != NULL) {
        free(src_i420_data_scale);
        src_i420_data_scale = NULL;
    }

    if (src_i420_data_mirror != NULL) {
        free(src_i420_data_mirror);
        src_i420_data_mirror = NULL;
    }

    env->ReleaseByteArrayElements(nv21Src, Src_data, 0);
    env->ReleaseIntArrayElements(rgbDst, Dst_data, 0);
}
/**
 * 未测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvNV21ToRGBA(JNIEnv *env, jclass type, jbyteArray nv21Src,
                                            jintArray rgbOut,
                                            jint width, jint height) {
    jbyte *Src_data = env->GetByteArrayElements(nv21Src, NULL);
    jint *Dst_data = env->GetIntArrayElements(rgbOut, NULL);

    libyuvNV21ToRGBA(reinterpret_cast<unsigned char *>(Src_data),
                     reinterpret_cast<unsigned char *>(Dst_data), width, height);

    env->ReleaseByteArrayElements(nv21Src, Src_data, 0);
    env->ReleaseIntArrayElements(rgbOut, Dst_data, 0);
}

/**
 * 未测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvNV12ToRGBA(JNIEnv *env, jclass type, jbyteArray nv21Src,
                                            jintArray rgbOut,
                                            jint width, jint height) {
    jbyte *Src_data = env->GetByteArrayElements(nv21Src, NULL);
    jint *Dst_data = env->GetIntArrayElements(rgbOut, NULL);

    libyuvNV12ToRGBA(reinterpret_cast<unsigned char *>(Src_data),
                     reinterpret_cast<unsigned char *>(Dst_data), width, height);

    env->ReleaseByteArrayElements(nv21Src, Src_data, 0);
    env->ReleaseIntArrayElements(rgbOut, Dst_data, 0);
}
/**
 * 未测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvYV12ToRGBA(JNIEnv *env, jclass type, jbyteArray nv21Src,
                                            jintArray rgbOut,
                                            jint width, jint height) {
    jbyte *Src_data = env->GetByteArrayElements(nv21Src, NULL);
    jint *Dst_data = env->GetIntArrayElements(rgbOut, NULL);

    libyuvYV12ToRGBA(reinterpret_cast<unsigned char *>(Src_data),
                     reinterpret_cast<unsigned char *>(Dst_data), width, height);

    env->ReleaseByteArrayElements(nv21Src, Src_data, 0);
    env->ReleaseIntArrayElements(rgbOut, Dst_data, 0);
}
/**
 * 已测试*/
extern "C"
JNIEXPORT void JNICALL
Java_com_libyuv_util_YuvUtils_yuvI420ToRGBA(JNIEnv *env, jclass type, jbyteArray nv21Src,
                                            jintArray rgbOut,
                                            jint width, jint height) {
    jbyte *Src_data = env->GetByteArrayElements(nv21Src, NULL);
    jint *Dst_data = env->GetIntArrayElements(rgbOut, NULL);

    libyuvI420ToARGB(reinterpret_cast<unsigned char *>(Src_data),
                     reinterpret_cast<unsigned char *>(Dst_data), width, height);

    env->ReleaseByteArrayElements(nv21Src, Src_data, 0);
    env->ReleaseIntArrayElements(rgbOut, Dst_data, 0);
}

/**
 * 未测试*/
extern "C"
JNIEXPORT jobject JNICALL
Java_com_libyuv_util_YuvUtils_normalize(JNIEnv *env, jclass type, jintArray rgbSrc,
                                            jint width, jint height,
                                            jfloat mean, jfloat std,
                                           jobject buffer) {
    jint *Src_data = env->GetIntArrayElements(rgbSrc, NULL);
    jclass cls = env->GetObjectClass(buffer);
    jclass clsByteBuffer= env->FindClass( "java/nio/ByteBuffer");
//    jmethodID jwrap = env->GetStaticMethodID(clsByteBuffer,"wrap", "([B)Ljava/nio/ByteBuffer;");
    jmethodID putFloat = env->GetMethodID(clsByteBuffer,"putFloat", "(F)Ljava/nio/ByteBuffer;");
    jmethodID put = env->GetMethodID(clsByteBuffer,"put", "(B)Ljava/nio/ByteBuffer;");
    jmethodID rewind = env->GetMethodID(clsByteBuffer,"rewind", "()Ljava/nio/Buffer;");
    /*jmethodID allocateDirect = env->GetStaticMethodID(clsByteBuffer,"allocateDirect",
                                                      "(I)Ljava/nio/ByteBuffer;");*/
    jbyte* pData    = (jbyte*) env->GetDirectBufferAddress(buffer); //获取buffer数据首地址
    jlong dwCapacity  = env->GetDirectBufferCapacity(buffer);         //获取buffer的容量
    if(!pData)
    {
        return NULL;
    }
    env->CallObjectMethod(buffer,rewind);
   // jobject result = env->CallStaticObjectMethod(clsByteBuffer,allocateDirect,(jint)dwCapacity);
  //  jobject result = env->AllocObject(clsByteBuffer);
//    jbyteArray data = env->NewByteArray(dwCapacity);                  //创建与buffer容量一样的byte[]
 //   env->SetByteArrayRegion(data, 0, dwCapacity, pData);              //数据拷贝到data中
    int pixel = 0;
    int offet = 0;
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            int value = Src_data[pixel++];
            env->CallObjectMethod(buffer,putFloat,(((float) (value >> 16 & 255) - mean) / std));
            env->CallObjectMethod(buffer,putFloat,(((float) (value >> 8 & 255) - mean) / std));
            env->CallObjectMethod(buffer,putFloat,(((float) (value & 255) - mean) / std));
        }
    }
    env->CallObjectMethod(buffer,rewind);
    env->ReleaseIntArrayElements(rgbSrc, Src_data, 0);
    return buffer;
}

int getBufferedColor(int pSourceColor) {
    return
            ((pSourceColor >> 24) << 24) |          // Alpha
            ((pSourceColor >> 16) & 0xFF) |         // Red  -> Blue
            ((pSourceColor >> 8) & 0xFF) << 8 |     // Green
            ((pSourceColor) & 0xFF) << 16;          // Blue -> Red
}



/*

我在使用中发现一些接口的功能，与预期有不一致的情况，如下：

## rgb 转 rgb
# RGBAToARGB
        预期是 rgba -> argb
        实际是 rgba -> gbar
        产生的效果是后三个通道前移一列，第一个通道移到第四列，实际与预期不一致

# ARGBToRGBA
        预期是 argb -> rgba
        实际是 argb -> barg
        产生的效果是前三个通道后移一列，第四个通道移到第一列，实际与预期不一致

# ARGBToABGR
        预期是 argb -> abgr
        实际是 argb -> grab
        产生的效果是第一个通道与第三个通道交换位置，实际与预期不一致

# ABGRToARGB
        预期是 abgr -> argb
        实际是 abgr -> gbar
        产生的效果是第一个通道与第三个通道交换位置，实际与预期不一致

# ARGBToBGRA
        预期是 argb -> bgra
        实际是 argb -> bgra
        四个通道顺序颠倒，实际与预期一致

# BGRAToARGB
        预期是 bgra -> argb
        实际是 bgra -> argb
        四个通道顺序颠倒，实际与预期一致

## rgb 转 yuv
# ARGBToI420
        实际与预期相反，需要 BGRAToI420 才能正确转换

# ABGRToI420
        实际与预期相反，需要 RGBAToI420 才能正确转换

# BGRAToI420
        实际与预期相反，需要 ARGBToI420 才能正确转换

# RGBAToI420
        实际与预期相反，需要 ABGRToI420 才能正确转换

# yuv 转 rgb
# I420ToARGB
        实际与预期相反，需要 I420ToBGRA 才能正确转换

# I420ToABGR
        实际与预期相反，需要 I420ToRGBA 才能正确转换

# I420ToBGRA
        实际与预期相反，需要 I420ToARGB 才能正确转换

# I420ToRGBA
        实际与预期相反，需要 I420ToABGR 才能正确转换*/
