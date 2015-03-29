/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.aspsine.zhihu.daily.ui.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Can display bitmap with rounded corners. This implementation works only with
 * ImageViews wrapped in ImageViewAware. <br />
 * This implementation is inspired by <a href=
 * "http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/"
 * > Romain Guy's article</a>. It rounds images using custom drawable drawing.
 * Original bitmap isn't changed. <br />
 * <br />
 * If this implementation doesn't meet your needs then consider <a
 * href="https://github.com/vinc3m1/RoundedImageView">this project</a> for
 * usage.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class CircleBitmapDisplayer implements BitmapDisplayer {

    protected final int borderWidth;
    protected final int borderColor;

    public CircleBitmapDisplayer() {
        this(0);
    }

    public CircleBitmapDisplayer(int borderWidthPixels) {
        this(borderWidthPixels, 0);
    }

    public CircleBitmapDisplayer(int borderWidthPixels, int borderColor) {
        this.borderWidth = borderWidthPixels;
        this.borderColor = borderColor;
    }


    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, borderWidth, borderColor));
    }

    public class CircleDrawable extends Drawable {
        public static final String TAG = "CircleDrawable";

        protected final Paint mPaint;
        protected final Paint mBorderPaint;

        private Shader mBitmapShader;

        private final Matrix mShaderMatrix;

        private final RectF mDrawableRect;
        private final RectF mBorderRect;

        private int mBitmapWidth;
        private int mBitmapHeight;

        private float mDrawableRadius;
        private float mBorderRadius;

        protected final int mBorderColor;
        protected final int mBorderWidth;

        protected Bitmap oBitmap;// 原图

        public CircleDrawable(Bitmap bitmap) {
            this(bitmap, 0);
        }

        public CircleDrawable(Bitmap bitmap, int borderWidthPixels) {
            this(bitmap, borderWidthPixels, Color.BLACK);
        }

        public CircleDrawable(Bitmap bitmap, int borderWidthPixels, int borderColor) {
            this.mBorderWidth = borderWidthPixels;
            this.mBorderColor = borderColor;
            this.oBitmap = bitmap;

            this.mBorderRect = new RectF();
            this.mDrawableRect = new RectF();

            this.mShaderMatrix = new Matrix();

            mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            mBorderPaint = new Paint();
            mBorderPaint.setColor(mBorderColor);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setShader(mBitmapShader);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            setup();
        }

        @Override
        public void draw(Canvas canvas) {

            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mPaint);
            if (mBorderWidth != 0) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
            }

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        private int getWidth() {
            Rect bounds = getBounds();
            return bounds.width();
        }

        private int getHeight() {
            Rect bounds = getBounds();
            return bounds.height();
        }

        private void setup() {

            if (oBitmap == null) {
                return;
            }
            mPaint.setAntiAlias(true);
            mPaint.setShader(mBitmapShader);

            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStrokeWidth(mBorderWidth);

            mBitmapHeight = oBitmap.getHeight();
            mBitmapWidth = oBitmap.getWidth();

            mBorderRect.set(0, 0, getWidth(), getHeight());
            mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

            mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
            mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

            updateShaderMatrix();
        }

        private void updateShaderMatrix() {
            float scale;
            float dx = 0;
            float dy = 0;

            mShaderMatrix.set(null);

            if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
                scale = mDrawableRect.height() / (float) mBitmapHeight;
                dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
            } else {
                scale = mDrawableRect.width() / (float) mBitmapWidth;
                dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
            }

            mShaderMatrix.setScale(scale, scale);
            mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

            mBitmapShader.setLocalMatrix(mShaderMatrix);
        }

    }
}
