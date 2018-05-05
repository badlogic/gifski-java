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

JNIEXPORT void JNICALL Java_com_badlogicgames_gifski_Gifski__1drop
  (JNIEnv *, jclass, jlong handle) {
  if (handle)
  	gifski_drop((gifski*)handle);  
}