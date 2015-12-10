#include <jni.h>
#include <math.h>
#include <android/bitmap.h>

#include "rgbautil.h"


extern "C" JNIEXPORT jint JNICALL

Java_com_asus_instantpage_util_RGBAUtilNative_compressARGBFromRGBANative(JNIEnv* env,
		jclass clazz,jobject img,jobject out,jint outBufCapacity,jint quality){
	jbyte* inBuf = (jbyte*) env->GetDirectBufferAddress(img);
	jbyte* outBuf = (jbyte*) env->GetDirectBufferAddress(out);
	auto flush = [](size_t numBytes) {
	    // do nothing
	 };
	return rgbautil::compress((unsigned char*)inBuf,(unsigned char*)out,outBufCapacity,flush,quality);
}
