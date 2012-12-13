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
 */

package com.tipusarowar.rapidballvbo;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Activity for testing OpenGL ES drawing speed.  This activity sets up sprites 
 * and passes them off to an OpenGLSurfaceView for rendering and movement.
 */
public class OpenGLTestActivity extends Activity {
	private final static int SPRITE_WIDTH = 100; // 64;
	private final static int SPRITE_HEIGHT = 10; //  64;


	private XGLSurfaceView mGLSurfaceView;
	private XSimpleGLRenderer spriteRenderer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Clear out any old profile results.
		//ProfileRecorder.sSingleton.resetAll();

		final Intent callingIntent = getIntent();
		// Allocate our sprites and add them to an array.
		final int robotCount = callingIntent.getIntExtra("spriteCount", 10);
		final boolean animate = callingIntent.getBooleanExtra("animate", true);
		final boolean useVerts = true;
		//callingIntent.getBooleanExtra("useVerts", false);
		final boolean useHardwareBuffers = 
				callingIntent.getBooleanExtra("useHardwareBuffers", false);      

		// We need to know the width and height of the display pretty soon,
		// so grab the information now.
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);       
		// Allocate space for the robot sprites + one background sprite.
		/****************************************************/
		GLSprite[] spriteArray = new GLSprite[3] ;//robotCount + 1];
		GLSprite background = new GLSprite(R.drawable.rapid_space);//background);
		BitmapDrawable backgroundImage = (BitmapDrawable)getResources().getDrawable(R.drawable.rapid_space);//background);
		Bitmap backgoundBitmap = backgroundImage.getBitmap();
		background.width = backgoundBitmap.getWidth();
		background.height = backgoundBitmap.getHeight();
		if (useVerts) {
			// Setup the background grid.  This is just a quad.
			Grid backgroundGrid = new Grid(2, 2, false);
			backgroundGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f, 1.0f, null);
			backgroundGrid.set(1, 0, background.width, 0.0f, 0.0f, 1.0f, 1.0f, null);
			backgroundGrid.set(0, 1, 0.0f, background.height, 0.0f, 0.0f, 0.0f, null);
			backgroundGrid.set(1, 1, background.width, background.height, 0.0f, 
					1.0f, 0.0f, null );
			background.setGrid(backgroundGrid);
		}
		spriteArray[0] = background;

		Grid spriteGrid = null;
		if (useVerts) {
			// Setup a quad for the sprites to use.  All sprites will use the
			// same sprite grid intance.
			spriteGrid = new Grid(2, 2, false);
			spriteGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
			spriteGrid.set(1, 0, SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
			spriteGrid.set(0, 1, 0.0f, SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
			spriteGrid.set(1, 1, SPRITE_WIDTH, SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);
		}       
		// This list of things to move. It points to the same content as the
		// sprite list except for the background.
		// Renderable[] renderableArray = new Renderable[ 2 ];//robotCount]; 
		/*final int robotBucketSize = robotCount / 3;
        for (int x = 0; x < robotCount; x++) {
            GLSprite robot;
            // Our robots come in three flavors.  Split them up accordingly.
            if (x < robotBucketSize) {
                robot = new GLSprite(R.drawable.skate1);
            } else if (x < robotBucketSize * 2) {
                robot = new GLSprite(R.drawable.skate2);
            } else {
                robot = new GLSprite(R.drawable.skate3);
            }        
            robot.width = SPRITE_WIDTH;
            robot.height = SPRITE_HEIGHT;            
            // Pick a random location for this sprite.
            robot.x = (float)(Math.random() * dm.widthPixels);
            robot.y = (float)(Math.random() * dm.heightPixels);            
            // All sprites can reuse the same grid.  If we're running the
            // DrawTexture extension test, this is null.
            robot.setGrid(spriteGrid);            
            // Add this robot to the spriteArray so it gets drawn and to the
            // renderableArray so that it gets moved.
		 *//*********** IMPORTANT *************//*
            spriteArray[x + 1] = robot;
            renderableArray[x] = robot;
        }*/
		/**********************************************/
		GLSprite robot = new GLSprite(R.drawable.red_bar);
		robot.width = SPRITE_WIDTH;
		robot.height = SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[1] = robot;
		// renderableArray[0] = robot;

		robot = new GLSprite(R.drawable.pink_bar);
		robot.width = SPRITE_WIDTH;
		robot.height = SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[2] = robot;
		// renderableArray[1] = robot;
		/************************************************/
		// Now's a good time to run the GC.  Since we won't do any explicit
		// allocation during the test, the GC should stay dormant and not
		// influence our results.
		Runtime r = Runtime.getRuntime();
		r.gc();        
		RapidBarCoordinate[] coordinate = new RapidBarCoordinate[5];
		int count = 0;
		for(int i=0 ; i<5 ; i++){
			coordinate[i] = new RapidBarCoordinate();
			coordinate[i].y = (float) count;
			coordinate[i].x = (float) new Random().nextInt(350);
			coordinate[i].type = new Random().nextInt(5);
			count+=80;
		}    	        
		/*        if (true){//animate) {
		 * 			Mover simulationRuntime = new Mover();
            simulationRuntime.setRenderables(renderableArray);            
            simulationRuntime.setViewSize(dm.widthPixels, dm.heightPixels);
		 *//****************//*
            simulationRuntime.setCoordinate(coordinate);
            simulationRuntime.init();
		  *//******************//*
            mGLSurfaceView.setEvent(simulationRuntime);
        }*/    
		mGLSurfaceView = new XGLSurfaceView(this);
		spriteRenderer = new XSimpleGLRenderer(this, dm);

		spriteRenderer.setRapidCoord(coordinate);
		spriteRenderer.setSprites(spriteArray);
		spriteRenderer.setVertMode(true, false);//useHardwareBuffers);

		mGLSurfaceView.setRenderer(spriteRenderer);

		setContentView(mGLSurfaceView);    
	}
}