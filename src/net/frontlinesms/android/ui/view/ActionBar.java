package net.frontlinesms.android.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import net.frontlinesms.android.R;
import net.frontlinesms.android.activity.Dashboard;

/**
 * The Action bar gives your users onscreen access to the most frequently used
 * actions in your application. We recommend you use this pattern if you want
 * to dedicate screen real estate for common actions. Using this pattern
 * replaces the title bar. It works with the Dashboard, as the upper left
 * portion of the Action bar is where we recommend you place a quick link back
 * to the dashboard or other app home screen.
 *
 * @author Mathias Lin
 */
public class ActionBar extends RelativeLayout implements View.OnClickListener {

    private static final int MAX_BUTTONS = 3;

    private Context mCtx;
    private ProgressBar mProgressBar;
    private ImageView mImgLogo;
    private final ImageButton[] mBtns = new ImageButton[MAX_BUTTONS];
    private Handler handler;
    

    /**
     * Constructor used when defining the ActionBar via xml layout
     * @param context Activity that holds the ActionBar
     * @param attrs Attributes taken from the xml layout
     */
    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.actionbar, this, true);
        init(attrs);
    }

    /**
     * Constructor used when defining the ActionBar programmatically
     * @param context Activity that holds the ActionBar
     * @param text Text label (non-clickable)
     * @param buttons List of button resource id's
     */
    public ActionBar(Context context, String text, int... buttons) {
        super(context, null);
        mCtx = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.actionbar, this, true);
        init(text, buttons);
    }

    /**
     * Constructor used when defining the ActionBar programmatically
     * @param context Activity that holds the ActionBar      
     * @param buttons List of button resource id's
     */
    public ActionBar(Context context, int... buttons) {
        super(context, null);
        mCtx = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.actionbar, this, true);
        init(null, buttons);
    }

    /**
     * Initialize the components, set click listener, etc.
     * Used when creating the ActionBar in xml layout.
     */
    public void init(AttributeSet attrs) {
        if (mImgLogo==null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ActionBar);
            int[] res = new int[]{
                    a.getResourceId(R.styleable.ActionBar_button1, -1),
                    a.getResourceId(R.styleable.ActionBar_button2, -1),
                    a.getResourceId(R.styleable.ActionBar_button3, -1)
            };
            init(a.getString(R.styleable.ActionBar_text),res);
        }
    }
    
    /**
     * Initialize the components, set click listener, etc.
     * Used when creating the ActionBar programmatically.
     */
    private void init(String text, int... buttons) {
        if (mImgLogo==null) {
            mImgLogo = (ImageView) findViewById(R.id.actionbar_logo);
            if (mImgLogo!=null) mImgLogo.setOnClickListener(this);
            mProgressBar = (ProgressBar) findViewById(R.id.actionbar_progress);
             //mProgressBar = new ProgressBar(mCtx, null, android.R.attr.progressBarStyleSmall);

            if (text!=null) {
                findViewById(R.id.actionbar_text).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.actionbar_text)).setText(text);
            }

            for (int i=1;i<=MAX_BUTTONS;i++) {
                Integer btnId = getResources().getIdentifier("actionbar_button_" + i,"id",mCtx.getPackageName());                
                mBtns[i-1] = (ImageButton) findViewById(btnId);
                if (mBtns[i-1]!=null) {
                    mBtns[i-1].setOnClickListener(this);                    
                    if (i>buttons.length||buttons[i-1]==-1) {
                        mBtns[i-1].setVisibility(View.GONE);
                        findViewById(
                            getResources().getIdentifier("actionbar_seperator_" + i,"id", mCtx.getPackageName()) ).
                                setVisibility(View.GONE);
                    } else {
                        ((ImageButton)mBtns[i-1]).setImageResource(buttons[i-1]);
                        ((ImageButton)mBtns[i-1]).setVisibility(View.VISIBLE);
                    }

                }
            }
        }
    }



    /**
     * Set background image for a button
     * @param i Button index, starting at 1 (not 0!)
     * @param resId Resource id of the image to be assigned
     */
    public void setBackgroundResId(int i, int resId) {
        mBtns[i-1].setBackgroundResource(resId);
    }


    /**
     * Handles click on button or logo, passes view id as message to handler
     * @param v View that has been clicked
     */
    public void onClick(View v) {
        if (handler==null) handler = new SampleOnClickHandler();
        Message.obtain(handler,v.getId()).sendToTarget();
    }

    /**
     * Sets the click handler for logo and buttons
     * @param handler Click handler
     */
    public void setOnClickHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Returns the progress indicator
     * @return progress indicator
     */
    public ProgressBar getProgressIndicator() {
        return mProgressBar;
    }

    /**
     * Sample click handler
     */
    public final class SampleOnClickHandler extends Handler {

        /**
         * Handles the message
         * @param msg msg.what contains the view id of the clicked view (button or logo)
         */
        @Override
        public void handleMessage(final Message msg) {
            String s = "";
            switch (msg.what) {
                case R.id.actionbar_logo:
                    s = "Logo";
                    mCtx.startActivity(new Intent(mCtx, Dashboard.class));
                    break;
                case R.id.actionbar_button_1:
                    ((Activity)mCtx).onSearchRequested();
                    break;
            }
        }
    }

}
