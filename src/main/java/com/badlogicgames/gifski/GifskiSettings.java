
package com.badlogicgames.gifski;

public class GifskiSettings {
	/** The width of the GIF, or 0 to use the maximum width of all frames. */
	public int width;
	/** The width of the GIF, or 0 to use the maximum height of all frames. */
	public int height;
	/** The quantization quality. 1-100, but the useful range is 50-100. 100 is recommended. */
	public byte quality = 100;
	/** If false, the GIF is not repeated. */
	public boolean once;
	/** If true, the quality is lower but encoding is faster. */
	public boolean fast;
}
