/*

 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.


package com.tipusarowar.rapidballvbo;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

 *//**
 * Activity for testing OpenGL ES drawing speed.  This activity sets up sprites 
 * and passes them off to an OpenGLSurfaceView for rendering and movement.
 *//*
public class RapidBallActivity_GL extends Activity {
	private final static int SPRITE_WIDTH = 100; // 64;
	private final static int SPRITE_HEIGHT = 10; //  64;


	private XGLSurfaceView mGLSurfaceView;
	private XSimpleGLRenderer spriteRenderer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);       
  *//****************************************************//*
		GLSprite[] spriteArray = new GLSprite[3] ;//robotCount + 1];
		GLSprite background = new GLSprite(R.drawable.rapid_space);//background);
		BitmapDrawable backgroundImage = (BitmapDrawable)getResources().getDrawable(R.drawable.rapid_space);//background);
		Bitmap backgoundBitmap = backgroundImage.getBitmap();
		background.width = backgoundBitmap.getWidth();
		background.height = backgoundBitmap.getHeight();

		Grid backgroundGrid = new Grid(2, 2, false);
		backgroundGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f, 1.0f, null);
		backgroundGrid.set(1, 0, background.width, 0.0f, 0.0f, 1.0f, 1.0f, null);
		backgroundGrid.set(0, 1, 0.0f, background.height, 0.0f, 0.0f, 0.0f, null);
		backgroundGrid.set(1, 1, background.width, background.height, 0.0f, 1.0f, 0.0f, null );
		background.setGrid(backgroundGrid);
		spriteArray[0] = background;

		Grid spriteGrid = null;
		spriteGrid = new Grid(2, 2, false);
		spriteGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		spriteGrid.set(1, 0, SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		spriteGrid.set(0, 1, 0.0f, SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		spriteGrid.set(1, 1, SPRITE_WIDTH, SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);
   *//**********************************************//*
		GLSprite robot = new GLSprite(R.drawable.red_bar);
		robot.width = SPRITE_WIDTH;
		robot.height = SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[1] = robot;

		robot = new GLSprite(R.drawable.pink_bar);
		robot.width = SPRITE_WIDTH;
		robot.height = SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[2] = robot;
    *//************************************************//*
		RapidBarCoordinate[] coordinate = new RapidBarCoordinate[5];
		int count = 0;
		for(int i=0 ; i<5 ; i++){
			coordinate[i] = new RapidBarCoordinate();
			coordinate[i].y = (float) count;
			coordinate[i].x = (float) new Random().nextInt(350);
			coordinate[i].type = new Random().nextInt(5);
			count+=80;
		}    	        

		mGLSurfaceView = new XGLSurfaceView(this);
		spriteRenderer = new XSimpleGLRenderer(this, dm);

		spriteRenderer.setRapidCoord(coordinate);
		spriteRenderer.setSprites(spriteArray);
		spriteRenderer.setVertMode(true, false);//useHardwareBuffers);

		mGLSurfaceView.setRenderer(spriteRenderer);

		setContentView(mGLSurfaceView);    
	}
}
     */
package com.tipusarowar.rapidballvbo;

import java.util.Random;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class RapidBallActivity_GL extends Activity {

	//******************//
	private final String LOG_TAG ="RAPID_ACTIVITY";
	private boolean LOG_SHOW = true;
	public Handler handler;
	//******************//

	private RapidBallGLSurfaceView rapidBallGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/***************************************************/
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		//****************************************************//
		handler = new Handler();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		rapidBallGLSurfaceView = new RapidBallGLSurfaceView(getApplicationContext(),displayMetrics,this,handler);//,spriteArray);
		setContentView(rapidBallGLSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rapid_ball_activity_layout, menu);
		return true;
	}

	@Override
	protected void onPause() {
		Log.d("RapidBallActivity_GL", "On Pause");
		super.onPause();
		if(LOG_SHOW){
			//Log.d("B4finishinActivity_GL", "value :"+rapidBallGLSurfaceView.rapidBallRenderer.gameVariables.GAME_OVER);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//rapidBallGLSurfaceView.rapidBallRenderer.gameVariables.mediaPlayer = MediaPlayer.create(this, R.raw.bwv_115); 
		//mediaPlayer = MediaPlayer.create(this, R.raw.bwv_115);
		//rapidBallGLSurfaceView.rapidBallRenderer.gameVariables.mediaPlayer.start();
	}

	@Override
	protected void onStop(){
		super.onStop();
		if(LOG_SHOW){
			Log.d("RapidActivity_GLonStop", "onStop Called");
		}
	}
}
