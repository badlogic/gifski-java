package com.badlogicgames.pngquant;

import java.io.Closeable;
import java.io.IOException;

public class PngQuant implements Closeable {
	long ptr;

	static {
		new SharedLibraryLoader().load("pngquant-java");
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
