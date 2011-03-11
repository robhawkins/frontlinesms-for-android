package net.frontlinesms.android.util;

import android.graphics.*;
import android.graphics.drawable.Drawable;

public class DropShadowDrawable extends Drawable {

    Bitmap originalBitmap;

    /**
     * Constructor.
     * @param originalBitmap Original bitmap without drop shadow
     */
    public DropShadowDrawable(Bitmap originalBitmap) {
        this.originalBitmap = originalBitmap;
    }

    @Override
    public void draw(Canvas c) {
        BlurMaskFilter blurFilter = new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER);
        Paint shadowPaint = new Paint();
        shadowPaint.setMaskFilter(blurFilter);

        int[] offsetXY = new int[2];
        Bitmap shadowImage = originalBitmap.extractAlpha(shadowPaint, offsetXY);

        c = new Canvas(shadowImage);
        c.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
