package tello;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import tello.communication.TelloConnection;
import tello.control.TelloControl;
import tello.control.TelloControlImpl;

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
		
	    TelloControl telloControl = new TelloControlImpl(Level.FINER);

	    try 
	    {
		    //telloControl.connect();
		    //telloControl.enterCommandMode();
		    //telloControl.takeOff();
		    
		    //telloControl.startStatusMonitor();
		    
		    //telloControl.startKeepAlive();
		    
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
		    		leftX = (int) currState.leftStickX * 100;
		    		leftY = (int) currState.leftStickY * 100;
		    		rightX = (int) currState.rightStickX * 100;
		    		rightY = (int) currState.rightStickY * 100;
		    		
		    		telloControl.flyRC(rightX, rightY, leftY, leftX);
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
	    		{telloControl.land();}
	    		catch(Exception e) { e.printStackTrace();}
	    	}
	    }
	    
    	telloControl.disconnect();
	    
	    logger.info("end");

	}
}
