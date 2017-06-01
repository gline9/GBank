package gbank.statics;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import gcore.tuples.mutable.MutablePair;

public final class WindowStatics {

	private WindowStatics() {}

	private static MutablePair<Double, Double> mainWindowLocation = new MutablePair<Double, Double>();

	private static Dimension screenDimensions = getScreenDimensions();

	private static Dimension getScreenDimensions() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static Point getMainWindowLocation() {

		// convert back to location for screen size
		return new Point(getMainWindowXLocation(), getMainWindowYLocation());
	}

	public static void setMainWindowLocation(Point location) {

		// convert to percent and save
		setMainWindowXPercent(location.getX() / screenDimensions.getWidth());
		setMainWindowYPercent(location.getY() / screenDimensions.getHeight());
	}

	public static void setMainWindowXLocation(int xLocation) {
		setMainWindowLocation(new Point(xLocation, getMainWindowYLocation()));
	}

	public static void setMainWindowYLocation(int yLocation) {
		setMainWindowLocation(new Point(getMainWindowXLocation(), yLocation));
	}

	public static int getMainWindowXLocation() {
		return (int) (getMainWindowXPercent() * screenDimensions.getWidth());
	}

	public static int getMainWindowYLocation() {
		return (int) (getMainWindowYPercent() * screenDimensions.getHeight());
	}

	public static double getMainWindowXPercent() {
		Double location = mainWindowLocation.getFirst();

		return location != null ? location : 0;
	}

	public static double getMainWindowYPercent() {
		Double location = mainWindowLocation.getSecond();

		return location != null ? location : 0;
	}

	public static void setMainWindowXPercent(double percent) {
		mainWindowLocation.setFirst(percent);
	}

	public static void setMainWindowYPercent(double percent) {
		mainWindowLocation.setSecond(percent);
	}

}
