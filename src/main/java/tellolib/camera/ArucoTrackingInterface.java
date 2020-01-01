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
	public boolean detectMarker(Mat frame);
}
