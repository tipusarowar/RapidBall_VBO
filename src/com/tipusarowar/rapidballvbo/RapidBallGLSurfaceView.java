/**
 * 
 */
package com.tipusarowar.rapidballvbo;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;

/**
 * @author Administrator
 *
 */
public class RapidBallGLSurfaceView extends GLSurfaceView {

	private final String LOG_TAG = "RapidBallGLSurfaceView";
	private boolean LOG_SHOW = true;

	private RapidBallRenderer rapidBallRenderer;
	private Context mContext;
	private VelocityTracker velocityTracker;
	/***********/
	private RapidBallActivity_GL activity_GL;
	private Handler mHandler;

	public RapidBallGLSurfaceView(Context context,DisplayMetrics displayMetrics,RapidBallActivity_GL activity_gl,Handler handler){//,GLSprite[] sprites){
		super(context);

		mContext = context; 
		activity_GL = activity_gl;
		mHandler = handler;

		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(1);

		if(!LOG_SHOW){
			Log.d("RapidBallGLSurfaceView", "Created RapidBallGLSurfaceView");
		}
		// Set the Renderer for drawing on the GLSurfaceView
		rapidBallRenderer = new RapidBallRenderer(context,displayMetrics,activity_gl,this);//,sprites);//handler);
		setRenderer(rapidBallRenderer);

		setFocusableInTouchMode(true);
	}
	/***************************************************************/	
	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
	}

	public void rapidOnDetachedFromWindow(){
		Log.d("rapidOnDetachedFromWindow()", "see what happens");
		this.onDetachedFromWindow();
	}

	/***************************************************************/
	@Override
	public void onPause(){

		if(LOG_SHOW){
			Log.d("GLSURFACEVIEW ONPAUSE", "check :1");
		}
		super.onPause();
	}

	@Override
	public void onResume(){
		super.onResume();
		if(LOG_SHOW){
			Log.d("GLSurfaceView OnResume","staring onResume");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			if(velocityTracker == null) {
				velocityTracker = VelocityTracker.obtain();
			}
			else {
				velocityTracker.clear();
			}
			velocityTracker.addMovement(event);
			break;
		case MotionEvent.ACTION_MOVE:
			velocityTracker.addMovement(event);
			velocityTracker.computeCurrentVelocity(1000);
			Log.v("VelocityTracker", "X velocity is " + velocityTracker.getXVelocity() +" pixels per second");
			Log.v("VelocityTracker", "Y velocity is " + velocityTracker.getYVelocity() +" pixels per second");
			rapidBallRenderer.gameVariables.velocity_on_X = ( velocityTracker.getXVelocity() /10 ) ;
			rapidBallRenderer.gameVariables.velocity_on_Y = ( velocityTracker.getYVelocity() /10 ) ;
			rapidBallRenderer.gameVariables.trajectory_X_Velocity = ( velocityTracker.getXVelocity() / 10 ) ;

			queueEvent(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if( rapidBallRenderer.gameVariables.velocity_on_X > 0 )
						rapidBallRenderer.handleDpadRight();
					else
						rapidBallRenderer.handleDpadLeft();
				}
			});
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			velocityTracker.recycle();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			queueEvent(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					rapidBallRenderer.handleDpadRight();
				}
			});
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){

			queueEvent(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					rapidBallRenderer.handleDpadLeft();
				}
			});
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){

		queueEvent(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rapidBallRenderer.handleDpadOff();
			}
		});
		return false;
	}
}
