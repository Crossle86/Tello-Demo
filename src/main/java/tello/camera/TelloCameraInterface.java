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

  void takePicture(String folder);
  
  void startRecording(String folder);
  
  void stopRecording();
  
  boolean isRecording();
}
