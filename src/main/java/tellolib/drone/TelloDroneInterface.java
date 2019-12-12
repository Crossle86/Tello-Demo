package tellolib.drone;

import tellolib.communication.TelloConnection;

/**
 * Represents the Tello Drone.
 */
public interface TelloDroneInterface 
{
	/** 
	 * Return last recorded battery level
	 * @return Battery level 0-100%
	 */
	Integer getBattery();

	/**
	 * Record battery level.
	 * @param battery Battery level to record 0-100%
	 */
	void setBattery(Integer battery);

	/**
	 * Return last set speed value 0-100 cm/s.
	 * @return
	 */
	Integer getSpeed();

	/**
	 * Record speed 10-100 cm/s.
	 * @param speed
	 */
	void setSpeed(Integer speed);

	/**
	 * Get current flight time.
	 * @return Flight time in seconds.
	 */
	Integer getTime();

	/**
	 * Set flight time.
	 * @param time Flight time in seconds.
	 */
	void setTime(Integer time);

	/**
	 * Get connection status.
	 * @return Connection status enum.
	 */
	TelloConnection getConnection();

	/**
	 * Record connection status.
	 * @param telloConnection The connection status value.
	 */
	void setConnection(TelloConnection telloConnection);

	/**
	 * Get last recorded operation mode.
	 * @return The current operation mode.
	 */
	TelloMode getMode();

	/**
	 * Record drone operation mode.
	 * @param telloMode Current operation mode value.
	 */
	void setMode(TelloMode telloMode);

	/**
	 * Record the current temperature.
	 * @param temp Temperature in degrees C.
	 */
	void setTemp( int temp );

	/**
	 * Get last recorded temperature.
	 * @return Temperature in degrees C.
	 */
	int getTemp();

	/**
	 * Record barometric pressure.
	 * @param pressure Pressure in millibars.
	 */
	void setBarometer( double pressure );

	/**
	 * Get the last recorded barometric pressure.
	 * @return Pressure in millibars.
	 */
	double getBarometer();

	/**
	 * Record TOF value.
	 * @param tof
	 */
	void setTof( double tof );

	/**
	 * Get the last recorded TOF value.
	 * @return TOF value.
	 */
  	double getTof();

  	/**
  	 * Record drone serial number.
  	 * @param sn Serial number.
  	 */
  	void setSN( String sn );

	/**
	 * Get the last recorded serial number.
	 * @return Serial number.
	 */
  	String getSN();

  	/**
  	 * Record drone height.
  	 * @param height Height in cm.
  	 */
  	void setHeight( int height );

	/**
	 * Get the last recorded height.
	 * @return Height in cm..
	 */
  	int getHeight();

  	/**
  	 * Record drone attitude.
  	 * @param pry Attitude vectors pitch, roll, yaw in degrees.
  	 */
  	void setAttitude( int[] pry );

	/**
	 * Get the last recorded attitude vectors.
	 * @return Attitude vectors pitch, roll, yaw in degrees.
	 */
  	int[] getAttitude();

  	/**
  	 * Record drone acceleration vectors.
  	 * @param xyz Acceleration vectors x,y,z in cm/s/s.
  	 */
  	void setAcceleration( double[] xyz );

	/**
	 * Get the last recorded acceleration vectors.
	 * @return Acceleration vectors x,y,z in cm/s/s.
	 */
  	double[] getAcceleration();

  	/**
  	 * Record drone velocity vectors.
  	 * @param xyz Velocity vectors x,y,z in cm/s.
  	 */
  	void setVelocity( double[] xyz );

	/**
	 * Get the last recorded velocity vectors.
	 * @return Velocity vectors x,y,z in cm/s.
	 */
  	double[] getVelocity();

  	/**
  	 * Record the drone sdk version.
  	 * @param version SDK version.
  	 */
  	void setSDK( String version );

	/**
	 * Get the last recorded SDK version.
	 * @return The SDK version.
	 */
  	String getSDK();
  	
  	/**
  	 * Record mission mode state.
  	 * @param enabled True if mission mode enabled, false if not.
  	 */
  	void setMissionMode(boolean enabled);
  	
  	/**
  	 * Get the last recorded mission mode state.
  	 * @return True if mission mode is enabled, false if not.
  	 */
  	boolean isMissionModeEnabled();
}
