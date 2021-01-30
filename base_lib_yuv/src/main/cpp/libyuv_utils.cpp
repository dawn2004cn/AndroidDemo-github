//
// Created by daisg on 2021/1/23.
//
#include <stdint.h>
#include <libyuv.h>
#include "libyuv_utils.h"

void libyuvNV21ToI420(unsigned char *src_nv21_data, int width, int height, unsigned char *src_i420_data) {
    int src_y_size = width * height;
    int src_u_size = (width >> 1) * (height >> 1);

    unsigned char *src_nv21_y_data = src_nv21_data;
    unsigned char *src_nv21_vu_data = src_nv21_data + src_y_size;

    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_y_size + src_u_size;


    libyuv::NV21ToI420((const uint8 *) src_nv21_y_data, width,
                       (const uint8 *) src_nv21_vu_data, width,
                       (uint8 *) src_i420_y_data, width,
                       (uint8 *) src_i420_u_data, width >> 1,
                       (uint8 *) src_i420_v_data, width >> 1,
                       width, height);
}

void libyuvI420ToNV21(unsigned char *src_i420_data, int width, int height, unsigned char *src_nv21_data) {
    int src_y_size = width * height;
    int src_u_size = (width >> 1) * (height >> 1);

    unsigned char *src_nv21_y_data = src_nv21_data;
    unsigned char *src_nv21_vu_data = src_nv21_data + src_y_size;

    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_y_size + src_u_size;


    libyuv::I420ToNV21((uint8 *) src_i420_y_data, width,
                       (uint8 *) src_i420_u_data, width >> 1,
                       (uint8 *) src_i420_v_data, width >> 1,
                       reinterpret_cast<uint8 *>(src_nv21_y_data), width,
                       reinterpret_cast<uint8 *>(src_nv21_vu_data), width,
                       width, height);
}
void libyuvNV12ToI420(unsigned char *src_nv12_data, int width, int height, unsigned char *src_i420_data) {
    int src_y_size = width * height;
    int src_u_size = (width >> 1) * (height >> 1);

    unsigned char *src_nv12_y_data = src_nv12_data;
    unsigned char *src_nv12_uv_data = src_nv12_data + src_y_size;

    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_y_size + src_u_size;


    libyuv::NV12ToI420((const uint8 *) src_nv12_y_data, width,
                       (const uint8 *) src_nv12_uv_data, width,
                       (uint8 *) src_i420_y_data, width,
                       (uint8 *) src_i420_u_data, width >> 1,
                       (uint8 *) src_i420_v_data, width >> 1,
                       width, height);
}
void libyuvI420ToNV12(unsigned char *src_i420_data, int width, int height, unsigned char *src_nv12_data) {
    int src_y_size = width * height;
    int src_u_size = (width >> 1) * (height >> 1);

    unsigned char *src_nv12_y_data = src_nv12_data;
    unsigned char *src_nv12_uv_data = src_nv12_data + src_y_size;

    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_y_size + src_u_size;


    libyuv::I420ToNV12((uint8 *) src_i420_y_data, width,
                       (uint8 *) src_i420_u_data, width >> 1,
                       (uint8 *) src_i420_v_data, width >> 1,
                       reinterpret_cast<uint8 *>(src_nv12_y_data), width,
                       reinterpret_cast<uint8 *>(src_nv12_uv_data), width,
                       width, height);
}

//I420 to rgb
/*# I420ToRGBA
实际与预期相反，需要 I420ToABGR 才能正确转换*/
void libyuvI420ToRGBA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height;
    unsigned char *pV = src + width * height * 5 / 4;
    libyuv::I420ToABGR(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}
/*# I420ToARGB
实际与预期相反，需要 I420ToBGRA 才能正确转换*/
void libyuvI420ToBGRA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height;
    unsigned char *pV = src + width * height * 5 / 4;
    libyuv::I420ToBGRA(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}

