//
// Created by daisg on 2021/1/23.
//
#ifndef LIBYUV_UTILS_H
#define LIBYUV_UTILS_H

#ifdef __cplusplus
extern "C" {
#endif
/*                        YUV 4:2:0 采样	YUV 4:2:0 采样
        YUV 420P 类型	YV12 格式	YU12 格式
        YUV 420SP 类型	NV12 格式	NV21 格式
        YU12 和 YV12 格式都属于 YUV 420P 类型，即先存储 Y 分量，再存储 U、V 分量，
        区别在于：YU12 是先 Y 再 U 后 V，而 YV12 是先 Y 再 V 后 U 。
        I420 即 YU12  YU12  即先存储 Y 分量，再存储 U、V 分量
        YV12 即先存储 Y 分量，再存储 V U分量
        NV12 是 IOS 中有的模式，它的存储顺序是先存 Y 分量，再 UV 进行交替存储。
        NV21 是 安卓 中有的模式，它的存储顺序是先存 Y 分量，在 VU 交替存储。
              YUV 420P 类型	 |   YUV 420SP 类型
        YU12(I420)| YV12     | NV12 |    NV21
        Y+U+V     |Y+V+U     |Y+UV  |    Y+VU
        */
void libyuvNV21ToI420(unsigned char *src_nv21_data, int width, int height, unsigned char *src_i420_data);
void libyuvI420ToNV21(unsigned char *src_i420_data, int width, int height, unsigned char *src_nv21_data);

void libyuvNV12ToI420(unsigned char *src_nv12_data, int width, int height, unsigned char *src_i420_data);
void libyuvI420ToNV12(unsigned char *src_i420_data, int width, int height, unsigned char *src_nv12_data);

//I420 即YU12 to rgb
void libyuvI420ToRGBA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvI420ToBGRA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvI420ToABGR(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvI420ToARGB(unsigned char *src, unsigned char *dst, int width, int height);

//yv12 to rgb
void libyuvYV12ToRGBA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvYV12ToBGRA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvYV12ToABGR(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvYV12ToARGB(unsigned char *src, unsigned char *dst, int width, int height);

//nv12 to rgb
void libyuvNV12ToRGBA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvNV12ToBGRA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvNV12ToABGR(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvNV12ToARGB(unsigned char *src, unsigned char *dst, int width, int height);

//nv21 to rgb
void libyuvNV21ToRGBA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvNV21ToBGRA(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvNV21ToABGR(unsigned char *src, unsigned char *dst, int width, int height);
void libyuvNV21ToARGB(unsigned char *src, unsigned char *dst, int width, int height);

// rgb to i420
void libyuvARGBToI420(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvABGRToI420(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvBGRAToI420(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvRGBAToI420(unsigned char*src,  unsigned char* dst, int width,  int height);

//rgb to rgb
void libyuvRGBAToARGB(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvARGBToRGBA(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvARGBToABGR(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvABGRToARGB(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvARGBToBGRA(unsigned char*src,  unsigned char* dst, int width,  int height);
void libyuvBGRAToARGB(unsigned char*src,  unsigned char* dst, int width,  int height);


void scaleI420(unsigned char *src_i420_data, int width, int height, unsigned char *dst_i420_data, int dst_width,
               int dst_height, int mode);

void rotateI420(unsigned char *src_i420_data, int width, int height, unsigned char *dst_i420_data, int degree);

void mirrorI420(unsigned char *src_i420_data, int width, int height, unsigned char *dst_i420_data);

void cropI420(unsigned char *src_i420_data, int width, int height,
              unsigned char *dst_i420_data,int dst_width,int dst_height,
              int left,int top);

void scaleARGB(unsigned char *src, int width, int height, unsigned char *dst, int dst_width,
               int dst_height, int mode);

void rotateARGB(unsigned char *src, int width, int height, unsigned char *dst, int degree);
void mirrorARGB(unsigned char *src, int width, int height, unsigned char *dst, int mode);

void libyuvRotateARGB(unsigned char *src, unsigned char *dst, int width, int height, float degree);

void libyuvRotateRGBA(unsigned char *src, unsigned char *dst, int width, int height, float degree);

void libyuvRotateYUV420P(unsigned char *src, unsigned char *dst, int width, int height, float degree);

void libyuvRotateYUV420SP(unsigned char *src, unsigned char *dst, int width, int height, float degree);
#ifdef __cplusplus
}
#endif


#endif //LIBYUV_UTILS_H
