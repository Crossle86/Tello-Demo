package tello;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import tellolib.camera.MissionDetectionCamera;
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
		int		leftX, leftY, rightX, rightY;
		boolean	recording = false;
		
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
		    		boolean found = telloControl.detectArucoMarkers();
		    		
		    		logger.info("markers found=" + found);
		    		
		    		if (found)
		    		{
		    			int markers = 0;
		    		}
		    	}

		    	if (currState.yJustPressed) 
		    	{
		    		telloControl.resetHeadingZero();
		    		telloControl.resetYawZero();
		    	}
	    		
	    		//logger.info("heading=" + telloControl.getHeading() + ";yaw=" + telloControl.getYaw());
		    	
		    	if (flying)
		    	{
		    		// scale controller stick axis range -1.0 to + 1.0 to -100 to + 100
		    		// used by the drone flyRC command. Apply a dead zone to allow
		    		// for stick axis not always returning 0 when released.
		    		leftX = deadZone((int) (currState.leftStickX * 100.0), 3);
		    		leftY = deadZone((int) (currState.leftStickY * 100.0), 3);
		    		rightX = deadZone((int) (currState.rightStickX * 100), 3);
		    		rightY = deadZone((int) (currState.rightStickY * 100), 3);
		    		
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