/*# I420ToABGR
实际与预期相反，需要 I420ToRGBA 才能正确转换*/
void libyuvI420ToABGR(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height;
    unsigned char *pV = src + width * height * 5 / 4;
    libyuv::I420ToRGBA(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}

/*# I420ToBGRA    ok 实际是 ARGB
实际与预期相反，需要 I420ToARGB 才能正确转换*/
void libyuvI420ToARGB(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height;
    unsigned char *pV = src + width * height * 5 / 4;
    libyuv::I420ToARGB(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}

//yv12  Y  V  U to rgb
void libyuvYV12ToRGBA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height * 5 / 4;
    unsigned char *pV = src + width * height;
    libyuv::I420ToRGBA(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}

void libyuvYV12ToBGRA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height * 5 / 4;
    unsigned char *pV = src + width * height;
    libyuv::I420ToBGRA(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}
void libyuvYV12ToABGR(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height * 5 / 4;
    unsigned char *pV = src + width * height;
    libyuv::I420ToABGR(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}
void libyuvYV12ToARGB(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pU = src + width * height * 5 / 4;
    unsigned char *pV = src + width * height;
    libyuv::I420ToARGB(pY, width, pU, width >> 1, pV, width >> 1, dst, width * 4, width, height);
}

//nv12 to rgb
void libyuvNV12ToRGBA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pUV = src + width * height;
    libyuv::NV12ToARGB(pY, width, pUV, width, dst, width * 4, width, height);
}

void libyuvNV12ToBGRA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char * i420 = (unsigned char *)malloc(width*height*3/2);
    if (i420 != NULL){
        libyuvNV12ToI420(src,width,height,i420);
        libyuvI420ToRGBA(i420,dst,width,height);
        free(i420);
        i420 = NULL;
    }

}
void libyuvNV12ToABGR(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char * i420 = (unsigned char *)malloc(width*height*3/2);
    if (i420 != NULL){
        libyuvNV12ToI420(src,width,height,i420);
        libyuvI420ToABGR(i420,dst,width,height);
        free(i420);
        i420 = NULL;
    }
}
void libyuvNV12ToARGB(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char * i420 = (unsigned char *)malloc(width*height*3/2);
    if (i420 != NULL){
        libyuvNV12ToI420(src,width,height,i420);
        libyuvI420ToARGB(i420,dst,width,height);
        free(i420);
        i420 = NULL;
    }
}

//nv21 to rgb
void libyuvNV21ToRGBA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char *pY = src;
    unsigned char *pUV = src + width * height;
    libyuv::NV21ToARGB(pY, width, pUV, width, dst, width * 4, width, height);
}
void libyuvNV21ToBGRA(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char * i420 = (unsigned char *)malloc(width*height*3/2);
    if (i420 != NULL){
        libyuvNV21ToI420(src,width,height,i420);
        libyuvI420ToBGRA(i420,dst,width,height);
        free(i420);
        i420 = NULL;
    }
}
void libyuvNV21ToABGR(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char * i420 = (unsigned char *)malloc(width*height*3/2);
    if (i420 != NULL){
        libyuvNV21ToI420(src,width,height,i420);
        libyuvI420ToABGR(i420,dst,width,height);
        free(i420);
        i420 = NULL;
    }
}
void libyuvNV21ToARGB(unsigned char *src, unsigned char *dst, int width, int height) {
    unsigned char * i420 = (unsigned char *)malloc(width*height*3/2);
    if (i420 != NULL){
        libyuvNV21ToI420(src,width,height,i420);
        libyuvI420ToARGB(i420,dst,width,height);
        free(i420);
        i420 = NULL;
    }
}
//rgb to yuv
void libyuvARGBToI420(unsigned char*src,  unsigned char* dst, int width,  int height) {
    unsigned char *pY = dst;
    unsigned char *pU = dst + width * height;
    unsigned char *pV = dst + width * height * 5 / 4;
    libyuv::ARGBToI420( src, width * 4,pY, width, pU, width >> 1, pV, width >> 1, width, height);
}

