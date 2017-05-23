package gbank.statics;

import java.awt.Point;

public final class WindowStatics {

	private WindowStatics() {}

	private static Point mainWindowLocation = new Point();

	public static Point getMainWindowLocation() {
		return mainWindowLocation;
	}
	
	public static void setMainWindowLocation(Point location){
		mainWindowLocation = location;
	}
	
	public static void setMainWindowXLocation(int xLocation){
		mainWindowLocation.setLocation(xLocation, mainWindowLocation.y);
	}
	
	public static void setMainWindowYLocation(int yLocation){
		mainWindowLocation.setLocation(mainWindowLocation.x, yLocation);
	}

}
