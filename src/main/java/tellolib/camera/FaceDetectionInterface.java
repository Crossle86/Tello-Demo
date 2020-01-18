package tellolib.camera;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Face detection with OpenCV.
 */
public interface FaceDetectionInterface
{
	public boolean detectFaces(Mat frame);

	public boolean detectFaces();
	
	public int getFaceCount();
	
	public Rect[] getFaces();

}
