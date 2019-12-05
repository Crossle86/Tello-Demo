package tello.camera;

import java.util.logging.Logger;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.*;
import org.opencv.highgui.HighGui;

import tellolib.drone.TelloDrone;


public class TelloCamera implements TelloCameraInterface
{
	private final 			Logger logger = Logger.getLogger("Tello");

	private boolean			recording;
	private Thread			videoCaptureThread;
	private VideoCapture	vc;

	public TelloCamera()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	@Override
	public void startVideoCapture()
	{
		logger.fine("starting video capture thread");
		
		if (vc != null) return;

		vc = new VideoCapture("udp://127.0.0.1:" + Integer.toString(TelloDrone.UDP_VIDEO_PORT));
		
		//videoCaptureThread = new VideoCaptureThread();
		//videoCaptureThread.start();
	}
	
	@Override
	public void stopVideoCapture()
	{
		if (vc != null) videoCaptureThread.interrupt();

		logger.fine("stopping video capture thread");
		
		vc.release();
		
		vc = null;
	}

	private class VideoCaptureThread extends Thread
	{
		VideoCaptureThread()
		{
			logger.fine("video thread constructor");
			
			this.setName("VideoCapture");
	    }
		
	    public void run()
	    {
			logger.fine("video thread start");
			
	    	try
	    	{
	    		while (!isInterrupted())
	    		{
	    		}
	    	}
	    	catch (Exception e) { logger.warning("video capture failed: " + e.getMessage()); }
	    	finally {}
	    	
	    	videoCaptureThread =  null;
	    }
	}

	@Override
	public void takePicture( String folder )
	{
		Mat	image  =  null;
		
		if (vc == null) return;
		
		if(vc.read(image)) HighGui.imshow("Tello", image);
		
		logger.fine("Picture saved to " + folder);
	}

	@Override
	public void startRecording( String folder )
	{
		if (vc == null) return;
		
		recording =  true;
		
		logger.fine("Video recording started to " + folder);
	}

	@Override
	public void stopRecording()
	{
		if (vc == null || !recording) return;
		
		recording =  false;
		
		logger.fine("Video recording stopped...");
	}
	
	@Override
	public boolean isRecording()
	{
		return recording;
	}
}
