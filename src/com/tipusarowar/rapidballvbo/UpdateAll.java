package com.tipusarowar.rapidballvbo;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.util.Log;

public class UpdateAll {

	private final boolean LOG_SHOW = false;
	private final String LOG_TAG = "UpdateAll";

	private Context mContext;
	private GameVariables mGameVariables;

	public UpdateAll(Context context,GameVariables gameVariables){
		mContext = context;
		this.mGameVariables = gameVariables;
	}

	public void upDate(){
		/*************************** BAR ********************************************************************/
		if(LOG_SHOW){
			Log.d("NEW UPDATE", "For Loop");
			Log.d("Y Limit", "mGameVariables.surface_height : "+mGameVariables.surface_height);
		}
		for (int i = 0; i < mGameVariables.total_bar_on_screen ; i++) {  				

			if( (mGameVariables.barCoordinates[i].y + 10 ) >= mGameVariables.surface_height){ //<= 40){ // SCORE <= 40

				mGameVariables.barCoordinates[i].x = new Random().nextInt(mGameVariables.surface_width-100);
				mGameVariables.barCoordinates[i].y = 2 ; //0;//surface_height;
				mGameVariables.barCoordinates[i].type= new Random().nextInt(mGameVariables.barCoordinates_identity_type);
				mGameVariables.newly_created_bar_id = i;
				mGameVariables.barCoordinates[i].life = new Random().nextInt(mGameVariables.life_controlling_variable);
				if(LOG_SHOW){
					Log.d("NewNewNewBar", "i :"+i+" type :"+mGameVariables.barCoordinates[i].type);
				}
				//checking whether life lies on a red bar
				if(mGameVariables.barCoordinates[i].type == 2 && mGameVariables.barCoordinates[i].life == 5)
					mGameVariables.barCoordinates[i].life++;
			}else {
				mGameVariables.barCoordinates[i].y += mGameVariables.increase_y_coordinate_by_this;
			}		
			if(LOG_SHOW){
				Log.d("UpDate", "i :"+i+" Y :"+mGameVariables.barCoordinates[i].y);			
			}
		}
		/************************************* BAR ********************************************************/
		/************************************* BALL *******************************************************/
		/******************************************** BALL'S POSITION **********************************************************/

		int before_x,after_x;
		int before_y,after_y; 


		before_x=(int)mGameVariables.rapidBallCoordinate.x;
		before_y=(int)mGameVariables.rapidBallCoordinate.y;

		if( mGameVariables.rapidBallCoordinate.right == true ){
			if( mGameVariables.trajectory_X_Velocity >= 100 ){
				int x = (int) ( mGameVariables.trajectory_X_Velocity * mGameVariables.time) ;
				if( ( mGameVariables.rapidBallCoordinate.x + x ) > ( mGameVariables.surface_width - mGameVariables.ball_diameter )  ){
					mGameVariables.rapidBallCoordinate.x = ( mGameVariables.surface_width - mGameVariables.ball_diameter ) ;
					mGameVariables.rapidBallCoordinate.right = false ;
					mGameVariables.rapidBallCoordinate.left = false ;
				}
				else{
					mGameVariables.rapidBallCoordinate.x += x ;
					mGameVariables.trajectory_X_Velocity -= 100 ;
				}
			}
			else{
				mGameVariables.rapidBallCoordinate.x += 1 ;
				mGameVariables.rapidBallCoordinate.right = false;
				mGameVariables.rapidBallCoordinate.left = false ;
			}
		}		
		else if( mGameVariables.rapidBallCoordinate.left == true ){
			if( mGameVariables.trajectory_X_Velocity >= 100 ){
				int x = (int) ( mGameVariables.trajectory_X_Velocity * mGameVariables.time);
				if( ( mGameVariables.rapidBallCoordinate.x - x ) < 0  ){
					mGameVariables.rapidBallCoordinate.x = 0 ;
					mGameVariables.rapidBallCoordinate.right = false ;
					mGameVariables.rapidBallCoordinate.left = false ;
				}
				else{
					mGameVariables.rapidBallCoordinate.x -= x ;
					mGameVariables.trajectory_X_Velocity -= 100 ;
				}
			}
			else{
				mGameVariables.rapidBallCoordinate.x -= 1 ;
				mGameVariables.rapidBallCoordinate.right = false ;
				mGameVariables.rapidBallCoordinate.left = false;
			}
		}
		/*if( mGameVariables.rapidBallCoordinate.right == true ){ // RIGHT MOVE CHANGE : X
			mGameVariables.rapidBallCoordinate.x += mGameVariables.gx;
		 *//**************************** VELOCITY TRACKER ************************************************//*
			if( mGameVariables.rapidBallCoordinate.x >= mGameVariables.limit_on_X ){
				mGameVariables.rapidBallCoordinate.x = mGameVariables.limit_on_X ;
				mGameVariables.rapidBallCoordinate.right = false;
			}
		}
		else if( mGameVariables.rapidBallCoordinate.left == true ){	 // LEFT MOVE CHANGE : X
			mGameVariables.rapidBallCoordinate.x -= mGameVariables.gx;
		  *//**************************** VELOCITY TRACKER ************************************************//*
			if( mGameVariables.rapidBallCoordinate.x < mGameVariables.limit_on_X ){
				mGameVariables.rapidBallCoordinate.x = mGameVariables.limit_on_X ;
				mGameVariables.rapidBallCoordinate.left = false;
			}
		}*/
		/************************** YYYYYYYYYY *************************/
		if( mGameVariables.rapidBallCoordinate.onBar == true ){			// Y MOVE
			if( mGameVariables.rapidBallCoordinate.x < ( mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x ) || mGameVariables.rapidBallCoordinate.x > ( mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x + 100 ) ){	// BALL FALLING FIRST TIME
				mGameVariables.rapidBallCoordinate.y -= mGameVariables.gy; // BALL FALLING FIRST TIME   
				mGameVariables.rapidBallCoordinate.onBar = false;
			}else 
				mGameVariables.rapidBallCoordinate.y += mGameVariables.increase_y_coordinate_by_this;	// BALL MOVING UP WITH BAR
		}
		else{
			mGameVariables.rapidBallCoordinate.y -= mGameVariables.gy;		//FALLING AND SCOREING
			mGameVariables.score += 10;										// SCORING
		}

		after_x = (int) mGameVariables.rapidBallCoordinate.x;
		after_y = (int) mGameVariables.rapidBallCoordinate.y;

		if(mGameVariables.rapidBallCoordinate.onBar == false ){
			for(int i = 0; i < mGameVariables.total_bar_on_screen ; i++){//WHETHER BALL LIES ON A BAR
				//if( before_y <= mGameVariables.barCoordinates[i].y && mGameVariables.barCoordinates[i].y <= after_y && mGameVariables.barCoordinates[i].x < after_x && after_x < (mGameVariables.barCoordinates[i].x+100) ){
				if( before_y >= (mGameVariables.barCoordinates[i].y + 10 ) && ( mGameVariables.barCoordinates[i].y + 10 ) >= after_y && mGameVariables.barCoordinates[i].x <= after_x && after_x < ( mGameVariables.barCoordinates[i].x + 100 ) ){

					mGameVariables.rapidBallCoordinate.y = mGameVariables.barCoordinates[i].y + 10;
					mGameVariables.rapidBallCoordinate.onBar=true;
					mGameVariables.rapidBallCoordinate.right = false;
					mGameVariables.rapidBallCoordinate.left = false;
					mGameVariables.rapidBallCoordinate.cord_id=i;
					/*if(mGameVariables.barCoordinates[i].mGameVariables.life == 5 ){
					Log.d("Life:","mGameVariables.barCoordinates[i].mGameVariables.life == 5"+" mGameVariables.rapidBallCoordinate.x :"+mGameVariables.rapidBallCoordinate.x+" (mGameVariables.barCoordinates[i].x+ 40):"+(mGameVariables.barCoordinates[i].x+ 40)+" (mGameVariables.barCoordinates[i].x+50): "+(mGameVariables.barCoordinates[i].x+50) );
					if(mGameVariables.rapidBallCoordinate.x >= (mGameVariables.barCoordinates[i].x+ 40) && mGameVariables.rapidBallCoordinate.x <= (mGameVariables.barCoordinates[i].x+50)){
						mGameVariables.life++;
						mGameVariables.barCoordinates[i].mGameVariables.life++;
						Log.d("NewLIfe", "mGameVariables.life: "+mGameVariables.life+"");
					}							
				}*/
					if(mGameVariables.barCoordinates[i].type == 2){
						newLifeState();
					}
					//Log.d("Inside_RightMoveCheck", "mGameVariables.rapidBallCoordinate.x: "+mGameVariables.rapidBallCoordinate.x+" mGameVariables.barCoordinates[i].x: "+mGameVariables.barCoordinates[i].x+" mGameVariables.rapidBallCoordinate.y: "+mGameVariables.rapidBallCoordinate.y+" Cords[i].y: "+mGameVariables.barCoordinates[i].y);
					break;
				}
			}
		}

		// Eating life so increasing Life 
		if(mGameVariables.rapidBallCoordinate.onBar == true)
			if(mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].life == 5 ){
				//if()
				Log.d("Life:","mGameVariables.barCoordinates[i].mGameVariables.life == 5"+" mGameVariables.rapidBallCoordinate.x :"+mGameVariables.rapidBallCoordinate.x+" (mGameVariables.barCoordinates[i].x+ 40):"+(mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x+ 40)+" (mGameVariables.barCoordinates[i].x+50): "+(mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x+50) );
				//if(mGameVariables.rapidBallCoordinate.x >= (mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x+ 40) && mGameVariables.rapidBallCoordinate.x <= (mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x+50)){
				if(mGameVariables.rapidBallCoordinate.x > (mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x+ 35) && mGameVariables.rapidBallCoordinate.x <= (mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].x+50)){
					if(mGameVariables.life < 3) //mGameVariables.MAX_LIFE
						mGameVariables.life++;
					mGameVariables.barCoordinates[mGameVariables.rapidBallCoordinate.cord_id].life++;
					Log.d("NewLIfe", "mGameVariables.life: "+mGameVariables.life+"");
				}							
			}
		/************* LIFE CHECKING OF GAME **********************/
		if( ( mGameVariables.rapidBallCoordinate.y + mGameVariables.ball_diameter ) > ( mGameVariables.surface_height - mGameVariables.ball_diameter ) ){ // SCORE <= 40
			newLifeState(); // ball crossed the upper boundary limit and new game starts
		}
		else if(mGameVariables.rapidBallCoordinate.y <= 0 ){
			newLifeState(); // ball crossed the lower boundary limit and new game starts
		}
		//Log.d("Ball's Position", "mGameVariables.rapidBallCoordinate.x: "+mGameVariables.rapidBallCoordinate.x+" mGameVariables.rapidBallCoordinate.y: "+mGameVariables.rapidBallCoordinate.y+" spentTime: "+timeSpent);              
		/**********************************************************************************************************************/           

		/************************************* BALL *******************************************************/
	}	
	/**	newLifeState() actually decreases life starting  a new life and is called when: 
	 * 1. if ball falls on a red bar in the for loop of checking of ball falling 
	 * 2. if the ball crosses the upper boundary limit
	 * 3. if the ball crosses the lower boundary limit	 
	 */
	private void newLifeState(){
		boolean allRed = true;
		if(mGameVariables.life > 0){
			mGameVariables.life--;
			//if(){
			Log.d("NewLifeState()", "Life :"+mGameVariables.life);
			// checking if all are red
			for(int i = 0 ; i < 5 ; i++){
				if(mGameVariables.barCoordinates[i].type != 2 ){
					allRed = false;
					break;
				}
			}
			// on which bar the newly created ball will lie
			int i =	mGameVariables.newly_created_bar_id; 
			while(!allRed){
				if( i == 5)
					i = 0;
				if( mGameVariables.barCoordinates[i].type != 2 ){
					mGameVariables.newly_created_bar_id = i ;
					break;
				}
				i++;
			}
			/*for( ; mGameVariables.barCoordinates[mGameVariables.newly_created_bar_id].type == 2; mGameVariables.newly_created_bar_id++){
				//Log.d("ARRAY INDEX OUT OF BOUND: ","mGameVariables.newly_created_bar_id :"+mGameVariables.newly_created_bar_id);
				if(mGameVariables.newly_created_bar_id == mGameVariables.total_bar_on_screen){
					mGameVariables.newly_created_bar_id=0;
					Log.d("IF(BAR_ID == total_bar_on_screen )","mGameVariables.newly_created_bar_id :"+mGameVariables.newly_created_bar_id);
				}
			}*/
			Log.d("newly_created_bar_id","mGameVariables.newly_created_bar_id :"+mGameVariables.newly_created_bar_id);

			mGameVariables.rapidBallCoordinate.x= mGameVariables.barCoordinates[mGameVariables.newly_created_bar_id].x + 40;
			mGameVariables.rapidBallCoordinate.y = mGameVariables.barCoordinates[mGameVariables.newly_created_bar_id].y + 10;
			mGameVariables.rapidBallCoordinate.radius = 10;
			mGameVariables.rapidBallCoordinate.onBar = true;
			mGameVariables.rapidBallCoordinate.left = false;
			mGameVariables.rapidBallCoordinate.right = false;
			mGameVariables.rapidBallCoordinate.cord_id = mGameVariables.newly_created_bar_id;	
		}
		else{
			mGameVariables.GAME_OVER = true;
		}// instead of this this is done for the time being
		/*if(mGameVariables.life == 0)
			mGameVariables.GAME_OVER = true;		*/
	}
	/*************************************************************************************************************************/
}
