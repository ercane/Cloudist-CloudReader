package mree.cloud.music.player.app.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * Created by eercan on 09.11.2016.
 */

public class ShadowImage extends BitmapDrawable {

    static float shadowRadius = 4f;
    static PointF shadowDirection = new PointF(2f, 2f);
    Bitmap bm;
    int fillColor = 0;

    public ShadowImage(Resources res, Bitmap bitmap) {
        super(res, Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() + shadowRadius *
                        shadowDirection.x), (int) (bitmap.getHeight() + shadowRadius *
                shadowDirection.y)
                , false));
        this.bm = bitmap;
    }

    public ShadowImage(Resources res, Bitmap bitmap, int fillColor) {
        super(res, Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() + shadowRadius *
                        shadowDirection.x), (int) (bitmap.getHeight() + shadowRadius *
                shadowDirection.y)
                , false));
        this.bm = bitmap;
        this.fillColor = fillColor;
    }

    @Override
    public void draw(Canvas canvas) {

        Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
        Log.e("TEST", rect.toString());
        setBounds(rect);

        Paint mShadow = new Paint();
        mShadow.setAntiAlias(true);
        mShadow.setShadowLayer(shadowRadius, shadowDirection.x, shadowDirection.y, Color.BLACK);

        canvas.drawRect(rect, mShadow);
        if (fillColor != 0) {
            Paint mFill = new Paint();
            mFill.setColor(fillColor);
            canvas.drawRect(rect, mFill);
        }
        canvas.drawBitmap(bm, 0.0f, 0.0f, null);

    }

}