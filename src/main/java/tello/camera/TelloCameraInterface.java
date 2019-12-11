package tello.camera;

import org.opencv.core.Mat;

public interface TelloCameraInterface
{
  /**
   * Start capture of video stream from drone for processing by
   * this program.
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
}