void libyuvABGRToI420(unsigned char*src,  unsigned char* dst, int width,  int height) {
    unsigned char *pY = dst;
    unsigned char *pU = dst + width * height;
    unsigned char *pV = dst + width * height * 5 / 4;
    libyuv::ABGRToI420( src, width * 4,pY, width, pU, width >> 1, pV, width >> 1, width, height);
}
void libyuvBGRAToI420(unsigned char*src,  unsigned char* dst, int width,  int height) {
    unsigned char *pY = dst;
    unsigned char *pU = dst + width * height;
    unsigned char *pV = dst + width * height * 5 / 4;
    libyuv::BGRAToI420( src, width * 4,pY, width, pU, width >> 1, pV, width >> 1, width, height);
}

void libyuvRGBAToI420(unsigned char*src,  unsigned char* dst, int width,  int height){
    unsigned char *pY = dst;
    unsigned char *pU = dst + width * height;
    unsigned char *pV = dst + width * height * 5 / 4;
    libyuv::RGBAToI420( src, width * 4,pY, width, pU, width >> 1, pV, width >> 1, width, height);
}

//rgb to rgb
void libyuvRGBAToARGB(unsigned char*src,  unsigned char* dst, int width,  int height) {
    libyuv::RGBAToARGB(src,width,dst,width,width,height);
}
void libyuvARGBToRGBA(unsigned char*src,  unsigned char* dst, int width,  int height) {
    libyuv::ARGBToRGBA(src,width,dst,width,width,height);
}
void libyuvARGBToABGR(unsigned char*src,  unsigned char* dst, int width,  int height) {
    libyuv::ARGBToABGR(src,width,dst,width,width,height);
}
void libyuvABGRToARGB(unsigned char*src,  unsigned char* dst, int width,  int height) {
    libyuv::ABGRToARGB(src,width,dst,width,width,height);
}
void libyuvARGBToBGRA(unsigned char*src,  unsigned char* dst, int width,  int height) {
    libyuv::ARGBToBGRA(src,width,dst,width,width,height);
}
void libyuvBGRAToARGB(unsigned char*src,  unsigned char* dst, int width,  int height) {
    libyuv::BGRAToARGB(src,width,dst,width,width,height);
}

void scaleI420(unsigned char *src_i420_data, int width, int height, unsigned char *dst_i420_data, int dst_width,
               int dst_height, int mode) {

    int src_i420_y_size = width * height;
    int src_i420_u_size = (width >> 1) * (height >> 1);
    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_i420_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_i420_y_size + src_i420_u_size;

    int dst_i420_y_size = dst_width * dst_height;
    int dst_i420_u_size = (dst_width >> 1) * (dst_height >> 1);
    unsigned char *dst_i420_y_data = dst_i420_data;
    unsigned char *dst_i420_u_data = dst_i420_data + dst_i420_y_size;
    unsigned char *dst_i420_v_data = dst_i420_data + dst_i420_y_size + dst_i420_u_size;

    libyuv::I420Scale((const uint8 *) src_i420_y_data, width,
                      (const uint8 *) src_i420_u_data, width >> 1,
                      (const uint8 *) src_i420_v_data, width >> 1,
                      width, height,
                      (uint8 *) dst_i420_y_data, dst_width,
                      (uint8 *) dst_i420_u_data, dst_width >> 1,
                      (uint8 *) dst_i420_v_data, dst_width >> 1,
                      dst_width, dst_height,
                      (libyuv::FilterMode) mode);
}

void rotateI420(unsigned char *src_i420_data, int width, int height, unsigned char *dst_i420_data, int degree) {
    int src_i420_y_size = width * height;
    int src_i420_u_size = (width >> 1) * (height >> 1);

    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_i420_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_i420_y_size + src_i420_u_size;

    unsigned char *dst_i420_y_data = dst_i420_data;
    unsigned char *dst_i420_u_data = dst_i420_data + src_i420_y_size;
    unsigned char *dst_i420_v_data = dst_i420_data + src_i420_y_size + src_i420_u_size;

    //要注意这里的width和height在旋转之后是相反的
    if (degree == libyuv::kRotate90 || degree == libyuv::kRotate270) {
        libyuv::I420Rotate((const uint8 *) src_i420_y_data, width,
                           (const uint8 *) src_i420_u_data, width >> 1,
                           (const uint8 *) src_i420_v_data, width >> 1,
                           (uint8 *) dst_i420_y_data, height,
                           (uint8 *) dst_i420_u_data, height >> 1,
                           (uint8 *) dst_i420_v_data, height >> 1,
                           width, height,
                           (libyuv::RotationMode) degree);
    }
}

