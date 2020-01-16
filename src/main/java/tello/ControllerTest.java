package tello;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Rect;
import org.opencv.core.Size;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import tellolib.camera.ArucoMarkers;
import tellolib.camera.MissionDetectionCamera;
import tellolib.camera.TelloCamera;
import tellolib.command.TelloFlip;
import tellolib.communication.TelloConnection;
import tellolib.control.TelloControl;
import tellolib.control.TelloControlInterface;

public class ControllerTest
{
	private final Logger 		logger = Logger.getGlobal(); 
	private ControllerManager	controllers;
	private boolean				flying;
	
	public ControllerTest()
	{
		controllers = new ControllerManager();
		controllers.initSDLGamepad();
	}

	public void executeControllerTest()
	{
		int		leftX, leftY, rightX, rightY, deadZone = 10;
		boolean	recording = false, trackArucoMarker = false;
		
		logger.info("start");
		
	    TelloControlInterface telloControl = TelloControl.getInstance();
	    
	    telloControl.setLogLevel(Level.FINE);

	    try 
	    {
		    telloControl.connect();
		    
		    telloControl.enterCommandMode();
		    
		    telloControl.startStatusMonitor();
		    
		    telloControl.streamOn();
		    
		    telloControl.startKeepAlive();
		    
		    telloControl.startVideoCapture(true);
		    
		    telloControl.setMissionMode(true, MissionDetectionCamera.downward);
		    
		    while(true) 
		    {
		    	ControllerState currState = controllers.getState(0);
		    	  
		    	if (!currState.isConnected) 
		    	{
		    		logger.severe("xbox controller not connected");
		    		break;
		    	}
		    	  
		    	if (currState.backJustPressed) 
		    	{
		    		logger.info("back button");
		    		
		    		if (flying)
		    		{
		    			telloControl.land();
		    			flying = false;
		    		}
		    		
		    		break;
		    	}

		    	if (currState.startJustPressed)
		    	{
		    		logger.info("start button");
		    		
		    		if (flying)
		    		{
		    			telloControl.land();
		    			flying = false;
		    		}
		    		else
		    		{
		    			telloControl.takeOff();
		    			flying = true;
		    		}
		    	}
		    	
		    	if (currState.bJustPressed)
		    	{
		    		if (recording)
		    		{
		    			telloControl.stopRecording();
		    			recording = false;
		    		} else
		    			recording = telloControl.startRecording(System.getProperty("user.dir") + "\\Photos");
		    	}
		    	
		    	if  (currState.aJustPressed) telloControl.takePicture(System.getProperty("user.dir") + "\\Photos");
		    	
		    	if (currState.xJustPressed) 
		    	{
	    			telloControl.addTarget(null);
	    			telloControl.setContours(null);
	    			
	    			boolean found = telloControl.detectArucoMarkers();
		    		
		    		logger.info("markers found=" + found);
		    		
		    		if (found)
		    		{
		    			int markerCount = telloControl.getArucoMarkerCount();
		    			
		    			logger.info("marker count=" + markerCount);
		    			
		    			ArrayList<Rect> targets = telloControl.getArucoMarkerTargets();
		    			
		    			telloControl.addTarget(targets.get(0));
		    			
		    			telloControl.setContours(telloControl.getArucoMarkerContours());
		    		}
		    		
		    		//Rect target = new Rect(100,100,200,200);

		    		//telloControl.addTarget(target);
		    	}

		    	if (currState.yJustPressed) 
		    	{
		    		telloControl.resetHeadingZero();
		    		telloControl.resetYawZero();
		    	}
		    	
		    	if (currState.rbJustPressed) trackArucoMarker = !trackArucoMarker;
		    	
		    	if (flying && trackArucoMarker)
		    	{
	    			telloControl.addTarget(null);
	    			telloControl.setContours(null);
	    			
	    			boolean found = telloControl.detectArucoMarkers();
		    		
		    		//logger.info("markers found=" + found);
		    		
		    		if (found)
		    		{
		    			ArrayList<Rect> targets = telloControl.getArucoMarkerTargets();
		    			
		    			Rect target = targets.get(0);
		    			
		    			telloControl.addTarget(target);
		    			
		    			Size imageSize = TelloCamera.getInstance().getImageSize();
		    			
		    			int targetCenterX = target.x + target.width / 2;
		    			int imageCenterX = (int) (imageSize.width / 2);
		    			
		    			int offset = targetCenterX - imageCenterX;

		    			logger.info("offset=" + offset);
		    			
		    			if (Math.abs(offset) < 20) offset = 0;
		    			
		    			int rotate = 5; //(int) (offset * .5);
		    			
		    			if  (offset > 0)
		    				telloControl.rotateRight(rotate);
		    			else if (offset < 0)
		    				telloControl.rotateLeft(rotate);
		    		}
		    	}
	    		
	    		//logger.info("heading=" + telloControl.getHeading() + ";yaw=" + telloControl.getYaw());
		    	
		    	if (flying && !trackArucoMarker)
		    	{
		    		// scale controller stick axis range -1.0 to + 1.0 to -100 to + 100
		    		// used by the drone flyRC command. Apply a dead zone to allow
		    		// for stick axis not always returning 0 when released.
		    		leftX = deadZone((int) (currState.leftStickX * 100.0), deadZone);
		    		leftY = deadZone((int) (currState.leftStickY * 100.0), deadZone);
		    		rightX = deadZone((int) (currState.rightStickX * 100), deadZone);
		    		rightY = deadZone((int) (currState.rightStickY * 100), deadZone);
		    		
		    		//logger.info("lr=" + rightX + " fb=" + rightY + " ud=" + leftY + " yaw=" + leftX);
		    		//                  L/R      F/B    U/D    YAW
	    			telloControl.flyRC(rightX, rightY, leftY, leftX);
		    		
		    		if (currState.dpadUpJustPressed) telloControl.doFlip(TelloFlip.forward);
		    		if (currState.dpadDownJustPressed) telloControl.doFlip(TelloFlip.backward);
		    		if (currState.dpadLeftJustPressed) telloControl.doFlip(TelloFlip.left);
		    		if (currState.dpadRightJustPressed) telloControl.doFlip(TelloFlip.right);
		    		
		    		if (currState.yJustPressed) telloControl.stop();
		    	}
		    	
		    	Thread.sleep(100);
		    }
	    }	
	    catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (telloControl.getConnection() == TelloConnection.CONNECTED)
	    	{
	    		try
	    		{ 
	    			if (flying) telloControl.land(); 
	    		}
	    		catch(Exception e) { e.printStackTrace(); }
	    	}
	    }
	    
    	telloControl.disconnect();
    	
//    	logger.info("mpid=" + telloControl.getMissionPadId() + ";mxyz=" + telloControl.getMissionPadxyz()[0] +
//    			"," + telloControl.getMissionPadxyz()[1] + "," + telloControl.getMissionPadxyz()[2] +
//    			";mpry=" + telloControl.getMissionPadpry()[0] + "," + telloControl.getMissionPadpry()[1] + "," +
//    			telloControl.getMissionPadpry()[2]);
	    
	    logger.info("end");

	}
	
	private int deadZone(int value, int minValue)
	{
		if (Math.abs(value) < minValue) value = 0;
		
		return value;
	}
}
