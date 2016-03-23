package edu.ucla.cs213a.whatwasyourname;

import java.io.ByteArrayInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;

public class Communicator {
	
	public static String sendFaceData(Mat face)
	{
		Mat face2 = new Mat();
		// TODO: Tie in to communication here.
		Bitmap bmp1 = Bitmap.createBitmap((int)face.size().width, (int)face.size().height, Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(face, bmp1);
		Utils.bitmapToMat(bmp1, face2);
		byte[] bytes = edu.ucla.cs213a.whatwasyourname.Utils.bitmapToByteArray(bmp1);
		Log.d(MainConnectActivity.TAG,"Number of bytes Sending: "+ bytes.length);	

//		Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//		boolean success = bmp1.sameAs(bmp2);
//		Mat newMat = new Mat();
//		Utils.bitmapToMat(bmp2, newMat);

		return Communication.sendPicture(bytes);
		
		
	}

}
