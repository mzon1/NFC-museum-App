package com.hallym.login_v1001;

import com.hallym.login_v1001.R;
import com.hallym.network.networkState;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


public class View extends Activity implements OnTouchListener {
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	
	 static final int NONE = 0;
	 static final int DRAG = 1;
	 static final int ZOOM = 2;
	 int mode = NONE;
	 
	 int imgw;
	 int imgh;
	 
	 PointF start = new PointF();
	 PointF mid = new PointF();
	 PointF currentP = new PointF();
	 float oldDist = 1f;
	 
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int deviceWidth = displayMetrics.widthPixels;
		int deviceHeight = displayMetrics.heightPixels;

		// 꼭 넣어 주어야 한다. 이렇게 해야 displayMetrics가 세팅이 된다.

		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); 
		//int dipWidth  = (int) (120  * displayMetrics.density);
		//int dipHeight = (int) (90 * displayMetrics.density);

		
		Log.e("TEST","displayMetrics.density : " + displayMetrics.density);
		Log.e("TEST","deviceWidth : " + deviceWidth +", deviceHeight : "+deviceHeight);

		//img.setImageResource(R.drawable.museum);
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {

		ImageView img = (ImageView)findViewById(R.id.imageView1);
		imgw = img.getWidth();
		imgh = img.getHeight();
        
        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.museum); // 비트맵 이미지를 만든다.
	    //int width=(int)(getWindowManager().getDefaultDisplay().getWidth()); // 가로 사이즈 지정
	    //int height=(int)((width*571)/935); // 세로 사이즈 지정
	    Bitmap resizedbitmap=Bitmap.createScaledBitmap(bmp, 935, 571, true); // 이미지 사이즈 조정
	    img.setImageBitmap(setRoundCorner(resizedbitmap, networkState.user_x, networkState.user_y)); // 이미지뷰에 조정한 이미지 넣기

	    matrix.postTranslate((imgw - 935)/2, (imgh - 571)/2);
	    img.setImageMatrix(matrix);

		img.setOnTouchListener(this);
    }
	
	public static Bitmap setRoundCorner(Bitmap bitmap, int x, int y) {

	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);	       
	    //int color = 0xff424242;
	       
	    Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    //RectF rectF = new RectF(rect);


	    //Paint paint = new Paint();
	    //paint.setAntiAlias(true);
	    //paint.setColor(Color.LTGRAY);
	    //paint.setStrokeWidth(10);


	    //Paint paint1 = new Paint();	// 색깔 담당
		//paint1.setColor(color);
		//paint1.setStrokeWidth(10);	// paint의 굵기   

	    //canvas.drawARGB(0, 0, 0, 0);
	    //canvas.drawRoundRect(rectF, pixel, pixel, paint);
	    //canvas.drawArc(rectF, 0, -90, true, paint1);  
	    
	    Paint paint2 = new Paint();
	    paint2.setColor(Color.RED);
	    paint2.setTextSize(15);
	    canvas.drawCircle(100, 100, 80, paint2);

	   // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, null);
	    canvas.drawCircle(x, y, 15, paint2);
	    canvas.drawText("현 위치", x-20, y-20, paint2);
	    return output;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}

	@Override
	public boolean onTouch(android.view.View v, MotionEvent event) {
		//float scale;
		ImageView view = (ImageView)v;
		   // Dump touch event to log
		   dumpEvent(event);

		   // Handle touch events here...
		   switch (event.getAction() & MotionEvent.ACTION_MASK) {

		   case MotionEvent.ACTION_DOWN:
			  savedMatrix.set(matrix);
			  start.set(event.getX(),event.getY());
		      mode = DRAG;
		      break;
		   case MotionEvent.ACTION_UP: //first finger lifted
		   case MotionEvent.ACTION_POINTER_UP: //second finger lifted
		      mode = NONE;
		      break;
		   case MotionEvent.ACTION_POINTER_DOWN: //second finger down
		      oldDist = spacing(event);
		      if (oldDist > 15f) {
		    	 savedMatrix.set(matrix);
		         midPoint(mid, event);
		         mode = ZOOM;
		      }
		      break;

		   case MotionEvent.ACTION_MOVE: 
		      if (mode == DRAG) { 
		    	  matrix.set(matrix);
		    	  currentP.set(event.getX(), event.getY());
		    	  matrix.postTranslate(currentP.x-start.x, currentP.y-start.y);
		    	  start.set(currentP.x, currentP.y);	    	  
		      }
		      else if (mode == ZOOM) { //pinch zooming
		         float newDist = spacing(event);
		         if (newDist > 15f) {
		        	 matrix.set(savedMatrix);
		        	 float scale = newDist / oldDist; //thinking i need to play around with this value to limit it**
		             matrix.postScale(scale, scale,mid.x,mid.y);
		         }
		      }
		      break;
		   }

		   // Perform the transformation
		   view.setImageMatrix(matrix);
		   return true; // indicate event was handled
		}
		private float spacing(MotionEvent event) {
		   float x = event.getX(0) - event.getX(1);
		   float y = event.getY(0) - event.getY(1);
		   return FloatMath.sqrt(x * x + y * y);
		}

		private void midPoint(PointF point, MotionEvent event) {
		   float x = event.getX(0) + event.getX(1);
		   float y = event.getY(0) + event.getY(1);
		   point.set(x / 2, y / 2);
		}

		/** Show an event in the LogCat view, for debugging */
		private void dumpEvent(MotionEvent event) {
		   String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
		      "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
		   StringBuilder sb = new StringBuilder();
		   int action = event.getAction();
		   int actionCode = action & MotionEvent.ACTION_MASK;
		   sb.append("event ACTION_" ).append(names[actionCode]);
		   if (actionCode == MotionEvent.ACTION_POINTER_DOWN
		         || actionCode == MotionEvent.ACTION_POINTER_UP) {
		      sb.append("(pid " ).append(
		      action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
		      sb.append(")" );
		   }
		   sb.append("[" );
		   for (int i = 0; i < event.getPointerCount(); i++) {
		      sb.append("#" ).append(i);
		      sb.append("(pid " ).append(event.getPointerId(i));
		      sb.append(")=" ).append((int) event.getX(i));
		      sb.append("," ).append((int) event.getY(i));
		      if (i + 1 < event.getPointerCount())
		         sb.append(";" );
		   }
		   sb.append("]" );
		}
	}

