
package com.badlogicgames.pngquant;

import org.junit.Test;

import com.badlogicgames.gifski.Gifski;
import com.badlogicgames.gifski.GifskiSettings;

public class GifskiTest {
	@Test
	public void testGifski () {
		GifskiSettings settings = new GifskiSettings();
		Gifski gifski = new Gifski(settings);
		gifski.dispose();
	}
	
	public static void main (String[] args) {
		System.out.println("Starting Gifski");
		GifskiSettings settings = new GifskiSettings();
		Gifski gifski = new Gifski(settings);

		byte[] frame = new byte[256 * 256 * 4];

		gifski.start("output.gif");

		for (int j = 0; j < 50; j++) {
			for (int i = 0; i < 256 * 256 * 4; i += 4) {
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
