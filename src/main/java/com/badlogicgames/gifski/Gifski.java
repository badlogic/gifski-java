
package com.badlogicgames.gifski;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Gifski {
	static public final int GIFSKI_OK = 0;
	/** one of input arguments was NULL */
	static public final int GIFSKI_NULL_ARG = 1;
	/** a one-time function was called twice, or functions were called in wrong order */
	static public final int GIFSKI_INVALID_STATE = 2;
	/** internal error related to palette quantization */
	static public final int GIFSKI_QUANT = 3;
	/** internal error related to gif composing */
	static public final int GIFSKI_GIF = 4;
	/** internal error related to multithreading */
	static public final int GIFSKI_THREAD_LOST = 5;
	/** I/O error: file or directory not found */
	static public final int GIFSKI_NOT_FOUND = 6;
	/** I/O error: permission denied */
	static public final int GIFSKI_PERMISSION_DENIED = 7;
	/** I/O error: file already exists */
	static public final int GIFSKI_ALREADY_EXISTS = 8;
	/** invalid arguments passed to function */
	static public final int GIFSKI_INVALID_INPUT = 9;
	/** misc I/O error */
	static public final int GIFSKI_TIMED_OUT = 10;
	/** misc I/O error */
	static public final int GIFSKI_WRITE_ZERO = 11;
	/** misc I/O error */
	static public final int GIFSKI_INTERRUPTED = 12;
	/** misc I/O error */
	static public final int GIFSKI_UNEXPECTED_EOF = 13;
	/** progress callback returned 0, writing aborted */
	static public final int ABORTED = 14;
	/** should not happen, file a bug */
	static public final int GIFSKI_OTHER = 15;

	private final long handle;

	public Gifski (GifskiSettings settings) {
		handle = _newGifski(settings.width, settings.height, settings.quality, settings.once, settings.fast);
		if (handle == 0) throw new RuntimeException("Unableto create Gifski instance.");
	}

	public int addFrameRGBA (int index, int width, int height, byte[] pixels, short delay) {
		return _addFrameRGBA(handle, index, width, height, pixels, delay);
	}

	public int addFrameRGBA (int index, int width, int height, ByteBuffer pixels, short delay) {
		return _addFrameRGBA(handle, index, width, height, pixels, delay);
	}

	public int endAddingFrames () {
		return _endAddingFrames(handle);
	}

	public int write (String outputFile) {
		try {
			return _write(handle, outputFile.getBytes("utf-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex); // Should never happen.
		}
	}

	public void startWriteThread (final String outputFile) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run () {
				int error = write(outputFile);
				if (error != GIFSKI_OK) throw new RuntimeException("Gifski error: " + error);
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public void drop () {
		_drop(handle);
	}

	static private native long _newGifski (int width, int height, int quality, boolean once, boolean fast);

	static private native int _addFrameRGBA (long handle, int index, int width, int height, byte[] pixels, short delay);

	static private native int _addFrameRGBA (long handle, int index, int width, int height, ByteBuffer pixels, short delay);

	static private native int _endAddingFrames (long handle);

	static private native int _write (long handle, byte[] fileName);

	static private native void _drop (long handle);
}
