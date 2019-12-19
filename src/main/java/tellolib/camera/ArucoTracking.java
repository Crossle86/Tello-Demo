package tellolib.camera;

import java.util.logging.Logger;

import org.opencv.core.Mat;

public class ArucoTracking implements ArucoTrackingInterface
{
	private final 		Logger logger = Logger.getLogger("Tello");

	//private Aruco		aruco;

	private ArucoTracking()
	{
		//aruco = new Aruco();
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
	public boolean detectMarker( Mat frame )
	{
		return false;
	}
	
}
