#include <jni.h>
#include "com_example_aaaa_test.h"

JNIEXPORT jstring JNICALL Java_com_example_aaaa_test_myHelloWord
  (JNIEnv *env, jobject obj)
{
	char buf[] = "Hello World in C!";

	jstring str;

	str = env->NewStringUTF(buf);

	return str;
}
