
package com.badlogicgames.gifski;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Gifski {
	public static final int GIFSKI_OK = 0;
	/** one of input arguments was NULL */
	public static final int GIFSKI_NULL_ARG = 1;
	/** a one-time function was called twice, or functions were called in wrong order */
	public static final int GIFSKI_INVALID_STATE = 2;
	/** internal error related to palette quantization */
	public static final int GIFSKI_QUANT = 3;
	/** internal error related to gif composing */
	public static final int GIFSKI_GIF = 4;
	/** internal error related to multithreading */
	public static final int GIFSKI_THREAD_LOST = 5;
	/** I/O error: file or directory not found */
	public static final int GIFSKI_NOT_FOUND = 6;
	/** I/O error: permission denied */
	public static final int GIFSKI_PERMISSION_DENIED = 7;
	/** I/O error: file already exists */
	public static final int GIFSKI_ALREADY_EXISTS = 8;
	/** invalid arguments passed to function */
	public static final int GIFSKI_INVALID_INPUT = 9;
	/** misc I/O error */
	public static final int GIFSKI_TIMED_OUT = 10;
	/** misc I/O error */
	public static final int GIFSKI_WRITE_ZERO = 11;
	/** misc I/O error */
	public static final int GIFSKI_INTERRUPTED = 12;
	/** misc I/O error */
	public static final int GIFSKI_UNEXPECTED_EOF = 13;
	/** progress callback returned 0, writing aborted */
	public static final int ABORTED = 14;
	/** should not happen, file a bug */
	public static final int GIFSKI_OTHER = 15;

	long handle;
	Thread thread;

	static {
		new SharedLibraryLoader().load("gifski-java");
	}

	public Gifski (GifskiSettings settings) {
		handle = _newGifski(settings.width, settings.height, settings.quality, settings.once, settings.fast);
		if (handle == 0) throw new RuntimeException("Couldn't create Gifski instance.");
	}

	public void start (final String outputFile) {
		thread = new Thread(new Runnable() {
			@Override
			public void run () {
				write(outputFile);
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public int addFrameRGBA (int index, int width, int height, byte[] pixels, short delay) {
		return _addFrameRGBA(handle, index, width, height, pixels, delay);
	}

	public int addFrameRGBA (int index, int width, int height, ByteBuffer pixels, short delay) {
		return _addFrameRGBA(handle, index, width, height, pixels, delay);
	}

	public int end () {
		return _endAddingFrames(handle);
	}

	private int write (String fileName) {
		try {
			return _write(handle, fileName.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Couldn't get bytes of string. This should never happend.");
		}
	}

	public void dispose () {
		_drop(handle);
	}

	private static native long _newGifski (int width, int height, int quality, boolean once, boolean fast);

	private static native int _addFrameRGBA (long handle, int index, int width, int height, byte[] pixels, short delay);

	private static native int _addFrameRGBA (long handle, int index, int width, int height, ByteBuffer pixels, short delay);

	private static native int _endAddingFrames (long handle);

	private static native int _write (long handle, byte[] fileName);

	private static native void _drop (long handle);

	public static void main (String[] args) {
		System.out.println("Starting Gifski");
		GifskiSettings settings = new GifskiSettings();
		Gifski gifski = new Gifski(settings);

		byte[] frame = new byte[256 * 256 * 4];

		gifski.start("output.gif");

		for (int j = 0; j < 50; j++) {
			for (int i = 0; i < 256*256*4; i+=4) {
				byte val = (byte)(j * (256 / 60f));
				frame[i] = val;
				frame[i + 1] = (byte)0x0;
				frame[i + 2] = val;
				frame[i + 3] = (byte)0xff;
			}
			gifski.addFrameRGBA(j, 256, 256, frame, (short)4);
		}
		gifski.end();

		gifski.dispose();
		System.out.println("Great success!");
	}
}