void mirrorI420(unsigned char *src_i420_data, int width, int height, unsigned char *dst_i420_data) {
    int src_i420_y_size = width * height;
    int src_i420_u_size = (width >> 1) * (height >> 1);

    unsigned char *src_i420_y_data = src_i420_data;
    unsigned char *src_i420_u_data = src_i420_data + src_i420_y_size;
    unsigned char *src_i420_v_data = src_i420_data + src_i420_y_size + src_i420_u_size;

    unsigned char *dst_i420_y_data = dst_i420_data;
    unsigned char *dst_i420_u_data = dst_i420_data + src_i420_y_size;
    unsigned char *dst_i420_v_data = dst_i420_data + src_i420_y_size + src_i420_u_size;

    libyuv::I420Mirror((const uint8 *) src_i420_y_data, width,
                       (const uint8 *) src_i420_u_data, width >> 1,
                       (const uint8 *) src_i420_v_data, width >> 1,
                       (uint8 *) dst_i420_y_data, width,
                       (uint8 *) dst_i420_u_data, width >> 1,
                       (uint8 *) dst_i420_v_data, width >> 1,
                       width, height);
}
void cropI420(unsigned char *src_i420_data, int width, int height,
              unsigned char *dst_i420_data,int dst_width,int dst_height,
              int left,int top){
    //裁剪的区域大小不对
    if (left + dst_width > width || top + dst_height > height) {
        return;
    }

    //left和top必须为偶数，否则显示会有问题
    if (left % 2 != 0 || top % 2 != 0) {
        return;
    }

    int src_length = width*height*3/2;
    int dst_i420_y_size = dst_width * dst_height;
    int dst_i420_u_size = (dst_width >> 1) * (dst_height >> 1);

    unsigned char *dst_i420_y_data = dst_i420_data;
    unsigned char *dst_i420_u_data = dst_i420_data + dst_i420_y_size;
    unsigned char *dst_i420_v_data = dst_i420_data + dst_i420_y_size + dst_i420_u_size;

    libyuv::ConvertToI420((const uint8 *) src_i420_data, src_length,
                          (uint8 *) dst_i420_y_data, dst_width,
                          (uint8 *) dst_i420_u_data, dst_width >> 1,
                          (uint8 *) dst_i420_v_data, dst_width >> 1,
                          left, top,
                          width, height,
                          dst_width, dst_height,
                          libyuv::kRotate0, libyuv::FOURCC_I420);
}
void scaleARGB(unsigned char *src, int width, int height, unsigned char *dst, int dst_width,
               int dst_height, int mode) {
    if (mode == 0){
        libyuv::ARGBScale(src,width,width,height,dst,dst_width,dst_width,dst_height,libyuv::kFilterNone);
    } else if (mode == 1){
        libyuv::ARGBScale(src,width,width,height,dst,dst_width,dst_width,dst_height,libyuv::kFilterLinear);
    }else if (mode == 2){
        libyuv::ARGBScale(src,width,width,height,dst,dst_width,dst_width,dst_height,libyuv::kFilterBilinear);
    }else if (mode == 3){
        libyuv::ARGBScale(src,width,width,height,dst,dst_width,dst_width,dst_height,libyuv::kFilterBox);
    } else  {
        return;
    }
}

void rotateARGB(unsigned char *src, int width, int height, unsigned char *dst, int degree) {
    libyuvRotateRGBA(src,dst,width,height,degree);
}
void mirrorARGB(unsigned char *src, int width, int height, unsigned char *dst, int mode) {
    libyuv::ARGBMirror(src,width*4,dst,width*4,width,height);
}

