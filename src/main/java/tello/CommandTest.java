package tello;

import static java.lang.Thread.sleep;

import java.util.logging.Logger;

import tello.command.TelloFlip;
import tello.communication.TelloConnection;
import tello.control.TelloControl;
import tello.control.TelloControlImpl;

public class CommandTest
{
	private static final Logger logger = Logger.getGlobal(); //.getAnonymousLogger(); //Main.class.getName());

	public void executeCommandTest()
	{
		logger.info("start");
		
	    TelloControl telloControl = new TelloControlImpl();

	    try 
	    {
		    telloControl.connect();
		    telloControl.enterCommandMode();
		    telloControl.takeOff();
		
		    try 
		    {
		      sleep(3000);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		    
		    telloControl.doFlip(TelloFlip.forward);
		    
		    int battery, speed, time, height, temp;
		    
		    double baro, tof;
		    
		    String sn, sdk;
		    
		    int[] attitude, acceleration;
		    
			battery = telloControl.getBattery();
			
			speed = telloControl.getSpeed();
			
			time = telloControl.getTime();
			
			baro = telloControl.getBarometer();
			
			height = telloControl.getHeight();
			
			tof = telloControl.getTof();
			
			temp = telloControl.getTemp();
			
			sn = telloControl.getSN();
			
			sdk = telloControl.getSDK();
			
			attitude = telloControl.getAttitude();
			
			acceleration = telloControl.getAcceleration();
			    
		    logger.info("battery level=" + battery + ";speed=" + speed + ";time=" + time);
		    logger.info("baro=" + baro + ";height=" + height + ";tof=" + tof + ";temp=" + temp);
		    logger.info("sdk=" + sdk + ";sn=" + sn + ";att=" + attitude + ";accel=" + acceleration);
		    
		    telloControl.forward(100);
		    
		    telloControl.backward(100);
		    
		    telloControl.up(100);
		    
		    telloControl.down(100);
		    
		    telloControl.left(100);
		    
		    telloControl.right(100);
		    
		    telloControl.rotateLeft(90);
		    
		    telloControl.rotateRight(90);
		    
		    telloControl.goTo(20, 20, 20, 50);
	    }	
	    catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (telloControl.getConnection() == TelloConnection.CONNECTED) telloControl.land();
	    
	    	telloControl.disconnect();
	    }
	    
	    logger.info("end");
	}
}
