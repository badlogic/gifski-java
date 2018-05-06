
package com.badlogicgames.gifski;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Gifski {
	static {
		new SharedLibraryLoader().load("gifski-java");
	}

	private final long handle;
	private final int width, height, quality;
	private final boolean once, fast;

	Result writeResult;
	private Thread writeThread;

	public Gifski (GifskiSettings settings) {
		if (settings.width < 0) throw new IllegalArgumentException("width must be >= 0: " + settings.width);
		if (settings.height < 0) throw new IllegalArgumentException("height must be >= 0: " + settings.height);
		if (settings.quality < 1 || settings.quality > 100)
			throw new IllegalArgumentException("quality must be between 1 and 100: " + settings.quality);
		this.width = settings.width;
		this.height = settings.height;
		this.quality = settings.quality;
		this.once = settings.once;
		this.fast = settings.fast;

		handle = _newGifski(width, height, quality, once, fast);
		if (handle == 0) throw new RuntimeException("Unable to create Gifski instance.");
	}

	/** @param pixels An array with width * height * 4 bytes. The array contents is copied. */
	public Result addFrameRGBA (int index, int width, int height, byte[] pixels, short delay) {
		if (pixels.length < width * height * 4)
			throw new IllegalArgumentException("pixels must have " + width + " * " + height + " * 4 length: " + pixels.length);
		return result(_addFrameRGBA(handle, index, width, height, pixels, delay));
	}

	/** @param pixels A buffer with width * height * 4 bytes. The buffer position is ignored, the data must start at position 0.
	 *           The buffer contents is copied. */
	public Result addFrameRGBA (int index, int width, int height, ByteBuffer pixels, short delay) {
		if (pixels.capacity() < width * height * 4)
			throw new IllegalArgumentException("pixels must have " + width + " * " + height + " * 4 capacity: " + pixels.capacity());
		return result(_addFrameRGBA(handle, index, width, height, pixels, delay));
	}

	/** Call after adding frames to allow {@link #write(String)} to return. */
	public Result endAddingFrames () {
		return result(_endAddingFrames(handle));
	}

	/** Waits for frames to be added until {@link #endAddingFrames()} is called. */
	public Result write (String outputFile) {
		if (outputFile == null) throw new IllegalArgumentException("outputFile cannot be null.");
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

	/** Releases all memory. This instance must not be used afterward. */
	public void drop () {
		_drop(handle);
	}

	/** Starts a thread that calls {@link #write(String)} and then {@link #drop()}. */
	public void start (final String outputFile) {
		if (outputFile == null) throw new IllegalArgumentException("outputFile cannot be null.");
		if (writeThread != null) throw new IllegalStateException("Write thread is already running.");
		writeThread = new Thread("GifskiWrite") {
			public void run () {
				writeResult = write(outputFile);
				drop();
			}
		};
		writeThread.start();
	}

	/** Calls {@link #endAddingFrames()}, waits for the write thread to stop, and returns the result of the {@link #write(String)}.
	 * Note that because the write thread calls {@link #drop()}, this Gifski instance can no longer be used after calling this
	 * method. */
	public Result end () {
		if (writeThread == null) throw new IllegalStateException("Write thread is not running.");
		endAddingFrames();
		try {
			writeThread.join();
		} catch (InterruptedException ignored) {
		}
		writeThread = null;
		return writeResult;
	}

	static private native long _newGifski (int width, int height, int quality, boolean once, boolean fast);

	static private native int _addFrameRGBA (long handle, int index, int width, int height, byte[] pixels, short delay);

	static private native int _addFrameRGBA (long handle, int index, int width, int height, ByteBuffer pixels, short delay);

	static private native int _endAddingFrames (long handle);

	static private native int _write (long handle, byte[] fileName);

	static private native void _drop (long handle);

	public int getWidth () {
		return width;
	}

	public int getHeight () {
		return height;
	}

	public int getQuality () {
		return quality;
	}

	public boolean getOnce () {
		return once;
	}

	public boolean getFast () {
		return fast;
	}

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
