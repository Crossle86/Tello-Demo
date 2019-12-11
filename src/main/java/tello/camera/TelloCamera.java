package tello.camera;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.videoio.*;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.ImageWindow;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import tellolib.drone.TelloDrone;


public class TelloCamera implements TelloCameraInterface
{
	private final 				Logger logger = Logger.getLogger("Tello");

	private boolean				recording;
	private Thread				videoCaptureThread;
	private VideoCapture		camera;
	private Mat					image;
	private VideoWriter			videoWriter;
	private ImageWindow			imageWindow;
	private Size				videoFrameSize = new Size(480, 360);
	private double				videoFrameRate = 30;
	private SimpleDateFormat	df = new SimpleDateFormat("yyyy-MM-dd.HHmmss");
	private JFrame				jFrame;
	private JLabel				jLabel;
	
	public TelloCamera()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	@Override
	public void startVideoCapture()
	{
		logger.fine("starting video capture");
		
		if (camera != null) return;

		camera = new VideoCapture();
		
	 	camera.setExceptionMode(true);
		
		camera.open("udp://0.0.0.0:" + Integer.toString(TelloDrone.UDP_VIDEO_PORT), Videoio.CAP_FFMPEG);
		
		logger.fine("video camera open:" + camera.isOpened());
		
		image = new Mat();
		
        JFrame jFrame = new JFrame("Tello Controller Test");
        jLabel = new JLabel();
        jLabel.setSize((int) videoFrameSize.width, (int) videoFrameSize.height);
        //frame.getContentPane().add(new JLabel(new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageBytes.toArray())))));
        //jFrame.setSize((int) videoFrameSize.width, (int) videoFrameSize.height);
        jFrame.getContentPane().add(jLabel);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

		videoCaptureThread = new VideoCaptureThread();
		videoCaptureThread.start();
	}
	
	@Override
	public void stopVideoCapture()
	{
		if (recording) stopRecording();
		
		if (videoCaptureThread != null) videoCaptureThread.interrupt();

		logger.fine("stopping video capture thread");
		
		if (jFrame != null) jFrame.dispose();
		
		camera.release();
		image.release();
		image = null;
		camera = null;
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
	    	Mat	image2 = new Mat();
	    	
			logger.fine("video capture thread started");
			
	    	try
	    	{
	    		while (!isInterrupted())
	    		{
	    			camera.read(image);
	    			
	    			if (recording) 
	    			{
	    				Imgproc.resize(image, image2, videoFrameSize);
	    				videoWriter.write(image2);
	    			}
	    		}
	    		
	    		logger.fine("Video capture thread ended");
	    	}
	    	catch (Exception e) { logger.warning("video capture failed: " + e.getMessage()); }
	    	finally {}
	    	
	    	videoCaptureThread =  null;
	    }
	}

	@Override
	public boolean takePicture( String folder )
	{
		String	fileName = null;
		boolean	result = false;
		
		if (camera == null) 
		{
			logger.warning("No video stream");
			return result;
		}
		
		if(image != null && !image.empty())
		{
			// Create and set up the window.
			try
			{
		        // Convert image Mat to a jpeg
		        MatOfByte imageBytes = new MatOfByte();
		        Imgcodecs.imencode(".jpg", image, imageBytes);	        
		        Image img = HighGui.toBufferedImage(image);
		        jLabel.setIcon(new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageBytes.toArray()))));
			}
			catch (Exception e) {logger.warning(e.toString());}
			
			fileName = folder + "\\" + df.format(new Date()) + ".jpg";
			
			if (Imgcodecs.imwrite(fileName, image))
			{
				logger.fine("Picture saved to " + fileName);
				result = true;
			} else
				logger.warning("Picture file save failed");
		} else
			logger.warning("Take Picture failed: image not available");
		
		return result;
	}

	@Override
	public boolean startRecording( String folder )
	{
		boolean		result = false;
		String		fileName;
		
		if (camera == null) 
		{
			logger.warning("No video stream");
			return result;
		}
		
		fileName = folder + "\\" + df.format(new Date()) + ".avi";

		videoWriter = new VideoWriter(fileName, VideoWriter.fourcc('M', 'J', 'P', 'G'), videoFrameRate, 
									  videoFrameSize, true);

		if (videoWriter != null && videoWriter.isOpened())
		{
			recording = result = true;
		
			logger.fine("Video recording started to " + fileName);
		} else
			logger.warning("Video recording failed");
		
		return result;
	}

	@Override
	public void stopRecording()
	{
		if (camera == null || !recording) return;

		recording =  false;

		videoWriter.release();
		
		logger.fine("Video recording stopped");
	}
	
	@Override
	public boolean isRecording()
	{
		return recording;
	}
}
