package com.tipusarowar.rapidballvbo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class RapidBallRenderer implements Renderer{

	private final boolean LOG_SHOW = true;
	private final String LOG_TAG = "Renderer";



	private int mHeight;              // surface height
	private int mWidth;               // surface width	

	private Context context;         // context handle for resource id
	public DisplayMetrics mDisplayMetrics;
	public RapidBallActivity_GL mRapidBallActivity_GL;
	public RapidBallGLSurfaceView mRapidBallGLSurfaceView;

	/**************************/
	public GameVariables gameVariables;
	public UpdateAll updateAll;
	public DrawAll drawAll;
	public TrajectoryEngine trajectoryEngine;
	/**************************/
	// Pre-allocated arrays to use at runtime so that allocation during the
	// test can be avoided.
	private int[] mTextureNameWorkspace;
	private int[] mCropWorkspace;
	// Specifies the format our textures should be converted to upon load.
	private static BitmapFactory.Options sBitmapOptions	= new BitmapFactory.Options();
	/**
	 * Constructor
	 *
	 * @param context
	 * @param DisplayMetrics 
	 * @param RapidBallActivity_GL 
	 * @param RapidBallGLSurfaceView
	 */
	public RapidBallRenderer(Context context, DisplayMetrics displayMetrics, RapidBallActivity_GL rapidBallActivity_GL, RapidBallGLSurfaceView rapidBallGLSurfaceView ){//, GLSprite[] sprites){

		if(LOG_SHOW){
			Log.d(LOG_TAG, "Constructor created or called");
		}

		this.context = context;
		mWidth = displayMetrics.widthPixels;
		mHeight = displayMetrics.heightPixels;

		mDisplayMetrics = displayMetrics;
		mRapidBallActivity_GL = rapidBallActivity_GL;
		mRapidBallGLSurfaceView = rapidBallGLSurfaceView;

		/******************************************************************************************/
		/************************ NEWLY ALLOCATED ALL THE REQUIRED CLASSES ************************/
		/******************************************************************************************/
		gameVariables = new GameVariables(context, displayMetrics);	
		updateAll = new UpdateAll(context, gameVariables);
		drawAll = new DrawAll(context, gameVariables);
		trajectoryEngine = new TrajectoryEngine(context, gameVariables);
		/******************************************************************************************/
		// Pre-allocate and store these objects so we can use them at runtime
		// without allocating memory mid-frame.
		mTextureNameWorkspace = new int[1];
		mCropWorkspace = new int[4];		
		// Set our bitmaps to 16-bit, 565 format.
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	/**
	 * onsurfacecreated will be handled after all are done
	 * 
	 */
	public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {	
		if(LOG_SHOW){
			Log.d(LOG_TAG, " onSurfaceCreated called");
		}
		/*
		 * Some one-time OpenGL initialization can be made here probably based
		 * on features of this particular context
		 */
		gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		gl10.glClearColor(0.5f, 0.5f, 0.5f, 1);
		gl10.glShadeModel(GL10.GL_FLAT);
		gl10.glDisable(GL10.GL_DEPTH_TEST);
		gl10.glEnable(GL10.GL_TEXTURE_2D);
		/*
		 * By default, OpenGL enables features that improve quality but reduce
		 * performance. One might want to tweak that especially on software
		 * renderer.
		 */
		gl10.glDisable(GL10.GL_DITHER);
		gl10.glDisable(GL10.GL_LIGHTING);

		gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		if (gameVariables.spriteArray != null) {

			int lastLoadedResource = -1;
			int lastTextureId = -1;

			for (int x = 0; x < gameVariables.spriteArray.length; x++) {
				if(LOG_SHOW){
					Log.d(LOG_TAG+" Load Bitmap", "Loading"+" spriteArray.length :"+gameVariables.spriteArray.length+" x :"+x);
				}
				int resource = gameVariables.spriteArray[x].getResourceId();
				if (resource != lastLoadedResource) {
					lastTextureId = loadBitmap(context, gl10, resource);
					lastLoadedResource = resource;
				}
				gameVariables.spriteArray[x].setTextureName(lastTextureId);
			}
		}
		if ( gameVariables.digitArray != null) {

			int lastLoadedResource = -1;
			int lastTextureId = -1;

			for (int x = 0; x < gameVariables.digitArray.length; x++) {
				if(LOG_SHOW){
					Log.d(LOG_TAG+" Load Bitmap", "Loading"+" spriteArray.length :"+gameVariables.spriteArray.length+" x :"+x);
				}
				int resource = gameVariables.digitArray[x].getResourceId();
				if (resource != lastLoadedResource) {
					lastTextureId = loadBitmap(context, gl10, resource);
					lastLoadedResource = resource;
				}
				gameVariables.digitArray[x].setTextureName(lastTextureId);
			}
		}

		if ( gameVariables.lifeArray != null) {

			int lastLoadedResource = -1;
			int lastTextureId = -1;

			for (int x = 0; x < gameVariables.lifeArray.length; x++) {
				if(LOG_SHOW){
					Log.d(LOG_TAG+" Load Bitmap", "Loading"+" spriteArray.length :"+gameVariables.spriteArray.length+" x :"+x);
				}
				int resource = gameVariables.lifeArray[x].getResourceId();
				if (resource != lastLoadedResource) {
					lastTextureId = loadBitmap(context, gl10, resource);
					lastLoadedResource = resource;
				}
				gameVariables.lifeArray[x].setTextureName(lastTextureId);
			}
		}


	}

	/**
	 * Called when the surface has changed, for example the
	 * @param gl10 openGl handle
	 * @param width - new width
	 * @param height - new height
	 */
	public void onSurfaceChanged(GL10 gl10, int width, int height ){ 
		// from xsimplerenderer
		gl10.glViewport(0, 0, width, height);
		/*
		 * Set our projection matrix. This doesn't have to be done each time we
		 * draw, but usually a new projection needs to be set when the viewport
		 * is resized.
		 */
		float ratio = (float) width / height ; // i / i1;
		gl10.glMatrixMode(GL10.GL_PROJECTION);
		gl10.glLoadIdentity();
		// When using this glFrustumf() our screen becomes flat for this kind of operation
		//gl10.glFrustumf(-ratio, ratio, -1, 1, 1, 10); 
		gl10.glOrthof(0.0f, width, 0.0f, height, 0.0f, 1.0f);

		gl10.glShadeModel(GL10.GL_FLAT);
		gl10.glEnable(GL10.GL_BLEND);
		gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl10.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		gl10.glEnable(GL10.GL_TEXTURE_2D);

		gameVariables.onSurfaceChanged( width, height);//width, height);
	}

	public void onDrawFrame(GL10 gl) {

		if( gameVariables.GAME_OVER == false ){

			gameVariables.beginTime = System.currentTimeMillis();
			gameVariables.framesSkipped = 0;  // resetting the frames skipped

			if(LOG_SHOW){
				Log.d(LOG_TAG+" beginTime", "beginTime :"+gameVariables.beginTime/1000);
			}

			double UpTime = System.currentTimeMillis();

			updateAll.upDate();

			double UpFinishTime = System.currentTimeMillis(); 

			if(LOG_SHOW){
				Log.d("Time 4 upDate()", "UpStartTime :"+UpTime/1000+" UpFinishTime :"+UpFinishTime+" TimeTaken :"+ (UpFinishTime-UpTime));
			}
			double DrawStartTime = System.currentTimeMillis();
			drawAll.draw(gl);
			double DrawFinishTime = System.currentTimeMillis(); 
			if(LOG_SHOW){
				Log.d("Time 4 Draw()", "DrawStartTime :"+DrawStartTime/1000+" DrawFinishTime :"+DrawFinishTime+" TimeTaken :"+ (DrawFinishTime-DrawStartTime));
			}

			// calculate how long did the cycle take
			gameVariables.timeDiff = ( System.currentTimeMillis() - gameVariables.beginTime ) ;
			// calculate sleep time
			gameVariables.sleepTime = (int)(gameVariables.FRAME_PERIOD - gameVariables.timeDiff);

			if(LOG_SHOW){
				Log.d(LOG_TAG+"TimeDiff "+"sleepTime", "TimeDiff :"+gameVariables.timeDiff+" SleepTime :"+gameVariables.sleepTime);
			}

			if (gameVariables.sleepTime > 0) {
				// if sleepTime > 0 we're OK
				try {
					// send the thread to sleep for a short period
					// very useful for battery saving
					Thread.sleep(gameVariables.sleepTime);
					if(LOG_SHOW){
						Log.d("Inside SleepTime try", "Thread.sleep(gv.sleepTime)");
					}
				} catch (InterruptedException e) {}
			}
			while ( gameVariables.sleepTime < 0 && gameVariables.framesSkipped < gameVariables.MAX_FRAME_SKIPS ) {
				// we need to catch up
				// update without rendering
				updateAll.upDate();
				// add frame period to check if in next frame
				gameVariables.sleepTime += gameVariables.FRAME_PERIOD;
				gameVariables.framesSkipped++;

				if(LOG_SHOW){
					Log.d("Inside While Loop", "frameSkipped :"+gameVariables.framesSkipped+" SleepTime :"+gameVariables.sleepTime);
				}
			}
		}
		else{
			drawAll.draw(gl);
		}
	}


	/** 
	 * Loads a bitmap into OpenGL and sets up the common parameters for 
	 * 2D texture maps. 
	 */
	protected int loadBitmap(Context context, GL10 gl, int resourceId) {
		int textureName = -1;
		if (context != null && gl != null) {
			if(LOG_SHOW){
				Log.d(LOG_TAG+" method LoadBitmap", "inside if");
			}
			gl.glGenTextures(1, mTextureNameWorkspace, 0);

			textureName = mTextureNameWorkspace[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

			InputStream is = context.getResources().openRawResource(resourceId);
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// Ignore.
				}
			}

			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			mCropWorkspace[0] = 0;
			mCropWorkspace[1] = bitmap.getHeight();
			mCropWorkspace[2] = bitmap.getWidth();
			mCropWorkspace[3] = -bitmap.getHeight();

			bitmap.recycle();

			((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);

			int error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR) {
				Log.e("SpriteMethodTest", "Texture Load GLError: " + error);
			}
		}
		return textureName;
	}



	public void handleDpadRight() {
		// how long can ball go
		gameVariables.limit_on_X = (int) ( gameVariables.rapidBallCoordinate.x + gameVariables.velocity_on_X);
		// whether this exceeds limit
		if( gameVariables.limit_on_X > ( gameVariables.surface_width - gameVariables.ball_diameter ) )
			gameVariables.limit_on_X = ( gameVariables.surface_width - gameVariables.ball_diameter );

		gameVariables.rapidBallCoordinate.right = true;
		gameVariables.rapidBallCoordinate.left = false;		
	}

	public void handleDpadLeft() {
		// how long can ball go
		gameVariables.limit_on_X = (int) ( gameVariables.rapidBallCoordinate.x + gameVariables.velocity_on_X);
		//whether this exceeds limit
		if( gameVariables.limit_on_X < 0 )
			gameVariables.limit_on_X = 0 ;

		gameVariables.rapidBallCoordinate.left = true;
		gameVariables.rapidBallCoordinate.right = false;

		gameVariables.trajectory_X_Velocity *= ( -1 ) ;

	}

	public void handleDpadOff() {
		// TODO Auto-generated method stub
		gameVariables.rapidBallCoordinate.right = false;
		gameVariables.rapidBallCoordinate.left = true;
	}
}