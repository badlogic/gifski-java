
package com.badlogicgames.gifski;

public class GifskiTest {
	public static void main (String[] args) {
		int seconds = 2;
		int frameCount = 50;

		GifskiSettings settings = new GifskiSettings();
		settings.width = 256;
		settings.height = 256;
		settings.quality = 100;
		settings.once = false;
		settings.fast = false;

		Gifski gifski = new Gifski(settings);
		gifski.start("output.gif");
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
		gifski.end();
	}
}