void libyuvRotateRGB(unsigned char *src, unsigned char *dst, int width, int height, float degree) {
    if (degree == 90.0f) {
        libyuv::ARGBRotate(src, width * 3, dst, height * 3, width, height, libyuv::kRotate90);
    } else if (degree == 180.0f) {
        libyuv::ARGBRotate(src, width * 3, dst, width * 3, width, height, libyuv::kRotate180);
    } else if (degree == 270.0f) {
        libyuv::ARGBRotate(src, width * 3, dst, height * 3, width, height, libyuv::kRotate270);
    } else {
        return;
    }
}

void libyuvRotateRGBA(unsigned char *src, unsigned char *dst, int width, int height, float degree) {
    if (degree == 90.0f) {
        libyuv::ARGBRotate(src, width * 4, dst, height * 4, width, height, libyuv::kRotate90);
    } else if (degree == 180.0f) {
        libyuv::ARGBRotate(src, width * 4, dst, width * 4, width, height, libyuv::kRotate180);
    } else if (degree == 270.0f) {
        libyuv::ARGBRotate(src, width * 4, dst, height * 4, width, height, libyuv::kRotate270);
    } else {
        return;
    }
}

void libyuvRotateYUV420P(unsigned char *src, unsigned char *dst, int width, int height, float degree) {
    unsigned char *pSrcY = src;
    unsigned char *pSrcU = src + width * height;
    unsigned char *pSrcV = src + width * height * 5 / 4;

    unsigned char *pDstY = dst;
    unsigned char *pDstU = dst + width * height;
    unsigned char *pDstV = dst + width * height * 5 / 4;

    if (degree == 90.0f) {
        libyuv::I420Rotate(pSrcY, width, pSrcU, width >> 1, pSrcV, width >> 1,
                   pDstY, height, pDstU, height >> 1, pDstV, height >> 1,
                   width, height, libyuv::kRotate90);
    } else if (degree == 180.0f) {
        libyuv::I420Rotate(pSrcY, width, pSrcU, width >> 1, pSrcV, width >> 1,
                   pDstY, width, pDstU, width >> 1, pDstV, width >> 1,
                   width, height, libyuv::kRotate180);
    } else if (degree == 270.0f) {
        libyuv::I420Rotate(pSrcY, width, pSrcU, width >> 1, pSrcV, width >> 1,
                   pDstY, height, pDstU, height >> 1, pDstV, height >> 1,
                   width, height, libyuv::kRotate270);
    } else {
        return;
    }
}

void libyuvRotateYUV420SP(unsigned char *src, unsigned char *dst, int width, int height, float degree) {
    unsigned char *pSrcY = src;
    unsigned char *pSrcUV = src + width * height;

    unsigned char *pDstY = dst;
    unsigned char *pDstUV = dst + width * height;

    unsigned char *pTmp = new unsigned char[width * height * 3 / 2];
    unsigned char *pTmpY = pTmp;
    unsigned char *pTmpU = pTmp + width * height;
    unsigned char *pTmpV = pTmp + width * height * 5 / 4;

    if (degree == 90.0f) {
        libyuv::NV12ToI420Rotate(pSrcY, width, pSrcUV, width,
                         pTmpY, height, pTmpU, height >> 1, pTmpV, height >> 1,
                         width, height, libyuv::kRotate90);
        libyuv::I420ToNV12(pTmpY, height, pTmpU, height >> 1, pTmpV, height >> 1,
                   pDstY, height, pDstUV, height, height, width);
    } else if (degree == 180.0f) {
        libyuv::NV12ToI420Rotate(pSrcY, width, pSrcUV, width,
                         pTmpY, width, pTmpU, width >> 1, pTmpV, width >> 1,
                         width, height, libyuv::kRotate180);
        libyuv::I420ToNV12(pTmpY, width, pTmpU, width >> 1, pTmpV, width >> 1,
                   pDstY, width, pDstUV, width, width, height);
    } else if (degree == 270.0f) {
        libyuv::NV12ToI420Rotate(pSrcY, width, pSrcUV, width,
                         pTmpY, height, pTmpU, height >> 1, pTmpV, height >> 1,
                         width, height, libyuv::kRotate270);
        libyuv::I420ToNV12(pTmpY, height, pTmpU, height >> 1, pTmpV, height >> 1,
                   pDstY, height, pDstUV, height, height, width);
    }
    delete[] pTmp;
}


