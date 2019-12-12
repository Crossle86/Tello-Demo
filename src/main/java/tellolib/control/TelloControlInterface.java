package tellolib.control;

import java.util.logging.Level;

import org.opencv.core.Mat;

import tellolib.command.MissionDetectionCamera;
import tellolib.command.TelloFlip;
import tellolib.communication.TelloConnection;
import tellolib.drone.TelloDroneInterface;
import tellolib.exception.TelloCommandException;

/**
 * Higher level interface to Tello Drone library.
 */
public interface TelloControlInterface 
{
  /**
   * set logging level. Defaults to OFF.
   * @param logLevel Desired log level.
   */
  void setLogLevel(Level logLevel);
  
  /**
   * Establishes connection to the Tello Drone.
   */
  void connect();

  /**
   * Disconnecting from the drone. If the drone is not landed yet, it will start an automatic
   * landing.
   */
  void disconnect();


  /**
   * Enter command mode. You can only execute commands after this call.
   */
  void enterCommandMode();

  /**
   * Taking off from the ground.
   */
  void takeOff();

  /**
   * Landing on the ground.
   */
  void land();

  /**
   * Doing a flip in the chosen direction.
   *
   * @param telloFlip Type of the flip.
   */
  void doFlip(TelloFlip telloFlip);

  /**
   * Set the drone's speed.
   *
   * @param speed Chosen speed (10-100 cm/s).
   */
  void setSpeed(Integer speed);

  /**
   * Move forward.
   * @param distance (20-500 cm).
   */
  void forward(Integer distance);

  /**
   * Move backward.
   * @param distance (20-500 cm).
   */
  void backward(Integer distance);

  /**
   * Move right.
   * @param distance (20-500 cm).
   */

  void right(Integer distance);

  /**
   * Move left.
   * @param distance (20-500 cm).
   */

  void left(Integer distance);

  /**
   * Move up.
   * @param distance (20-500 cm).
   */

  void up(Integer distance);

  /**
   * Move down.
   * @param distance (20-500 cm).
   */

  void down(Integer distance);

  /**
   * rotate right.
   * @param angle (0-3600 deg).
   */
  void rotateRight(Integer angle);

  /**
   * rotate left.
   * @param angle (0-3600 deg).
   */
  void rotateLeft(Integer angle);
  
  /**
   * Fly to these offsets from current position.
   * @param x X axis offset (20-500 cm).
   * @param y Y axis offset (20-500 cm).
   * @param z Z axis offset (20-500 cm).
   * @param speed Speed of movement (10-100 cm/s).
   */
  void goTo(Integer x, Integer y, Integer z, Integer speed);
  
  /**
   * Fly by remote control.
   * @param lr Left/Right (-100 to 100).
   * @param fb forward/backward (-100 to 100).
   * @param ud up/down (-100 to 100).
   * @param yaw
   */
  void flyRC(Integer lr, Integer fb, Integer ud, Integer yaw);

  /**
   * Get current battery level.
   * @return Battery level %.
   */
  int getBattery();
  
  /**
   * Get current speed setting.
   * @return Speed (1-100 cm/s).
   */
  int getSpeed();
  
  /**
   * Get flight time.
   * @return Flight time in seconds.
   */
  int getTime();
  
  /**
   * Get drone height.
   * @return Height (0-3000 cm).
   */
  int getHeight();
  
  /**
   * Get drone temperature.
   * @return Temperature in degrees C (0-90).
   */
  int getTemp();
  
  /**
   * Get barometric pressure.
   * @return Pressure in millibars.	
   */
  double getBarometer();
  
  /**
   * Get IMU attitude data.
   * @return Pitch, roll, yaw.
   */
  int[] getAttitude();
  
  /**
   * Get IMU acceleration.
   * @return Angular acceleration x, y, z (.001 g).
   */
  double[] getAcceleration();
  
  /**
   * Get distance from TOF.
   * @return Distance (30-1000 cm).
   */
  double getTof();
  
  /**
   * Get drone serial number. Requires EDU with SDK 1.3 or later.
   * @return Serial Number.
   */
  String getSN();

  /**
   * Get drone connection status.
   * @return Connection status.
   */
  TelloConnection getConnection();
  
  /**
   * Get drone sdk version. Requires EDU with SDK 1.3 or later.
   * @return Version.
   */
  String getSDK();
  
  /**
   * Stop drone motion, goes into hover.
   */
  void stop();
  
  /**
   * Stop all motors.
   */
  void emergency();
  
  /**
   * Turn on video stream.
   */
  void streamOn();
  
  /**
   * Turn off video stream.
   */
  void streamOff();
  
  /**
   * Start monitoring the status updates sent by the Tello.
   * Requires SDK 1.3 or later.
   */
  void startStatusMonitor();
  
  /**
   * Stop monitoring the status updates sent by the Tello.
   * Requires SDK 1.3 or later. Monitor will be stopped when
   * disconnect() is called.
   */
  void stopStatusMonitor();
  
  /**
   * Start keep alive thread that pings the Tello every 10 seconds
   * with a get battery level command to keep the Tello from shutting
   * down automatically if it receives no commands for 15 seconds.
   */
  void startKeepAlive();
  
  /**
   * Stops the keep alive thread. Thread will be stopped when disconnect()
   * is called.
   */
  void stopKeepAlive();
  
  /**
   * Return the TelloDrone instance maintained by TelloControl.
   * @return The drone instance.
   */
  TelloDroneInterface getDrone();
  
  /**
   * Start capture of video stream from drone for processing by
   * this program. This function may take several seconds to complete.
   * @param liveWindow True to display video feed in a live window.
   */
  void startVideoCapture(boolean liveWindow);
  
  /**
   * Stop video stream capture thread.
   */
  void stopVideoCapture();
  
  /**
   * Save the current image from the video feed to a file in
   * the named folder.
   * @param folder Location to save image.
   * @return True if image saved, false if failed.
   */
  boolean takePicture(String folder);
  
  /**
   * Turn on recording of the video feed to an .avi file in
   * the named folder.
   * @param folder Location to save the video file.
   * @return True if recording started, false if failed.
   */
  boolean startRecording(String folder);
  
  /**
   * Stop recording the video feed.
   */
  void stopRecording();
  
  /**
   * Returns recording state.
   * @return True if recording in progress, false if not.
   */
  boolean isRecording();

  /**
   * Returns the current image from the video feed.
   * @return The current image.
   */
  Mat getImage();

  /**
   * Set mission mode state.
   * @param enabled True to enable, false to disable.
   * @param camera Select which camera for detection when enabled.
  */
  void setMissionMode(boolean enabled, MissionDetectionCamera camera);
  
  /**
   * Returns mission mode state.
   * @return True if enabled, false if disabled.
   */
  boolean isMissionModeEnabled();
}