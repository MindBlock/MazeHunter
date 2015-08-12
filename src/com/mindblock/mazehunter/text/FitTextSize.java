package com.mindblock.mazehunter.text;

import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.widget.TextView;

public class FitTextSize {

	
	public static void correctHeight(TextView textView, int desiredHeight)
	{
	    Paint paint = new Paint();
	    Rect bounds = new Rect();

	    paint.setTypeface(textView.getTypeface());
	    float textSize = textView.getTextSize();
	    paint.setTextSize(textSize);
	    String text = textView.getText().toString();
	    paint.getTextBounds(text, 0, text.length(), bounds);

	    while (bounds.height() > desiredHeight)
	    {
	        textSize--;
	        paint.setTextSize(textSize);
	        paint.getTextBounds(text, 0, text.length(), bounds);
	    }

	    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (1.5*textSize));
	}
	
	public static void correctSize(TextView textView, int desiredWidth, int desiredHeight)
	{
	    Paint paint = new Paint();
	    Rect bounds = new Rect();

	    paint.setTypeface(textView.getTypeface());
	    float textSize = textView.getTextSize();
	    paint.setTextSize(textSize);
	    String text = textView.getText().toString();
	    paint.getTextBounds(text, 0, text.length(), bounds);

	    while (bounds.width() > desiredWidth || bounds.height() > desiredHeight)
	    {
	        textSize--;
	        paint.setTextSize(textSize);
	        paint.getTextBounds(text, 0, text.length(), bounds);
	    }

	    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (1.5*textSize));
	}
}
