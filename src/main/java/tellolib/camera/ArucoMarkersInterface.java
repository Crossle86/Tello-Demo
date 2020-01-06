package tellolib.camera;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;

import org.opencv.aruco.*;

/**
 * Convenience functions for Opencv Aruco Markers feature.
 */
public interface ArucoMarkersInterface
{
	/**
	 * Perform Aruco marker detection on the supplied image.
	 * @param frame Image to analyze for markers.
	 * @return True if marker(s) detected, false if not.
	 */
	public boolean detectMarkers(Mat frame);
	
	/**
	 * Get the number of markers detected on last call to 
	 * detectMarkers().
	 * @return Number of detected markers.
	 */
	public int getMarkerCount();
	
	/**
	 * Get the marker id of the selected marker detected by the
	 * last call to detectMarkers().
	 * @param index Marker to select indexed from 0.
	 * @return Marker id number or -1 if no markers or index out of range.
	 */
	public int getMarkerId(int index);
	
	/**
	 * Get detected markers as rectangles located within the image used in
	 * last call to detectMarkers() as x,y,h,w.
	 * @return List of target rectangles or null if no markers found.
	 */
	public ArrayList<Rect> getMarkerTargets();
}
