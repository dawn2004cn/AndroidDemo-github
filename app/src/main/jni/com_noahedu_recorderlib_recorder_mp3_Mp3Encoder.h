/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_noahedu_recorderlib_recorder_mp3_Mp3Encoder */

#ifndef _Included_com_noahedu_recorderlib_recorder_mp3_Mp3Encoder
#define _Included_com_noahedu_recorderlib_recorder_mp3_Mp3Encoder
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_noahedu_recorderlib_recorder_mp3_Mp3Encoder
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_noahedu_recorderlib_recorder_mp3_Mp3Encoder_close
  (JNIEnv *, jclass);

/*
 * Class:     com_noahedu_recorderlib_recorder_mp3_Mp3Encoder
 * Method:    encode
 * Signature: ([S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_com_noahedu_recorderlib_recorder_mp3_Mp3Encoder_encode
  (JNIEnv *, jclass, jshortArray, jshortArray, jint, jbyteArray);

/*
 * Class:     com_noahedu_recorderlib_recorder_mp3_Mp3Encoder
 * Method:    flush
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_noahedu_recorderlib_recorder_mp3_Mp3Encoder_flush
  (JNIEnv *, jclass, jbyteArray);

/*
 * Class:     com_noahedu_recorderlib_recorder_mp3_Mp3Encoder
 * Method:    init
 * Signature: (IIIII)V
 */
JNIEXPORT void JNICALL Java_com_noahedu_recorderlib_recorder_mp3_Mp3Encoder_init
  (JNIEnv *, jclass, jint, jint, jint, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
