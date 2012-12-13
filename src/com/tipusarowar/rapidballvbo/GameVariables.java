package com.tipusarowar.rapidballvbo;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;

public class GameVariables {
	/***************** LOG *******************/
	private boolean LOG_SHOW = true;
	private String LOG_TAG ="";
	/***************** PUBLIC *****************/	
	public Context mContext;
	public DisplayMetrics mDisplayMetrics;	

	public int imageWidth;           // original bitmap width
	public int imageHeight;          // original bitmap height
	public int surface_height;
	public int surface_width;

	public PinkTexture pinkTexture;
	public RedTexture redTexture;
	public BackgroundTexture backgroundTexture;
	public RapidBallTexture rapidBallTexture;
	public GameOverTexture gameOverTexture;

	public boolean GAME_OVER;	

	public MediaPlayer mediaPlayer;
	/****************************************/	
	public int total_bar_on_screen;
	public int barCoordinates_identity_type;
	public int newly_created_bar_id;
	public int ball_diameter;
	public int life_controlling_variable;

	public float velocity_on_X;
	public float velocity_on_Y;
	public int limit_on_X;
	public int limit_on_Y;		
	public int decrease_y_coordinate_by_this = 2;
	public int increase_y_coordinate_by_this = 2;
	public int gx;
	public int gy;	
	public float acceleration_on_Y;	
	public float trajectory_X_Velocity;

	public double time;

	public RapidBarCoordinate[] barCoordinates;
	public RapidBallCoordinate rapidBallCoordinate;
	/****************************************/

	public int score;
	public int life;

	/******************** FINAL ************************/
	// desired fps
	public final static int    MAX_FPS = 20; // 50
	// maximum number of frames to be skipped
	public final static int    MAX_FRAME_SKIPS = 5;
	// the frame period
	public final static int    FRAME_PERIOD = ( 1000 / MAX_FPS );  
	/***************************************************/
	long beginTime;     // the time when the cycle begun
	long timeDiff;      // the time it took for the cycle to execute
	int sleepTime;      // ms to sleep (<0 if we're behind)
	int framesSkipped;  // number of frames being skipped 
	/************************************ VBO VARIABLES **************************************/
	public GLSprite[] spriteArray;
	public GLSprite[] digitArray;
	public GLSprite[] lifeArray;

	public final static int SPRITE_WIDTH = 100; // 64;
	public final static int SPRITE_HEIGHT = 10; //  64;
	public final static int BALL_SPRITE_WIDTH = 20;
	public final static int BALL_SPRITE_HEIGHT = 20;
	public final static int DIGIT_SPRITE_WIDTH = 32;
	public final static int DIGIT_SPRITE_HEIGHT = 32;
	public final static int LIFE_SPRITE_WIDTH = 20 ;
	public final static int LIFE_SPRITE_HEIGHT = 32 ;
	
	/*public final static int LIFE_X = ;
	public final static int LIFE_Y = ;
	public final static int DIGIT_X = ;
	public final static int DIGIT_Y = ;
	*/
	public int DIGIT_COUNT = 1;
	public final static int TOTAL_DIGIT = 10;
	public final static int TOTAL_NO_OF_SPRITE = 5 ;

