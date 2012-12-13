package com.tipusarowar.rapidballvbo;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;



import android.content.Context;

public class DrawAll {
	public Context context;
	public GameVariables mGameVariables;
	public DrawAll(Context context,GameVariables gameVariables){
		this.context = context;
		this.mGameVariables = gameVariables;
	}
	public void draw(GL10 gl){
		// Just clear the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Begin drawing
		//--------------
		// These function calls can be experimented with for various effects such as transparency
		// although certain functionality maybe device specific.
		/*		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);

		// Setup correct projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, mGameVariables.surface_width, 0.0f, mGameVariables.surface_height, 0.0f, 1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glEnable(GL10.GL_TEXTURE_2D);

		 */		
		// Draw all VBO

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		Grid.beginDrawing(gl, true, false);

		if( mGameVariables.GAME_OVER == false ){

			mGameVariables.spriteArray[0].setTranslatef(0.0f, 0.0f, 0.0f);
			mGameVariables.spriteArray[0].draw(gl);
			for(int x = 0 ; x < 5 ; x++){
				if( mGameVariables.barCoordinates[x].type == 2 ){
					mGameVariables.spriteArray[2].setTranslatef(mGameVariables.barCoordinates[x].x,mGameVariables.barCoordinates[x].y, 0.0f);
					mGameVariables.spriteArray[2].draw(gl);
				}
				else{
					mGameVariables.spriteArray[1].setTranslatef(mGameVariables.barCoordinates[x].x,mGameVariables.barCoordinates[x].y, 0.0f);
					mGameVariables.spriteArray[1].draw(gl);
				}
				if( mGameVariables.barCoordinates[x].life == mGameVariables.life_controlling_variable ){
					mGameVariables.lifeArray[0].setTranslatef(mGameVariables.barCoordinates[x].x + 40 , mGameVariables.barCoordinates[x].y+10 , 0.0f);
					mGameVariables.lifeArray[0].draw(gl);
				}
			}

			mGameVariables.spriteArray[3].setTranslatef(mGameVariables.rapidBallCoordinate.x, mGameVariables.rapidBallCoordinate.y, 0.0f);
			mGameVariables.spriteArray[3].draw(gl);

			// drawing life logo
			float y = mGameVariables.surface_height - ( mGameVariables.LIFE_SPRITE_HEIGHT + 10 );
			float x = mGameVariables.surface_width - ( mGameVariables.LIFE_SPRITE_WIDTH + 10 );
			for(int i=0 ; i<mGameVariables.life ; i++){
				mGameVariables.lifeArray[0].setTranslatef( x, y, 0.0f);
				mGameVariables.lifeArray[0].draw(gl);
				x -= mGameVariables.LIFE_SPRITE_WIDTH;
			}

			// Draw Score
			int score = mGameVariables.score;
			int digitCount = 0;
			while(score != 0){
				score /= 10;
				digitCount++;
			}
			if( digitCount == 0 )
				digitCount = 1;

			score = mGameVariables.score ;

			float yD = (float) mGameVariables.surface_height - ( mGameVariables.DIGIT_SPRITE_HEIGHT + 10 );
			float xD = 10 + ( mGameVariables.DIGIT_SPRITE_WIDTH * (digitCount - 1 )) ;

			int remainder = 0 ;

			if(score == 0){
				mGameVariables.digitArray[0].setTranslatef(xD, yD, 0.0f);
				mGameVariables.digitArray[0].draw(gl);
			}
			else{
				while( score != 0){
					remainder = (score %10 );
					score /= 10 ;
					
					mGameVariables.digitArray[remainder].setTranslatef(xD, yD, 0.0f);
					mGameVariables.digitArray[remainder].draw(gl);

					xD -= mGameVariables.DIGIT_SPRITE_WIDTH ;
				}		
			}
		}
		else{
			mGameVariables.spriteArray[4].setTranslatef(0.0f, 0.0f, 0.0f);
			mGameVariables.spriteArray[4].draw(gl);				
		}

		Grid.endDrawing(gl);

		// End of Drawing by VBO	

		// Draw all Textures
		/*		if(mGameVariables.GAME_OVER == true){
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mGameVariables.gameOverTexture.texture[0]);
			((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mGameVariables.gameOverTexture.mCropWorkspace, 0);  
			((GL11Ext) gl).glDrawTexfOES(0f, 0f, 0f, mGameVariables.gameOverTexture.width, mGameVariables.gameOverTexture.height);

		}
		else{
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mGameVariables.backgroundTexture.texture[0]);
			((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mGameVariables.backgroundTexture.mCropWorkspace, 0);  
			((GL11Ext) gl).glDrawTexfOES(0f, 0f, 0f, mGameVariables.backgroundTexture.width, mGameVariables.backgroundTexture.height);

		 *//******************************************************************************************//*
			for(int i = 0 ; i < mGameVariables.total_bar_on_screen ; i++){
				if(mGameVariables.barCoordinates[i].type == 2){				
					gl.glBindTexture(GL10.GL_TEXTURE_2D, mGameVariables.redTexture.texture[0]);
					((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mGameVariables.redTexture.mCropWorkspace, 0);  
					((GL11Ext) gl).glDrawTexfOES((float)mGameVariables.barCoordinates[i].x, (float)(mGameVariables.barCoordinates[i].y), 0, mGameVariables.redTexture.width, mGameVariables.redTexture.height);
				}
				else{
					if( cords[i].life == 5 ){ //DRAWING LIFE HERE
											//paint.setARGB(150, 200, 150, 200);
											//canvas.drawCircle( (cords[i].x+45), (cords[i].y -5 ), 5, paint);
										}				
					gl.glBindTexture(GL10.GL_TEXTURE_2D, mGameVariables.pinkTexture.texture[0]);
					((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mGameVariables.pinkTexture.mCropWorkspace, 0);  
					((GL11Ext) gl).glDrawTexfOES((float)(mGameVariables.barCoordinates[i].x), (float)(mGameVariables.barCoordinates[i].y), 0, mGameVariables.pinkTexture.width, mGameVariables.pinkTexture.height);
				}
			}
		  *//******************************************************************************************//*
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mGameVariables.rapidBallTexture.texture[0]);
			((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mGameVariables.rapidBallTexture.mCropWorkspace, 0);  
			((GL11Ext) gl).glDrawTexfOES((float)(mGameVariables.rapidBallCoordinate.x), (float)(mGameVariables.rapidBallCoordinate.y), 0, mGameVariables.rapidBallTexture.width, mGameVariables.rapidBallTexture.height);

		   *//******************************************************************************************//*
		}*/
		// Finish drawing
		gl.glDisable(GL10.GL_BLEND);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPopMatrix();

	}
}
