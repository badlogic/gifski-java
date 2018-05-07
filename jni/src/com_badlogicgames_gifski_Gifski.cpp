#include "com_badlogicgames_gifski_Gifski.h"
#include "gifski.h"

JNIEXPORT jlong JNICALL Java_com_badlogicgames_gifski_Gifski__1newGifski
(JNIEnv *, jclass, jint width, jint height, jint quality, jboolean once, jboolean fast) {
	GifskiSettings settings;
	settings.width = width;
	settings.height = height;
	settings.quality = quality;
	settings.once = once;
	settings.fast = fast;
	return (jlong)gifski_new(&settings);
}

JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1addFrameRGBA__JIII_3BS
(JNIEnv *env, jclass, jlong handle, jint index, jint width, jint height, jbyteArray pixelsArray, jshort delay) {
	gifski* g = (gifski*)handle;
	const unsigned char* buffer = (const unsigned char*)env->GetPrimitiveArrayCritical(pixelsArray, 0);
	GifskiError error = gifski_add_frame_rgba(g, index, width, height, buffer, (uint16_t)delay);
	env->ReleasePrimitiveArrayCritical(pixelsArray, (void*)buffer, 0);
	return error;
}

JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1addFrameRGBA__JIIILjava_nio_ByteBuffer_2S
(JNIEnv * env, jclass, jlong handle, jint index, jint width, jint height, jobject pixelsBuffer, jshort delay) {
	gifski* g = (gifski*)handle;
	const unsigned char* buffer = (const unsigned char*)(pixelsBuffer ? env->GetDirectBufferAddress(pixelsBuffer) : 0);
	return gifski_add_frame_rgba(g, index, width, height, buffer, (uint16_t)delay);
}

JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1endAddingFrames
(JNIEnv *, jclass, jlong handle) {
	gifski* g = (gifski*)handle;
	return gifski_end_adding_frames(g);
}

JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1write
(JNIEnv *env, jclass, jlong handle, jbyteArray fileName) {
	gifski* g = (gifski*)handle;
	const char* fileNameAddr = (const char*)env->GetPrimitiveArrayCritical(fileName, 0);
	GifskiError error = gifski_write(g, fileNameAddr);
	env->ReleasePrimitiveArrayCritical(fileName, (void*)fileNameAddr, 0);
	return error;
}

JNIEXPORT void JNICALL Java_com_badlogicgames_gifski_Gifski__1drop
(JNIEnv *, jclass, jlong handle) {
	if (handle) gifski_drop((gifski*)handle);
}