	public GameVariables(Context context,DisplayMetrics displayMetrics){
		mContext = context;
		mDisplayMetrics = displayMetrics;

		surface_height = displayMetrics.heightPixels;
		surface_width = displayMetrics.widthPixels;

		GAME_OVER = false;
		/*****************************/
		total_bar_on_screen = 5;//9;//-1;
		barCoordinates_identity_type = 3;		
		newly_created_bar_id = 2;
		ball_diameter = 20;
		life_controlling_variable = 10;

		gx = 2;//6
		gy = 2;//4

		time = 0.035 ;

		score = 0;
		life = 2;
		/***********************************************************************************************/
		/***********************************************************************************************/
		/***********************************************************************************************/
		barCoordinates = new RapidBarCoordinate[total_bar_on_screen];//Allocation		
		//initialization
		for(int i=0 ; i<total_bar_on_screen ; i++){
			barCoordinates[i] =(RapidBarCoordinate) new RapidBarCoordinate();
		}
		if(LOG_SHOW){
			Log.d("Y Coordinate Check", "surface_height :"+surface_height+" surface_width :"+surface_width);
		}

		int count=surface_height;		
		int count_decrease = (int) (surface_height / 10);
		count-=count_decrease;

		int dCount = (int) (surface_height / 10);
		dCount += dCount;

		int fromBelow = 0;

		if(LOG_SHOW){
			Log.d("Variable Check", "count_decrease :"+count_decrease+" initial count :"+count);		
		}
		for(int i=0 ; i<total_bar_on_screen ; i++){
			barCoordinates[i].x = (int) new Random().nextInt((surface_width-100));//440-100
			barCoordinates[i].y = fromBelow;//count;
			//also have to set type
			barCoordinates[i].type = (int) new Random().nextInt(barCoordinates_identity_type);
			barCoordinates[i].life = 0;
			if(LOG_SHOW){
				Log.d("Loop Variable", "i :"+i+"fromBelow :"+fromBelow);		
			}

			count-=count_decrease;
			fromBelow += dCount;

			/*if(LOG_SHOW){
				Log.d("Loop Variable Check:Last", "i :"+i+"  count :"+count +" total_bar_on_screen :"+total_bar_on_screen);	
			}*/
		}	
		/*********************************************************/

		rapidBallCoordinate = new RapidBallCoordinate();

		rapidBallCoordinate.x= barCoordinates[2].x+40;//5].x+35;
		//ball.y = cords[5].y;//+20;
		rapidBallCoordinate.y = barCoordinates[2].y+10;;//5].y+10;
		rapidBallCoordinate.radius = 10;
		rapidBallCoordinate.onBar = true;
		rapidBallCoordinate.right = false;
		rapidBallCoordinate.left = false;
		rapidBallCoordinate.cord_id = 2;//5;
		if(barCoordinates[2].type == 2)
			barCoordinates[2].type++;
		//GAME_OVER = false;
		/**********************************/

		/*************      GLSPRITE AND GRID ARE SET HERE       ***************************/
		spriteArray = new GLSprite[TOTAL_NO_OF_SPRITE] ;//4];

		GLSprite background = new GLSprite(R.drawable.rapid_wall );//_space);//background);
		BitmapDrawable backgroundImage = (BitmapDrawable)mContext.getResources().getDrawable(R.drawable.rapid_space);//background);
		Bitmap backgoundBitmap = backgroundImage.getBitmap();
		background.width = backgoundBitmap.getWidth();
		background.height = backgoundBitmap.getHeight();
		// Setup the background grid.  This is just a quad.
		Grid backgroundGrid = new Grid(2, 2, false);
		backgroundGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f, 1.0f, null);
		backgroundGrid.set(1, 0, background.width, 0.0f, 0.0f, 1.0f, 1.0f, null);
		backgroundGrid.set(0, 1, 0.0f, background.height, 0.0f, 0.0f, 0.0f, null);
		backgroundGrid.set(1, 1, background.width, background.height, 0.0f,1.0f, 0.0f, null );
		background.setGrid(backgroundGrid);
		spriteArray[0] = background;

