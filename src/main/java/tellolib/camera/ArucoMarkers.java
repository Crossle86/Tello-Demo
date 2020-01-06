package tellolib.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.opencv.aruco.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.*;

/**
 * Convenience functions for Opencv Aruco Markers feature.
 */
public class ArucoMarkers implements ArucoMarkersInterface
{
	private final Logger		logger = Logger.getLogger("Tello");

	private Dictionary			dict;
	private Mat					ids;
	private List<Mat> 			corners;
	
	private ArucoMarkers()
	{
		//aruco = new Aruco();
		
		dict = Aruco.getPredefinedDictionary(Aruco.DICT_ARUCO_ORIGINAL);
		
		//parms = DetectorParameters.create();
	}
    
	private static class SingletonHolder 
	{
        public static final ArucoMarkers INSTANCE = new ArucoMarkers();
    }
	
	/**
	 * Get the global instance of ArucoTracking class.
	 * @return Global ArucoTracking instance.
	 */
	public static ArucoMarkers getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	@Override
	public boolean detectMarkers(Mat frame)
	{
		if (frame == null) return false;
		
		Mat	grayFrame = new Mat();

		ids = new Mat();
		corners = new Vector<Mat>();
		
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		
		Aruco.detectMarkers(grayFrame, dict, corners, ids);
		
		if (ids.empty()) return false;
		
		logger.fine("ids=" + ids.dump());
		
		return true;
	}

	@Override
	public int getMarkerCount()
	{
		if (ids == null) return 0;

		return (int) ids.total();
	}

	@Override
	public int getMarkerId( int index )
	{
		if (ids == null) return -1;

		if (index >= ids.total() || index < 0) return -1;
		
		return (int) ids.get(index, 0)[0];
	}

	@Override
	public ArrayList<Rect> getMarkerTargets()
	{
		ArrayList<Rect>	targetRectangles;
		
		if  (ids == null) return null;
		
		targetRectangles = new ArrayList<Rect>();
		
		for (int i = 0; i < getMarkerCount(); i++)
		{
			Mat mat = corners.get(i);

			//logger.fine("corner(" + i + ")=" + mat.dump());
			//logger.fine("x1=" + mat.get(i, 0)[0] + "y1=" + mat.get(i, 0)[1]);
			//logger.fine("x2=" + mat.get(i, 1)[0] + "y2=" + mat.get(i, 1)[1]);
			
			int x1 = (int) mat.get(i, 0)[0];
			int y1 = (int) mat.get(i, 0)[1];
			int x2 = (int) mat.get(i, 1)[0];
			int y2 = (int) mat.get(i, 1)[1];
			int x3 = (int) mat.get(i, 2)[0];
			int y3 = (int) mat.get(i, 2)[1];
			int x4 = (int) mat.get(i, 3)[0];
			int y4 = (int) mat.get(i, 3)[1];

			Rect rect = new Rect();
			rect.x = x1;
			rect.y = y1;
			rect.width = x2 - x1;
			rect.height = y4 - y1;
			
			targetRectangles.add(rect);
			
			//logger.fine("rect=" + rect.toString());
		}
		
		return targetRectangles;
	}
}
