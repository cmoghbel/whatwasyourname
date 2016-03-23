/*
 * This class is used as is from OpenCV Face Detection Sample, available via: http://opencv.org/android
 */
package edu.ucla.cs213a.whatwasyourname;

import java.text.DecimalFormat;

import org.opencv.core.Core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class FpsMeter {
	private static final String TAG       = "OCVSample::FpsMeter";
    int                         step;
    int                         framesCouner;
    double                      freq;
    long                        prevFrameTime;
    String                      strfps;
    DecimalFormat               twoPlaces = new DecimalFormat("0.00");
    Paint                       paint;

    public void init() {
        step = 20;
        framesCouner = 0;
        freq = Core.getTickFrequency();
        prevFrameTime = Core.getTickCount();
        strfps = "";

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(50);
    }

    public void measure() {
        framesCouner++;
        if (framesCouner % step == 0) {
            long time = Core.getTickCount();
            double fps = step * freq / (time - prevFrameTime);
            prevFrameTime = time;
            DecimalFormat twoPlaces = new DecimalFormat("0.00");
            strfps = twoPlaces.format(fps) + " FPS";
            Log.i(TAG, strfps);
        }
    }

    public void draw(Canvas canvas, float offsetx, float offsety) {
        canvas.drawText(strfps, 20 + offsetx, 10 + 50 + offsety, paint);
    }
}