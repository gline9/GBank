package gbank.statics;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;

import gcore.tuples.mutable.MutablePair;

public final class WindowStatics {

	private WindowStatics() {}

	private static HashMap<String, MutablePair<Double, Double>> windowLocationMap = new HashMap<>();

	private static Dimension screenDimensions = getScreenDimensions();

	private static Dimension getScreenDimensions() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static Point getWindowLocation(String id) {

		// convert back to location for screen size
		return new Point(getWindowXLocation(id), getWindowYLocation(id));
	}

	public static void setWindowLocation(Point location, String id) {

		// convert to percent and save
		setWindowXPercent(location.getX() / screenDimensions.getWidth(), id);
		setWindowYPercent(location.getY() / screenDimensions.getHeight(), id);
	}

	public static void setWindowXLocation(int xLocation, String id) {
		setWindowLocation(new Point(xLocation, getWindowYLocation(id)), id);
	}

	public static void setWindowYLocation(int yLocation, String id) {
		setWindowLocation(new Point(getWindowXLocation(id), yLocation), id);
	}

	public static int getWindowXLocation(String id) {
		return (int) (getWindowXPercent(id) * screenDimensions.getWidth());
	}

	public static int getWindowYLocation(String id) {
		return (int) (getWindowYPercent(id) * screenDimensions.getHeight());
	}

	public static double getWindowXPercent(String id) {
		Double location = getPair(id).getFirst();

		return location != null ? location : 0;
	}

	public static double getWindowYPercent(String id) {
		Double location = getPair(id).getSecond();

		return location != null ? location : 0;
	}

	public static void setWindowXPercent(double percent, String id) {
		getPair(id).setFirst(percent);
	}

	public static void setWindowYPercent(double percent, String id) {
		getPair(id).setSecond(percent);
	}

	private static MutablePair<Double, Double> getPair(String id) {
		// get the pair
		MutablePair<Double, Double> results = windowLocationMap.get(id);

		// if the pair isn't set, set it.
		if (results == null) {
			results = new MutablePair<>();
			windowLocationMap.put(id, results);
		}

		// return the results
		return results;
	}
	
	protected static Iterator<String> getIDIterator(){
		return windowLocationMap.keySet().iterator();
	}

}
