package tellolib.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.opencv.aruco.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.*;

public class ArucoTracking implements ArucoTrackingInterface
{
	private final Logger		logger = Logger.getLogger("Tello");

	//private Aruco				aruco;
	private Dictionary			dict;
	//private DetectorParameters	parms;
	private Mat					ids;
	private List<Mat> 			corners;
	
	private ArucoTracking()
	{
		//aruco = new Aruco();
		
		dict = Aruco.getPredefinedDictionary(Aruco.DICT_ARUCO_ORIGINAL);
		
		//parms = DetectorParameters.create();
	}
    
	private static class SingletonHolder 
	{
        public static final ArucoTracking INSTANCE = new ArucoTracking();
    }
	
	/**
	 * Get the global instance of ArucoTracking class.
	 * @return Global ArucoTracking instance.
	 */
	public static ArucoTracking getInstance()
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
}
