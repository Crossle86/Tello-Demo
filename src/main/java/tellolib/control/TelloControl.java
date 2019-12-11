package tellolib.control;

import tellolib.command.BasicTelloCommand;
import tellolib.command.ComplexTelloCommand;
import tellolib.command.TelloCommandInterface;
import tellolib.command.TelloCommandValues;
import tellolib.command.TelloFlip;
import tellolib.communication.TelloCommunication;
import tellolib.communication.TelloConnection;
import tellolib.drone.TelloDrone;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;

import tello.camera.TelloCamera;

/**
 * Implements TelloControl interface.
 */
public class TelloControl implements TelloControlInterface 
{
	private final 	Logger logger = Logger.getLogger("Tello");
	private final 	ConsoleHandler handler = new ConsoleHandler();
	
	private TelloDrone drone;
	
	private TelloCommunication telloCommunication;
	
	private TelloCamera telloCamera;
	
	private Thread	statusMonitorThread, keepAliveThread;
	
	public TelloControl() 
	{
	  this(Level.OFF);
	}
	
	public TelloControl(Level logLevel) 
	{
	  logger.setLevel(logLevel);
	  handler.setLevel(logLevel);
	  logger.addHandler(handler);
	  logger.setUseParentHandlers(false);
	  
	  drone = new TelloDrone();
	  
	  telloCommunication = new TelloCommunication();
	}
	
	@Override
	public void connect() 
	{
	  telloCommunication.connect();
	  drone.setConnection(TelloConnection.CONNECTED);
	}
	
	@Override
	public void disconnect() 
	{
	  stopStatusMonitor();
	  stopKeepAlive();
	  stopVideoCapture();
	  
	  // This will land if we are still flying and throw away the error
	  // returned by land if we have already landed or never took off.
	  
	  try { land(); } catch (Exception e) {}
	  
	  telloCommunication.disconnect();
	  drone.setConnection(TelloConnection.DISCONNECTED);
	}
	  
	public TelloConnection getConnection() 
	{
	 return drone.getConnection();
	}
	
