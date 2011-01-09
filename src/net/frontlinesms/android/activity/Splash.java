/**
 * 
 */
package net.frontlinesms.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import net.frontlinesms.android.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author aga, Mathias Lin <mathias.lin@metahealthcare.com>
 *
 */
public class Splash extends BaseActivity {
	protected static final long SPLASH_TIME = 5000;

    Timer mTimer = new Timer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                skip(null);
            }
        }, SPLASH_TIME);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		skip(null);
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            mTimer.cancel();
            finish();
        } else {
            skip(null);
        }
		return true;
	}

    /**
     * Skips the splash screen, i.e. when user touches the
     * screen.
     * @param v View touched (splash image)
     */
    public void skip(View v) {
        mTimer.cancel();
        startActivity(new Intent(this, Home.class));
        finish();
    }

}
