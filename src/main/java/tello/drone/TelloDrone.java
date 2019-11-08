package tello.drone;

import tello.communication.TelloConnection;

/**
 * Implements the Tello Drone Interface.
 */
public class TelloDrone implements TelloDroneInterface
{
  /*
   * Connection IP address.
   */
  public static final String IP_ADDRESS = "192.168.10.1";

  /*
   * Connection UDP Port.
   */
  public static final Integer UDP_PORT = 8889, UDP_STATUS_PORT = 8890;
  
  private int				battery, height, speed, time, temp, attitude[];
  private double			barometer, tof, acceleration[], velocity[];
  private String			sn, sdk;
  private TelloConnection 	telloConnection;
  private TelloMode 		telloMode;

  public TelloDrone() 
  {
    telloConnection = TelloConnection.DISCONNECTED;
    telloMode = TelloMode.NORMAL;
  }

  @Override
  public Integer getBattery() 
  {
    return battery;
  }

  @Override
  public void setBattery(Integer battery) 
  {
    this.battery = battery;
  }

  @Override
  public Integer getSpeed() 
  {
    return speed;
  }

  @Override
  public void setSpeed(Integer speed) 
  {
    this.speed = speed;
  }

  @Override
  public Integer getTime() 
  {
    return time;
  }

  @Override
  public void setTime(Integer time) 
  {
    this.time = time;
  }

  @Override
  public TelloConnection getConnection() 
  {
    return telloConnection;
  }

  @Override
  public void setConnection(TelloConnection telloConnection) 
  {
    this.telloConnection = telloConnection;
  }

  @Override
  public TelloMode getMode() 
  {
    return telloMode;
  }

  @Override
  public void setMode(TelloMode telloMode) 
  {
    this.telloMode = telloMode;
  }

  @Override
  public void setTemp( int temp )
  {
	this.temp = temp;
  }

  @Override
  public int getTemp()
  {
	return temp;
  }

  @Override
  public void setBarometer( double barometer )
  {
	this.barometer = barometer;
  }

  @Override
  public double getBarometer()
  {
	return barometer;
  }

  @Override
  public void setTof( double tof )
  {
	this.tof = tof;
  }

  @Override
  public double getTof()
  {
	return tof;
  }

  @Override
  public void setSN( String sn )
  {
	this.sn = sn;
  }

  @Override
  public String getSN()
  {
	return sn;
  }

  @Override
  public void setHeight( int height )
  {
	this.height = height;
  }

  @Override
  public int getHeight()
  {
	return height;
  }

  @Override
  public void setAttitude( int[] pry )
  {
	attitude  = pry;
  }

  @Override
  public int[] getAttitude()
  {
	return attitude;
  }

  @Override
  public void setAcceleration( double[] xyz )
  {
	acceleration = xyz;
  }

  @Override
  public double[] getAcceleration()
  {
	return acceleration;
  }

  @Override
  public void setSDK( String sdk )
  {
	this.sdk = sdk;
  }

  @Override
  public String getSDK()
  {
	return sdk;
  }

  @Override
  public void setVelocity( double[] xyz )
  {
    this.velocity = xyz;
  }

  @Override
  public double[] getVelocity()
  {
	return velocity;
  }
}