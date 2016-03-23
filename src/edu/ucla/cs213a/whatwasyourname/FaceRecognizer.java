package edu.ucla.cs213a.whatwasyourname;

import org.opencv.core.Mat;

public class FaceRecognizer
{
	
	public void train()
	{
		nativeTrain();
	}
	
	public boolean predict(Mat faceImage)
	{
		return nativePredict(faceImage.getNativeObjAddr());
	}
	
	public void setConfidence(double confidence)
	{
		nativeSetConfidence(confidence);
	}
	
	public void setThreshold(double threshold)
	{
		nativeSetThreshold(threshold);
	}
	
	public void setComparison(boolean gt)
	{
		nativeSetComparison(gt);
	}
	
	private static native void nativeTrain();
	private static native boolean nativePredict(long faceImage);
	private static native void nativeSetConfidence(double confidence);
	private static native void nativeSetThreshold(double threshold);
	private static native void nativeSetComparison(boolean gt);

}
