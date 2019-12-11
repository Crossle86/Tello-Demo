package tello.camera;

public interface TelloCameraInterface
{
  /**
   * Start capture of video stream from drone for processing by
   * this program.
   */
  void startVideoCapture();
  
  /**
   * Stop video stream capture thread.
   */
  void stopVideoCapture();

  boolean takePicture(String folder);
  
  boolean startRecording(String folder);
  
  void stopRecording();
  
  boolean isRecording();
}
