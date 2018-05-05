
package com.badlogicgames.gifski;

public class Gifski {
	long handle;

	static {
		new SharedLibraryLoader().load("gifski-java");
	}

	public Gifski (GifskiSettings settings) {
		handle = _newGifski(settings.width, settings.height, settings.quality, settings.once, settings.fast);
		if (handle == 0) throw new RuntimeException("Couldn't create Gifski instance.");
	}

	public void dispose () {
		_drop(handle);
	}

	private static native long _newGifski (int width, int height, int quality, boolean once, boolean fast);

	private static native void _drop (long handle);

	public static void main (String[] args) {
		System.out.println("Starting Gifski");
		GifskiSettings settings = new GifskiSettings();
		Gifski gifski = new Gifski(settings);
		gifski.dispose();
		System.out.println("Great success!");
	}
}
