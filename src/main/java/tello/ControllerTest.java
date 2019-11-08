package tello;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import tello.command.TelloFlip;
import tello.communication.TelloConnection;
import tello.control.TelloControlInterface;
import tello.control.TelloControl;

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
		
		logger.info("start");
		
	    TelloControlInterface telloControl = new TelloControl(Level.FINE);

	    try 
	    {
		    telloControl.connect();
		    telloControl.enterCommandMode();
		    
		    telloControl.startStatusMonitor();
		    
		    telloControl.startKeepAlive();
		    
		    while(true) 
		    {
		    	ControllerState currState = controllers.getState(0);
		    	  
		    	if (!currState.isConnected) 
		    	{
		    		logger.severe("xbox controller not connected");
		    		break;
		    	}
		    	  
		    	if (currState.back) 
		    	{
		    		logger.info("back button");
		    		telloControl.land();
		    		flying = false;
		    		break;
		    	}

		    	if (currState.startJustPressed)
		    	{
		    		logger.info("start button");
		    		telloControl.takeOff();
		    		flying = true;
		    	}
		    	
		    	if (flying)
		    	{
		    		// scale controller stick range -1.0 to + 1.0 to -100 to + 100
		    		// used by the drone flyRC command.
		    		leftX = deadZone((int) (currState.leftStickX * 100.0), 3);
		    		leftY = deadZone((int) (currState.leftStickY * 100.0), 3);
		    		rightX = deadZone((int) (currState.rightStickX * 100), 3);
		    		rightY = deadZone((int) (currState.rightStickY * 100), 3);
		    		
		    		logger.info(rightX + " " + rightY + " " + leftY + " " + leftX);
		    		
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
	    } finally 
	    {
	    	if (telloControl.getConnection() == TelloConnection.CONNECTED)
	    	{
	    		try
	    		{ 
	    			if (flying) telloControl.land(); 
	    		}
	    		catch(Exception e) { e.printStackTrace();}
	    	}
	    }
	    
    	telloControl.disconnect();
	    
	    logger.info("end");

	}
	
	private int deadZone(int value, int minValue)
	{
		if (Math.abs(value) < minValue) value = 0;
		
		return value;
	}
}