	@Override
	public void enterCommandMode() 
	{
	  BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.COMMAND_MODE);
	  telloCommunication.executeCommand(command);
	}
	
	@Override
	public void takeOff() 
	{
	  BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.TAKE_OFF);
	  telloCommunication.executeCommand(command);
	}
	
	@Override
	public void land() 
	{
	  BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.LAND);
	  telloCommunication.executeCommand(command);
	}
	
	@Override
	public void doFlip(TelloFlip telloFlip) 
	{
	  ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.FLIP, TelloFlip.toCommand(telloFlip));
	  telloCommunication.executeCommand(command);
	}
	
	@Override
	public void setSpeed(Integer speed) 
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.SPEED, speed.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void forward(Integer distance) 
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.FORWARD, distance.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void backward(Integer distance)
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.BACK, distance.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void right(Integer distance) 
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.RIGHT, distance.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void left(Integer distance) 
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.LEFT, distance.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void rotateRight(Integer angle) 
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.CW, angle.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void rotateLeft(Integer angle)
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.CCW, angle.toString());
		telloCommunication.executeCommand(command);
	}
	  
	public int getBattery() 
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_BATTERY);
		String battery = telloCommunication.executeReadCommand(command);
		drone.setBattery(Integer.parseInt(battery.trim()));
		return drone.getBattery();
	}
	  
	public int getSpeed() 
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_SPEED);
		String speed = telloCommunication.executeReadCommand(command);
		drone.setSpeed((int) Double.parseDouble(speed.trim()));
		return drone.getSpeed();
	}

	@Override
	public void up( Integer distance )
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.UP, distance.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public void down( Integer distance )
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.DOWN, distance.toString());
		telloCommunication.executeCommand(command);
	}
	
	@Override
	public int getTime()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_FLY_TIME);
		String time = telloCommunication.executeReadCommand(command);
		drone.setTime(Integer.parseInt(time.trim().replaceAll("[^\\d.-]", "")));
		return drone.getTime();
	}
	
	@Override
	public int getHeight()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_HEIGHT);
		String height = telloCommunication.executeReadCommand(command);
		drone.setHeight(Integer.parseInt(height.trim().replaceAll("[^\\d.-]", "")) * 10);
		return drone.getHeight();
	}
	
	@Override
	public int getTemp()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_TEMPERATURE);
		String temp = telloCommunication.executeReadCommand(command);
		drone.setTemp(Integer.parseInt(temp.trim().split("~")[0].replaceAll("[^\\d.-]", "")));
		return drone.getTemp();
	}
	
	@Override
	public double getBarometer()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_BAROMETER);
		String barometer = telloCommunication.executeReadCommand(command);
		drone.setBarometer(Double.parseDouble(barometer.trim()));
		return drone.getBarometer();
	}
	
	@Override
	public int[] getAttitude()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_ATTITUDE);
		String attitude = telloCommunication.executeReadCommand(command);
		
		String spry[] = attitude.split(";");
		int pry[] = new int[3];

		for (int i = 0; i < 3; i++)
		{
			String axis[] = spry[i].split(":");
			pry[i] = Integer.parseInt(axis[1]);
			//logger.info(Integer.toString(pry[i]));
 		}

		drone.setAttitude(pry);
		
		return drone.getAttitude();
	}
	
	@Override
	public double[] getAcceleration()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_ACCELERATION);
		String acceleration = telloCommunication.executeReadCommand(command);
		
		String sxyz[] = acceleration.split(";");
		double xyz[] = new double[3];

		for (int i = 0; i < 3; i++)
		{
			String axis[] = sxyz[i].split(":");
			xyz[i] = Double.parseDouble(axis[1]);
			//logger.info(Double.toString(xyz[i]));
 		}

		drone.setAcceleration(xyz);
		return drone.getAcceleration();
	}
	
	@Override
	public double getTof()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.CURRENT_TOF);
		String tof = telloCommunication.executeReadCommand(command);
		drone.setTof(Double.parseDouble(tof.trim().replaceAll("[^\\d.-]", "")) / 10);
		return drone.getTof();
	}

	@Override
	public String getSN()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.SN);
		String sn = telloCommunication.executeReadCommand(command);
		drone.setSN(sn.trim());
		return drone.getSN();
	}

	@Override
	public void goTo( Integer x, Integer y, Integer z, Integer speed )
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.GO, 
				x.toString() + " " + y.toString() + " " + z.toString() + " " + speed.toString());
		telloCommunication.executeCommand(command);
	}

	@Override
	public void flyRC( Integer lr, Integer fb, Integer ud, Integer yaw )
	{
		ComplexTelloCommand command = new ComplexTelloCommand(TelloCommandValues.RC, 
				lr.toString() + " " + fb.toString() + " " + ud.toString() + " " + yaw.toString());
		telloCommunication.executeCommandNoWait(command);
	}

	@Override
	public String getSDK()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.SDK);
		String sdk = telloCommunication.executeReadCommand(command);
		drone.setSDK(sdk.trim());
		return drone.getSDK();
	}

	@Override
	public void stop()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.STOP);
		telloCommunication.executeCommand(command);
	}

	@Override
	public void emergency()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.EMERGENCY);
		telloCommunication.executeCommand(command);
	}

	@Override
	public void streamOn()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.ENABLE_VIDEO_STREAM);
		telloCommunication.executeCommand(command);
	}

	@Override
	public void streamOff()
	{
		BasicTelloCommand command = new BasicTelloCommand(TelloCommandValues.DISABLE_VIDEO_STREAM);
		telloCommunication.executeCommand(command);
	}

	@Override
	public void startStatusMonitor()
	{
		logger.fine("starting status monitor thread");
		
		if (statusMonitorThread != null) return;

		statusMonitorThread = new StatusMonitor();
		statusMonitorThread.start();
	}

	@Override
	public void stopStatusMonitor()
	{
		if (statusMonitorThread != null) statusMonitorThread.interrupt();

		logger.fine("stopping status monitor thread");
		
		statusMonitorThread = null;
	}
	
	private class StatusMonitor extends Thread
	{
		StatusMonitor()
		{
			logger.fine("monitor thread constructor");
			
			this.setName("StatusMonitor");
	    }
		
	    public void run()
	    {
			logger.fine("monitor thread start");
			
	    	try
	    	{
	    		while (!isInterrupted())
	    		{
	    			String logData = telloCommunication.receiveStatusData();
	    			
	    			logger.finer(logData);
	    			
	    			String[] keyValuePairs = logData.split(";"); 
	    			
	    			for(String pair : keyValuePairs)                        // iterate over the pairs.
	    			{
	    				int[] attpry = new int[3];
	    				double[] accelxyz = new double[3], veloxyz = new double[3];
	    				
	    			    String[] entry = pair.split(":");                   // split the pairs to get key and value. 
	    			    
	    			    switch (entry[0])
	    			    {
	    			    	case "bat": drone.setBattery(Integer.parseInt(entry[1].trim())); break;
	    			    	case "time": drone.setTime(Integer.parseInt(entry[1].trim())); break;
	    			    	case "temph": drone.setTemp(Integer.parseInt(entry[1].trim())); break;
	    			    	case "tof": drone.setTof(Integer.parseInt(entry[1].trim())); break;
	    			    	case "h": drone.setHeight(Integer.parseInt(entry[1].trim())); break;
	    			    	case "baro": drone.setBarometer(Double.parseDouble(entry[1].trim())); break;
	    			    	case "pitch": attpry[0] = Integer.parseInt(entry[1].trim()); break;
	    			    	case "roll": attpry[1] = Integer.parseInt(entry[1].trim()); break;
	    			    	case "yaw": attpry[2] = Integer.parseInt(entry[1].trim()); break;
	    			    	case "agx": accelxyz[0] = Double.parseDouble(entry[1].trim()); break;
	    			    	case "agy": accelxyz[1] = Double.parseDouble(entry[1].trim()); break;
	    			    	case "agz": accelxyz[2] = Double.parseDouble(entry[1].trim()); break;
	    			    	case "vgx": veloxyz[0] = Double.parseDouble(entry[1].trim()); break;
	    			    	case "vgy": veloxyz[1] = Double.parseDouble(entry[1].trim()); break;
	    			    	case "vgz": veloxyz[2] = Double.parseDouble(entry[1].trim()); break;
	    			    }
	    			    
	    			    drone.setAttitude(attpry);
	    			    
	    			    drone.setAcceleration(accelxyz);
	    			    
	    			    drone.setVelocity(veloxyz);
	    			}
	    		}
	    	}
	    	catch (Exception e) { logger.warning("status monitor failed: " + e.getMessage()); }
	    	finally {}
	    	
	    	statusMonitorThread =  null;
	    }
	}

	@Override
	public void startKeepAlive()
	{
		logger.fine("starting keepalive thread");
		
		if (keepAliveThread != null) return;

		keepAliveThread = new KeepAlive();
		keepAliveThread.start();
	}

	@Override
	public void stopKeepAlive()
	{
		if (keepAliveThread != null) keepAliveThread.interrupt();

		logger.fine("stopping keepalive thread");
		
		keepAliveThread = null;
	}
	
	private class KeepAlive extends Thread
	{
		KeepAlive()
		{
			logger.fine("KeepAlive thread constructor");
			
			this.setName("KeepAlive");
	    }
		
	    public void run()
	    {
			logger.fine("keepalive thread start");
			
	    	try
	    	{
	    		while (!isInterrupted())
	    		{
	    			sleep(10000);	// 10 seconds.
	    			
	    			getBattery();
	    		}
	    	}
	    	catch (InterruptedException e) {}
	    	catch (Exception e) { logger.warning("keepalive failed: " + e.getMessage()); }
	    	
	    	keepAliveThread =  null;
	    }
	}

	@Override
	public TelloDrone getDrone()
	{
		return drone;
	}

	@Override
	public void startVideoCapture(boolean liveWindow)
	{
		if (telloCamera != null) stopVideoCapture();
		
		telloCamera = new TelloCamera();
		
		telloCamera.startVideoCapture(liveWindow);
	}

	@Override
	public void stopVideoCapture()
	{
		if (telloCamera != null) telloCamera.stopVideoCapture();
	}

	@Override
	public boolean takePicture( String folder )
	{
		if (telloCamera != null) return telloCamera.takePicture(folder);
		
		return false;
	}

	@Override
	public boolean startRecording( String folder )
	{
		if (telloCamera != null)  return telloCamera.startRecording(folder);
		
		return false;
	}

	@Override
	public void stopRecording()
	{
		if (telloCamera != null) telloCamera.stopRecording();
	}

	@Override
	public boolean isRecording()
	{
		if (telloCamera != null) 
			return telloCamera.isRecording();
		else
			return false;
	}

	@Override
	public Mat getImage()
	{
		if (telloCamera != null) 
			return telloCamera.getImage();
		else
			return null;
	}
}
