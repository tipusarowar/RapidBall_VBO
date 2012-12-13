package com.tipusarowar.rapidballvbo;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainMenuActivity extends Activity {

	/**************************************************/
	private boolean mJustCreated;

	private View mStartButton;
	private View mOptionsButton;
	private View mBackground;

	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;
	private Animation mFadeInAnimation;

	/**************************************************/
	// Create an anonymous implementation of OnClickListener

	
	private View.OnClickListener sStartButtonListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(getBaseContext(),RapidBallActivity_GL.class); // OpenGLTestActivity.class); //RapidBallActivity_GL.class);
			v.startAnimation(mButtonFlickerAnimation);
			mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));			
		}
	};

	/**************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		

		mStartButton = findViewById(R.id.startButton);
		mBackground = findViewById(R.id.mainMenuBackground);

		mStartButton.setOnClickListener(sStartButtonListener);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

		mJustCreated = true;

		// Keep the volume control type consistent across all activities.
		//setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

		/*        MediaPlayer mp = MediaPlayer.create(this, R.raw.bwv_115);
	        mp.start();
		 */
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume(){
		super.onResume();
		if (mJustCreated) {
			if (mStartButton != null) {
				mStartButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));
			}
			if (mOptionsButton != null) {
				Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
				anim.setStartOffset(1000L);
				mOptionsButton.startAnimation(anim);
			}
			//mJustCreated = false;

		} else {
			mStartButton.clearAnimation();
			mOptionsButton.clearAnimation();
		}
	}
	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}


		public void onAnimationEnd(Animation animation) {

			//startActivity(mIntent);    
			startActivityForResult(mIntent, 100);
		}
		

		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("MainMenuonActivityResult", "Ended succesfully");
		//Intent i = new Intent(getBaseContext(), GameOverTest.class);
		//startActivity(i);
	}

}
