# Fork notes
This fork is used by [gifski-java](https://github.com/badlogic/gifski-java) to compile static gifski libraries for 32- and 64-bit Windows & Linux as well as 64-bit macOS. The static libraries are then linked against the gifski-java JNI code. The fork is based on commit https://github.com/ImageOptim/gifski/commit/da3022d1c92200ed6b6549c77e278bc005b95b5d

This fork removes the gifski app and only keeps the lib parts. It also removes the dependency on lodepng which does not compile for MinGW targets.

## Windows & Linux
To (cross-)compile the static gifski library for Windows and Linux on Ubuntu via Rust:
1. Install Rust: `curl https://sh.rustup.rs -sSf | sh`
1. Install MinGW for your system:
	```
	sudo apt-get update && sudo apt-get install gcc g++ gcc-multilib g++-multilib mingw-w64 lib32z1
	```
1. Create the file `~/.cargo/config` and specify the location of the MinGW `gcc` binary for the two Windows targets. This is used by Rust as the linker. Note that the paths are for Ubuntu. On macOS it's in `/usr/local/bin/` when installing MinGW via brew. Use `which x86_64-w64-mingw32-gcc` to locate the path.

	```
	[target.x86_64-pc-windows-gnu]
	linker = "/usr/bin/x86_64-w64-mingw32-gcc"

	[target.i686-pc-windows-gnu]
	linker = "/usr/bin/i686-w64-mingw32-gcc"
	rustflags = "-C panic=abort"
	```
1. Add the platform targets via
	```
	rustup target add i686-pc-windows-gnu
	rustup target add x86_64-pc-windows-gnu
	```
1. Compile the static lib for the required platform, either in release or debug mode.
	```
	cargo build --target=x86_64-pc-windows-gnu --release
	cargo build --target=i686-pc-windows-gnu --release
	cargo build --target=i686-unknown-linux-gnu --release
	cargo build --target=x86_64-unknown-linux-gnu --release
	```

The static libraries will be located in `target/<platform-triple>/<release|debug>/` and will be called `libgifski.a` or `gifski.lib` depending on the operating system.

When linking the resulting gifski static lib for Windows via MinGW, the following linker flags need to be used:

```
x86_64-w64-mingw32-gcc test.c -L./ -lgifski -lwsock32 -lws2_32 -ldbghelp -luserenv -shared -o test.dll
```

## macOS
To compiling for macOS:
1. Install Rust: `curl https://sh.rustup.rs -sSf | sh`
1. Compile the static lib for the required platform, either in release or debug mode.
	```
	cargo build --release
	```

The static library will be located in `target/<release|debug>` and will be called `libgifski.a`

## Docker
The easiest way to compile the Windows & Linux binaries is to use the provided Docker image in the root of this repository. This way, no toolchains needs to be installed locally, and no VMs need to be setup. See the README.md file
in the root of the repository.