		Grid spriteGrid = null;
		// Setup a quad for the sprites to use.  All sprites will use the
		// same sprite grid intance.
		spriteGrid = new Grid(2, 2, false);
		spriteGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		spriteGrid.set(1, 0, SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		spriteGrid.set(0, 1, 0.0f, SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		spriteGrid.set(1, 1, SPRITE_WIDTH, SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);

		GLSprite robot = new GLSprite(R.drawable.pink_bar);
		robot.width = SPRITE_WIDTH;
		robot.height = SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[1] = robot;

		robot = new GLSprite(R.drawable.red_bar);
		robot.width = SPRITE_WIDTH;
		robot.height = SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[2] = robot;

		spriteGrid = new Grid(2, 2, false);
		spriteGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		spriteGrid.set(1, 0, BALL_SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		spriteGrid.set(0, 1, 0.0f, BALL_SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		spriteGrid.set(1, 1, BALL_SPRITE_WIDTH, BALL_SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);

		robot = new GLSprite(R.drawable.rapid_ball);
		robot.width = BALL_SPRITE_WIDTH;
		robot.height = BALL_SPRITE_HEIGHT;
		robot.setGrid(spriteGrid);
		spriteArray[3] = robot ;


		GLSprite gameOver = new GLSprite(R.drawable.game_over_01);//background);
		BitmapDrawable gameOverImage = (BitmapDrawable)mContext.getResources().getDrawable(R.drawable.rapid_space);//background);
		Bitmap gameOverBitmap = gameOverImage.getBitmap();
		background.width = gameOverBitmap.getWidth();
		background.height = gameOverBitmap.getHeight();
		// Setup the background grid.  This is just a quad.
		Grid gameOverGrid = new Grid(2, 2, false);
		gameOverGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f, 1.0f, null);
		gameOverGrid.set(1, 0, background.width, 0.0f, 0.0f, 1.0f, 1.0f, null);
		gameOverGrid.set(0, 1, 0.0f, background.height, 0.0f, 0.0f, 0.0f, null);
		gameOverGrid.set(1, 1, background.width, background.height, 0.0f,1.0f, 0.0f, null );
		gameOver.setGrid(gameOverGrid);
		spriteArray[4] = gameOver;

		/*************      GLSPRITE AND GRID ARE SET HERE       ***************************/
		/********************************* SCORE SPRITE ************************************/

		digitArray = new GLSprite[TOTAL_DIGIT];

		Grid digitGrid = null;
		// Setup a quad for the sprites to use.  All sprites will use the
		// same sprite grid intance.
		digitGrid = new Grid(2, 2, false);
		digitGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		digitGrid.set(1, 0, DIGIT_SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		digitGrid.set(0, 1, 0.0f, DIGIT_SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		digitGrid.set(1, 1, DIGIT_SPRITE_WIDTH, DIGIT_SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);

		GLSprite digit = new GLSprite(R.drawable.zero);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[0] = digit;
		
		digit = new GLSprite(R.drawable.one);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[1] = digit;
		
		digit = new GLSprite(R.drawable.two);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[2] = digit;
		
		digit = new GLSprite(R.drawable.three);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[3] = digit;
		
		digit = new GLSprite(R.drawable.four);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[4] = digit;
		
		digit = new GLSprite(R.drawable.five);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[5] = digit;
		
		digit = new GLSprite(R.drawable.six);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[6] = digit;
		
		digit = new GLSprite(R.drawable.seven);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[7] = digit;
		
		digit = new GLSprite(R.drawable.eight);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[8] = digit;
		
		digit = new GLSprite(R.drawable.nine);
		digit.width = SPRITE_WIDTH;
		digit.height = SPRITE_HEIGHT;
		digit.setGrid(spriteGrid);
		digitArray[9] = digit;
		
		/***************************************** LIFE SPRITE ***********************************/
		
		lifeArray = new GLSprite[1];//TOTAL_DIGIT];

		Grid lifeGrid = null;
		// Setup a quad for the sprites to use.  All sprites will use the
		// same sprite grid intance.
		lifeGrid = new Grid(2, 2, false);
		lifeGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		lifeGrid.set(1, 0, LIFE_SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		lifeGrid.set(0, 1, 0.0f, LIFE_SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		lifeGrid.set(1, 1, LIFE_SPRITE_WIDTH, LIFE_SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);

		GLSprite life = new GLSprite(R.drawable.life);
		life.width = LIFE_SPRITE_WIDTH;
		life.height = LIFE_SPRITE_HEIGHT;
		life.setGrid(lifeGrid);
		lifeArray[0] = life;
		
	}

	public void onSurfaceChanged(int width,int height){
		surface_height = height;
		surface_width = width;

		int dCount = (int) (surface_height / 10);
		dCount += dCount;

		int fromBelow = 0;	

		for(int i=0 ; i<total_bar_on_screen ; i++){
			barCoordinates[i].x = (int) new Random().nextInt((surface_width-100));//440-100
			barCoordinates[i].y = fromBelow;//count;
			//also have to set type
			barCoordinates[i].type = (int) new Random().nextInt(barCoordinates_identity_type);
			barCoordinates[i].life = 0;
			if(LOG_SHOW){
				Log.d("Loop Variable", "i :"+i+"fromBelow :"+fromBelow);		
			}
			fromBelow += dCount;
			/*if(LOG_SHOW){
				Log.d("Loop Variable Check:Last", "i :"+i+"  count :"+count +" total_bar_on_screen :"+total_bar_on_screen);	
			}*/
		}
		rapidBallCoordinate.x= barCoordinates[rapidBallCoordinate.cord_id].x+40;//5].x+35;
		//ball.y = cords[5].y;//+20;
		rapidBallCoordinate.y = barCoordinates[rapidBallCoordinate.cord_id].y+10;;//5].y+10;
	}
}
