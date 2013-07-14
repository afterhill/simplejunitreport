package simple.junit.reporter;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

import junit.framework.Test;

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.w3c.dom.Element;

import simple.junit.reporter.GUITests;
import simple.junit.reporter.ImageException;
import simple.junit.reporter.ScreenshotTaker;

import static org.apache.tools.ant.taskdefs.optional.junit.JUnitVersionHelper2.testClassName;
import static org.apache.tools.ant.taskdefs.optional.junit.JUnitVersionHelper2.testMethodName;
import static org.apache.tools.ant.taskdefs.optional.junit.XMLConstants.ERROR;
import static simple.junit.reporter.ScreenshotTaker.PNG_EXTENSION;
import static simple.junit.utils.Strings.isNullOrEmpty;
import static simple.junit.utils.Strings.join;

public final class ScreenshotOnFailureResultFormatter extends
		XmlJUnitResultFormatter {

	private static final String SCREENSHOT_ELEMENT = "screenshot";
	private static final String SCREENSHOT_FILE_ATTRIBUTE = "file";

	private ScreenshotTaker screenshotTaker;
	private boolean ready;

	private ImageException couldNotCreateScreenshotTaker;

	public ScreenshotOnFailureResultFormatter() {
		try {
			screenshotTaker = new ScreenshotTaker();
			ready = screenshotTaker != null;
		} catch (ImageException e) {
			couldNotCreateScreenshotTaker = e;
		}
	}

	@Override
	protected void onStartTestSuite(JUnitTest suite) {
		if (couldNotCreateScreenshotTaker == null)
			return;
		writeCouldNotCreateScreenshotTakerError();
		couldNotCreateScreenshotTaker = null;
		return;
	}

	private void writeCouldNotCreateScreenshotTakerError() {
		Element errorElement = document().createElement(ERROR);
		writeErrorAndStackTrace(couldNotCreateScreenshotTaker, errorElement);
		rootElement().appendChild(errorElement);
	}

	@Override
	protected void onFailureOrError(Test test, Throwable error,
			Element errorElement) {
		if (!ready)
			return;
		String className = testClassName(test);
		String methodName = testMethodName(test);
//		if (!isGUITest(className, methodName))
//			return;
		String image = takeScreenshotAndReturnEncoded();
		if (isNullOrEmpty(image))
			return;
		String imageFileName = join(className, methodName, PNG_EXTENSION).with(".");
		writeScreenshotFileName(image, imageFileName, errorElement);
	}

	private boolean isGUITest(String className, String methodName) {
		try {
			Class<?> testClass = Class.forName(className);
			Method testMethod = testClass.getDeclaredMethod(methodName,
					new Class<?>[0]);
			return GUITests.isGUITest(testClass, testMethod);
		} catch (Exception e) {
			return false;
		}
	}

	private String takeScreenshotAndReturnEncoded() {
		BufferedImage image = screenshotTaker.takeDesktopScreenshot();
		return ImageHandler.encodeBase64(image);
	}

	private void writeScreenshotFileName(String encodedImage,
			String imageFileName, Element errorElement) {
		Element screenshotElement = document()
				.createElement(SCREENSHOT_ELEMENT);
		screenshotElement
				.setAttribute(SCREENSHOT_FILE_ATTRIBUTE, imageFileName);
		writeText(encodedImage, screenshotElement);
		errorElement.getParentNode().appendChild(screenshotElement);
	}
}
