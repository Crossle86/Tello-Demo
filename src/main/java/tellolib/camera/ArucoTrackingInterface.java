package tellolib.camera;

import org.opencv.core.Mat;
import org.opencv.aruco.*;

public interface ArucoTrackingInterface
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
}
