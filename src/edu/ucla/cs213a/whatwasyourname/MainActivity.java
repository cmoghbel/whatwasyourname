/*
 * This class is derived and modified from OpenCV Face Detection sample, available via: http://opencv.org/android
 */
package edu.ucla.cs213a.whatwasyourname;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import edu.ucla.cs213a.whatwasyourname.DetectionBasedTracker;
import edu.ucla.cs213a.whatwasyourname.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements CvCameraViewListener {

	private static final String    TAG                 = "FaceDetection";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private MenuItem               mItemFace50;
    private MenuItem               mItemFace40;
    private MenuItem               mItemFace30;
    private MenuItem               mItemFace20;
    private MenuItem			   mItemTrain;
    private MenuItem               mItemType;

    private Mat                    mRgba;
    private Mat                    mGray;
    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private CameraBridgeViewBase   mOpenCvCameraView;
    
    private static final String SUBJECT_1_LABEL = "Chris Moghbel";
    private static final double SUBJECT_1_THRESHOLD = 300.0;
    private static final double SUBJECT_1_CONFIDENCE = 130.0;
    private static final String SUBJECT_2_LABEL = "Face 1";
    private static final double SUBJECT_2_THRESHOLD = 300.0;
    private static final double SUBJECT_2_CONFIDENCE = 51.0;
    public static edu.ucla.cs213a.whatwasyourname.FaceRecognizer mFaceRecognizer = new FaceRecognizer();
    public static boolean subject1 = false;
    public static String subject_label = SUBJECT_1_LABEL;
    private boolean mTrain = false;
    private Mat[] mMatArray = new Mat[5];
    private int mTrainCounter = 0;
    private File mFilesDir;
    private int mNumTrainingImages = 6;
    private int counter = 0;
    private boolean local = false;	

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();
                        
                        // Load images to files dir for trainer to access.
                        Context context = getApplicationContext();
                        mFilesDir = context.getFilesDir();
                        int[] images = null;
                        if (subject1)
                        {
                        	images = new int[] {R.raw.chris1, R.raw.chris2, R.raw.chris3, R.raw.chris4, R.raw.chris5, R.raw.chris6, R.raw.chris7, R.raw.chris8, R.raw.chris9, R.raw.chris10, R.raw.chris11, R.raw.chris12, R.raw.chris13, R.raw.chris14};
//                        	images = new int[] {R.raw.chris1p, R.raw.chris6p, R.raw.chris8p, R.raw.chris15p, R.raw.chris16p, R.raw.chris17p, R.raw.chris18p, R.raw.chris19p, R.raw.chris20p};
                        	subject_label = SUBJECT_1_LABEL;
                        	mFaceRecognizer.setConfidence(SUBJECT_1_CONFIDENCE);
                        	mFaceRecognizer.setThreshold(SUBJECT_1_THRESHOLD);
                        	mFaceRecognizer.setComparison(false);
                        }
                        else
                        {
                        	images = new int[] {R.raw.sample1, R.raw.sample2, R.raw.sample3, R.raw.sample4, R.raw.sample5, R.raw.sample6, R.raw.sample7, R.raw.sample9, R.raw.sample10};
                        	subject_label = SUBJECT_2_LABEL;
                        	mFaceRecognizer.setConfidence(SUBJECT_2_CONFIDENCE);
                        	mFaceRecognizer.setThreshold(SUBJECT_2_THRESHOLD);
                        	mFaceRecognizer.setComparison(false);
                        } 

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);
                        

                        cascadeDir.delete();
                        
                        for (int i = 0; i < images.length; ++i)
                        {
                        	int image = images[i];
                	        try
                	        {
                		        is = context.getResources().openRawResource(image);
                		        File imageFile = null;
                		        if (subject1)
                		        {
                		        	imageFile = new File(mFilesDir, "/chris" + (i + 1) + ".jpg");
                		        }
                		        else
                		        {
                		        	imageFile = new File(mFilesDir, "sample" + (i + 1) + ".pgm");
                		        }
                		        String filepath = imageFile.getAbsolutePath();
                		        Log.w(TAG, "Writing to files dir: " + imageFile.getAbsolutePath());
                		        os = new FileOutputStream(imageFile);
                		
            		        	buffer = new byte[4096];
            		        	bytesRead = 0;
            		        	while ((bytesRead = is.read(buffer)) != -1) {
            		        		os.write(buffer, 0, bytesRead);
            		        	}
            		        	is.close();
            		        	os.close();
            		        
                		        MatOfRect faces = new MatOfRect();
                		        Log.w(TAG, "Trying to read in: " + filepath);
                		        Mat imageGray = Highgui.imread(filepath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
                		        if (imageGray.empty()) Log.w(TAG, "Image gray empty!!");
                		        else Log.w(TAG, imageGray.size().height + "," + imageGray.size().width);
                		        
                		        setMinFaceSize(mRelativeFaceSize);
                		        if (mAbsoluteFaceSize == 0)
                		        {
                                    int height = imageGray.rows();
                                    if (Math.round(height * mRelativeFaceSize) > 0) {
                                        mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                                    }
                                    mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
                                }
                		        
//                		        mNativeDetector.detect(imageGray, faces);
                		        mJavaDetector.detectMultiScale(imageGray, faces, 1.1, 2, 2,
                                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
                		        
                		        Rect[] faceArray = faces.toArray();
                		        Log.w(TAG, "Num faces detected in image: " + faceArray.length);
                		        if (faceArray.length > 0)
                                {
                		        	Rect face = faceArray[0];
                		        	if (face.x + face.size().width > imageGray.size().width
                		        		|| face.y + face.size().height > imageGray.size().height
                		        		|| face.x < 0
                		        		|| face.y < 0)
                		        	{
                		        		Log.e(TAG, "Weird face detected that doesn't fit within picture!");
                		        	}
                		        	else
                		        	{
                		        		Log.w(TAG, "Face Size: " + face.size().width + ", " + face.size().height);
                		        		Log.w(TAG, "X: " + face.x + " Y: " + face.y);
                		        		Mat trainingImage = imageGray.submat(faceArray[0]);
                		        		Log.w(TAG, "Writing to files dir: " + mFilesDir + "/train" + (i) + ".pgm");
                		        		Highgui.imwrite(mFilesDir.getAbsolutePath() + "/train" + i + ".pgm", trainingImage);
                		        	}
                                }
                	        }
                	        catch (FileNotFoundException ex) {}
                	        catch (IOException ex) {}
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(Mat inputFrame) {

        inputFrame.copyTo(mRgba);
        Imgproc.cvtColor(inputFrame, mGray, Imgproc.COLOR_RGBA2GRAY);

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null)
                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null)
                mNativeDetector.detect(mGray, faces);
        }
        else {
            Log.e(TAG, "Detection method is not selected!");
        }
        
        Core.putText(mRgba, "Connected: " + Communication.PeersIP.size(), new Point(5.0, 10.0), Core.FONT_HERSHEY_COMPLEX, 0.4, FACE_RECT_COLOR);

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
        {
        	Rect face = facesArray[i];
            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
            
            if (counter == 0)
            {
            	Point labelPoint = facesArray[i].tl();
            	labelPoint.y = labelPoint.y - 5.0;
            	if (!local)
            	{
	            	String label = Communicator.sendFaceData(mGray.submat(face));
	            	if (label != null)
	            	{
	            		Core.putText(mRgba, label, labelPoint, Core.FONT_HERSHEY_PLAIN, 1.0, FACE_RECT_COLOR);
	            	}
            	}
            	else
            	{	
	                if (mFaceRecognizer.predict(mGray.submat(face)))
	                {
	                	Core.putText(mRgba, subject_label, labelPoint, Core.FONT_HERSHEY_PLAIN, 1.0, FACE_RECT_COLOR);
	                }
	                else
	                {
	                	Core.putText(mRgba, "Unknown", labelPoint, Core.FONT_HERSHEY_PLAIN, 1.0, FACE_RECT_COLOR);
	                }
            	}
            }
            counter += 1;
            if (counter < 4) counter = 0;
        }
        return mRgba;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w(TAG, "called onCreateOptionsMenu");
//        mItemFace50 = menu.add("Face size 50%");
        mItemTrain = menu.add("Train");
        mItemFace40 = menu.add("Face size 40%");
        mItemFace30 = menu.add("Face size 30%");
        mItemFace20 = menu.add("Face size 20%");
        mItemType   = menu.add(mDetectorName[mDetectorType]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.w(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemTrain)
        {
        	Log.w(TAG, "telling face recognizer to train...");
            mFaceRecognizer.train();
        }
        else if (item == mItemFace40)
            setMinFaceSize(0.4f);
        else if (item == mItemFace30)
            setMinFaceSize(0.3f);
        else if (item == mItemFace20)
            setMinFaceSize(0.2f);
        else if (item == mItemType) {
            mDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[mDetectorType]);
            setDetectorType(mDetectorType);
        }
        return true;
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }
}
