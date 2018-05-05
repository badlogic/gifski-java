/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_badlogicgames_gifski_Gifski */

#ifndef _Included_com_badlogicgames_gifski_Gifski
#define _Included_com_badlogicgames_gifski_Gifski
#ifdef __cplusplus
extern "C" {
#endif
#undef com_badlogicgames_gifski_Gifski_GIFSKI_OK
#define com_badlogicgames_gifski_Gifski_GIFSKI_OK 0L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_NULL_ARG
#define com_badlogicgames_gifski_Gifski_GIFSKI_NULL_ARG 1L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_INVALID_STATE
#define com_badlogicgames_gifski_Gifski_GIFSKI_INVALID_STATE 2L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_QUANT
#define com_badlogicgames_gifski_Gifski_GIFSKI_QUANT 3L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_GIF
#define com_badlogicgames_gifski_Gifski_GIFSKI_GIF 4L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_THREAD_LOST
#define com_badlogicgames_gifski_Gifski_GIFSKI_THREAD_LOST 5L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_NOT_FOUND
#define com_badlogicgames_gifski_Gifski_GIFSKI_NOT_FOUND 6L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_PERMISSION_DENIED
#define com_badlogicgames_gifski_Gifski_GIFSKI_PERMISSION_DENIED 7L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_ALREADY_EXISTS
#define com_badlogicgames_gifski_Gifski_GIFSKI_ALREADY_EXISTS 8L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_INVALID_INPUT
#define com_badlogicgames_gifski_Gifski_GIFSKI_INVALID_INPUT 9L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_TIMED_OUT
#define com_badlogicgames_gifski_Gifski_GIFSKI_TIMED_OUT 10L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_WRITE_ZERO
#define com_badlogicgames_gifski_Gifski_GIFSKI_WRITE_ZERO 11L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_INTERRUPTED
#define com_badlogicgames_gifski_Gifski_GIFSKI_INTERRUPTED 12L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_UNEXPECTED_EOF
#define com_badlogicgames_gifski_Gifski_GIFSKI_UNEXPECTED_EOF 13L
#undef com_badlogicgames_gifski_Gifski_ABORTED
#define com_badlogicgames_gifski_Gifski_ABORTED 14L
#undef com_badlogicgames_gifski_Gifski_GIFSKI_OTHER
#define com_badlogicgames_gifski_Gifski_GIFSKI_OTHER 15L
/*
 * Class:     com_badlogicgames_gifski_Gifski
 * Method:    _newGifski
 * Signature: (IIIZZ)J
 */
JNIEXPORT jlong JNICALL Java_com_badlogicgames_gifski_Gifski__1newGifski
  (JNIEnv *, jclass, jint, jint, jint, jboolean, jboolean);

/*
 * Class:     com_badlogicgames_gifski_Gifski
 * Method:    _addFrameRGBA
 * Signature: (JIII[BS)I
 */
JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1addFrameRGBA__JIII_3BS
  (JNIEnv *, jclass, jlong, jint, jint, jint, jbyteArray, jshort);

/*
 * Class:     com_badlogicgames_gifski_Gifski
 * Method:    _addFrameRGBA
 * Signature: (JIIILjava/nio/ByteBuffer;S)I
 */
JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1addFrameRGBA__JIIILjava_nio_ByteBuffer_2S
  (JNIEnv *, jclass, jlong, jint, jint, jint, jobject, jshort);

/*
 * Class:     com_badlogicgames_gifski_Gifski
 * Method:    _endAddingFrames
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1endAddingFrames
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_badlogicgames_gifski_Gifski
 * Method:    _write
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_com_badlogicgames_gifski_Gifski__1write
  (JNIEnv *, jclass, jlong, jbyteArray);

/*
 * Class:     com_badlogicgames_gifski_Gifski
 * Method:    _drop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_badlogicgames_gifski_Gifski__1drop
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
