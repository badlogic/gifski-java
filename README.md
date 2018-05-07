# gifski-java
gifski-java is a JNI wrapper around the C API for [Gifski](https://gif.ski/) enabling you to encode sequences of 32-bit RGBA images to high-quality animated GIFs.

gifski-java is kept intentionally simple, only operating on `byte[]` arrays and direct `ByteBuffer` instances.

gifski-java bundles the required native shared library and automatically loads the library comaptible with your operating system. No messing around with `-Djava.library.path`. gifski-java supports Windows 32- & 64-bit, Linux 32- & 64-bit as well as macOS out of the box.

## Installation
gifski-java is published to Maven Central. You can include it in your `pom.xml` as a dependency as follows:

```xml
<dependency>
	<groupId>com.badlogicgames.gdx</groupId>
	<artifactId>gifski-java</artifactId>
	<version>1.0</version>
</dependency>
```

To include it in your Gradle project, ensure your `build.gradle` file adds the `mavenCentral()` repository. Then add the dependency:

```groovy
compile "com.badlogicgames.gdx:gifski-java:1.0"
```

gifski-java is also build on every new commit by [Jenkins](https://libgdx.badlogicgames.com/jenkins/job/gifski-java/) and published as a [SNAPSHOT release](https://oss.sonatype.org/content/repositories/snapshots/com/badlogicgames/gifski-java/) to SonaType.

## Usage
The below code generates a fade-in animation of a pink rectangle, with a 2 second duration at 25 frames per second.

```java
// Animation duration and frame rate (frameCount / seconds)
int seconds = 2;
int frameCount = 50;

// Define Gifski encoding settings
GifskiSettings settings = new GifskiSettings();
settings.width = 256;
settings.height = 256;
settings.quality = 100;
settings.once = false;
settings.fast = false;

// Create the Gifski instance and start writting
// the output GIF file
Gifski gifski = new Gifski(settings);
gifski.start("output.gif");

// Generate frames and tell gifski to add them to the
// animation
short delay = (short)(seconds * 100 / frameCount);
byte[] pixels = new byte[settings.width * settings.height * 4];
for (int i = 0; i < frameCount; i++) {
	for (int p = 0, n = pixels.length; p < n; p += 4) {
		byte color = (byte)(i * 256f / frameCount);
		pixels[p] = color;
		pixels[p + 1] = 0;
		pixels[p + 2] = color;
		pixels[p + 3] = (byte)0xff;
	}
	gifski.addFrameRGBA(i, settings.width, settings.height, pixels, delay);
}

// Tell Gifski to stop encoding and close the GIF file
gifski.end();
```

**Note**: gifski-java automatically sets up the [ additional thread](https://github.com/ImageOptim/gifski/blob/master/gifski.h#L11) for writing the added frames to the output file. Call `Gifski#start()` and `Gifski#end()` which will ensure the proper setup and tear down of the thread. The Java API allows you to manage the threads manually by exposing the `write()`, `drop()` and `endAddingFrames()` methods, which are direct wrappers of the C API.

Refer to the ['gifski.h`](https://github.com/ImageOptim/gifski/blob/master/gifski.h) file of the C API for more information.

## Working from source
gifski-java includes the upstream Gifski repository as a submodule in `jni/gifski`. Make sure to clone this repository recursively to also check out the submodule:

```
git clone --recursive https://github.com/badlogic/gifski-java
```

gifski-java is a plain old Maven project and can be imported into any Maven aware IDE. Before importing, run

```
mvn clean compile
```

from a terminal. This will download the latest native shared libraries from [Jenkins](https://libgdx.badlogicgames.com/ci/gifski-java/binaries/) so you do not have to build them yourself. After the above Maven invocation, the native shared libraries will be located in `src/main/resources`.

### Building the native libraries
To build the native shared libraries yourself, you can use the included `jni/build-docker.sh` script for Windows 32-bit, Windows 64-bit, Linux 32-bit and Linux 64-bit. Install [Docker CE](https://www.docker.com/community-edition), then in your terminal:

```bash
cd jni/
./build-docker.sh
```

This will build a Docker image with all required toolchains for Windows and Linux, compile the gifski-java shared libraries and put them in `src/main/resources`.

Finally, if you want to build a shared library for your specific host, use the `jni/build.sh` script:

```bash
/build.sh --target=<target-os-arch> --build=<release|debug>
```

Valid values for the `--target` parameter to pass to `build.sh` are `macosx`, `windows-x86`, `windows-x86_64`, `linux-64` and `linux-x86_64`.

Valid values for the `--build` parameter are `release` and `debug`.

To build locally, all required toolchains must be installed. The [`Dockerfile`](https://github.com/badlogic/gifski-java/blob/master/jni/Dockerfile#L3) shows you a toolchain setup to compile for Windows & Linux on Linux. See the [`jni/README.md`](https://github.com/badlogic/gifski-java/blob/master/jni/README.md) file for more information.

## License
AGPL 3, See [LICENSE](https://github.com/badlogic/gifski-java/blob/master/LICENSE). For alternative licensing options contact the Gifski [author](https://kornel.ski/contact).