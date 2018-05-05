
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
}
