package simple.junit.reporter;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import static simple.junit.utils.Files.newFile;
import static simple.junit.utils.Strings.concat;
import static simple.junit.utils.Strings.isNullOrEmpty;
import static simple.junit.utils.Strings.quote;

public final class ScreenshotTaker {

	public static final String PNG_EXTENSION = "png";

	private final Robot robot;

	public ScreenshotTaker() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new ImageException("Unable to create AWT Robot", e);
		}
	}

	public void saveDesktopAsPng(String imageFilePath) {
		validate(imageFilePath);
		BufferedImage screenshot = takeDesktopScreenshot();
		try {
			ImageIO.write(screenshot, PNG_EXTENSION, newFile(imageFilePath));
		} catch (IOException e) {
			throw new ImageException(concat("Unable to save screenshot as ",
					quote(imageFilePath)), e);
		}
	}

	public BufferedImage takeDesktopScreenshot() {
		Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize());
		return robot.createScreenCapture(screen);
	}

	private void validate(String imageFilePath) {
		if (isNullOrEmpty(imageFilePath))
			throw new ImageException("The image path cannot be empty");
		if (!imageFilePath.endsWith(PNG_EXTENSION))
			throw new ImageException(concat("The image file should be a ",
					PNG_EXTENSION.toUpperCase()));
	}
}
