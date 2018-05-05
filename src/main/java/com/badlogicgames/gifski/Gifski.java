
package com.badlogicgames.gifski;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Gifski {
	private final long handle;

	public Gifski (GifskiSettings settings) {
		handle = _newGifski(settings.width, settings.height, settings.quality, settings.once, settings.fast);
		if (handle == 0) throw new RuntimeException("Unable to create Gifski instance.");
	}

	public Result addFrameRGBA (int index, int width, int height, byte[] pixels, short delay) {
		return result(_addFrameRGBA(handle, index, width, height, pixels, delay));
	}

	public Result addFrameRGBA (int index, int width, int height, ByteBuffer pixels, short delay) {
		return result(_addFrameRGBA(handle, index, width, height, pixels, delay));
	}

	public Result endAddingFrames () {
		return result(_endAddingFrames(handle));
	}

	public Result write (String outputFile) {
		try {
			return result(_write(handle, outputFile.getBytes("utf-8")));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex); // Should never happen.
		}
	}

	private Result result (int code) {
		if (code < 0 || code >= Result.values.length) return Result.OTHER;
		return Result.values[code];
	}

	/** Starts a thread that calls {@link #write(String)} and then {@link #drop()}. */
	public void startWriteThread (final String outputFile) {
		new Thread("GifskiWrite") {
			public void run () {
				try {
					Result result = write(outputFile);
					if (result != Result.OK) throw new RuntimeException("Gifski error: " + result);
				} finally {
					drop();
				}
			}
		}.start();
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

	static public enum Result {
		/** Success. */
		OK,
		/** One of input arguments was NULL. */
		NULL_ARG,
		/** A one-time function was called twice, or functions were called in wrong order. */
		INVALID_STATE,
		/** Internal error related to palette quantization. */
		QUANT,
		/** Internal error related to gif composing. */
		GIF,
		/** Internal error related to multithreading. */
		THREAD_LOST,
		/** I/O error: file or directory not found. */
		NOT_FOUND,
		/** I/O error: permission denied. */
		PERMISSION_DENIED,
		/** I/O error: file already exists. */
		ALREADY_EXISTS,
		/** Invalid arguments passed to function. */
		INVALID_INPUT,
		/** Misc I/O error. */
		TIMED_OUT,
		/** Misc I/O error. */
		WRITE_ZERO,
		/** Misc I/O error. */
		INTERRUPTED,
		/** Misc I/O error. */
		UNEXPECTED_EOF,
		/** Progress callback returned 0, writing aborted. */
		ABORTED,
		/** Should not happen, file a bug. */
		OTHER,
		/** Invalid result, file a bug. */
		INVALID;

		static final Result[] values = values();
	}
}
