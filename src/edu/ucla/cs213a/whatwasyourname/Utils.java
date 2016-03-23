package edu.ucla.cs213a.whatwasyourname;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class Utils
{
	
	public static byte[] bitmapToByteArray(Bitmap bmp)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 0, os);
		return os.toByteArray();
	}

}
