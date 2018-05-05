
package com.badlogicgames.gifski;

import org.junit.Test;

import com.badlogicgames.gifski.Gifski;
import com.badlogicgames.gifski.GifskiSettings;

public class GifskiTest {
	static {
		new SharedLibraryLoader().load("gifski-java");
	}

	@Test
	public void testGifski () {
		GifskiSettings settings = new GifskiSettings();
		Gifski gifski = new Gifski(settings);
		gifski.drop();
	}

	public static void main (String[] args) {
		System.out.println("Starting Gifski");

		GifskiSettings settings = new GifskiSettings();
		Gifski gifski = new Gifski(settings);

		gifski.startWriteThread("output.gif");

		byte[] frame = new byte[256 * 256 * 4];
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
		gifski.endAddingFrames();
		gifski.drop();

		System.out.println("Great success!");
	}
}
