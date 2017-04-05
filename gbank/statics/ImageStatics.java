package gbank.statics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class ImageStatics {
	private ImageStatics() {}

	// get the favicon image
	public static BufferedImage getFavicon() {
		try {
			return ImageIO.read(new File(FileLocations.getFaviconLocation()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
