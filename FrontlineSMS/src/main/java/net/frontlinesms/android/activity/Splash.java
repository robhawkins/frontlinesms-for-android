/**
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2010, Meta Healthcare Systems Ltd.
 *
 * This file is part of FrontlineSMS for Android.
 *
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import net.frontlinesms.android.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Splash screen to be displayed at application launch.
 * User can skip the splash screen by tapping the screen.
 * Otherwise the app continues after 5 seconds.
 *
 * @author Mathias Lin <mathias.lin@metahealthcare.com>
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

    /**
     * Handles user's touch and proceeds to the
     * main menu / home screen.
     * @param event Touch event
     * @return true
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		skip(null);
		return true;
	}

    /**
     * Handle key events. Finish the app when user pressed back,
     * otherwise proceed to main menu screen.
     * @param keyCode Key code
     * @param event Key event
     * @return true if event has been handled
     */
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
        startActivity(new Intent(this, Dashboard.class));
        finish();
    }

}
