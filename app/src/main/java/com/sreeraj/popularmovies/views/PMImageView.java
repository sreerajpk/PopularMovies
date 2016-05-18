package com.sreeraj.popularmovies.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sreeraj.popularmovies.R;

/**
 * The Aspect Ratio adjusted image view.
 */
public class PMImageView extends ImageView {

    private float aspectRatio = 0;
    @Nullable
    private AspectRatioSource aspectRatioSource = null;

    /**
     * Instantiates a new CA Image view.
     *
     * @param context the context
     */
    public PMImageView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Ca image view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public PMImageView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PMImageView);
        aspectRatio = a.getFloat(R.styleable.PMImageView_imageAspectRatio, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        float localRatio = aspectRatio;

        if (localRatio == 0.0 && aspectRatioSource != null
                && aspectRatioSource.getHeight() > 0) {
            localRatio =
                    (float) aspectRatioSource.getWidth()
                            / (float) aspectRatioSource.getHeight();
        }

        if (localRatio == 0.0) {
            super.onMeasure(widthSpec, heightSpec);
        } else {
            int lockedWidth = MeasureSpec.getSize(widthSpec);
            int lockedHeight = MeasureSpec.getSize(heightSpec);

            if (lockedWidth == 0 && lockedHeight == 0) {
                throw new IllegalArgumentException(
                        "Both width and height cannot be zero -- watch out for scrollable containers");
            }

            // Get the padding of the border background.
            int hPadding = getPaddingLeft() + getPaddingRight();
            int vPadding = getPaddingTop() + getPaddingBottom();

            // Resize the preview frame with correct aspect ratio.
            lockedWidth -= hPadding;
            lockedHeight -= vPadding;

            if (lockedHeight > 0 && (lockedWidth > lockedHeight * localRatio)) {
                lockedWidth = (int) (lockedHeight * localRatio + .5);
            } else {
                lockedHeight = (int) (lockedWidth / localRatio + .5);
            }

            // Add the padding of the border.
            lockedWidth += hPadding;
            lockedHeight += vPadding;

            // Ask children to follow the new preview dimension.
            super.onMeasure(MeasureSpec.makeMeasureSpec(lockedWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(lockedHeight, MeasureSpec.EXACTLY));
        }
    }

    /**
     * Sets aspect ratio source.
     *
     * @param v the v
     */
    public void setAspectRatioSource(View v) {
        this.aspectRatioSource = new ViewAspectRatioSource(v);
    }

    /**
     * Sets aspect ratio source.
     *
     * @param aspectRatioSource the aspect ratio source
     */
    public void setAspectRatioSource(AspectRatioSource aspectRatioSource) {
        this.aspectRatioSource = aspectRatioSource;
    }

    // from com.android.camera.PreviewFrameLayout, with slight
    // modifications

    /**
     * Sets aspect ratio.
     *
     * @param aspectRatio the aspect ratio
     */
    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio <= 0.0) {
            throw new IllegalArgumentException(
                    "aspect ratio must be positive");
        }

        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            requestLayout();
        }
    }

    /**
     * The interface Aspect ratio source.
     */
    public interface AspectRatioSource {
        /**
         * Gets width.
         *
         * @return the width
         */
        int getWidth();

        /**
         * Gets height.
         *
         * @return the height
         */
        int getHeight();
    }

    private static class ViewAspectRatioSource implements
            AspectRatioSource {
        @Nullable
        private View v = null;

        /**
         * Instantiates a new View aspect ratio source.
         *
         * @param v the v
         */
        ViewAspectRatioSource(View v) {
            this.v = v;
        }

        @Override
        public int getWidth() {
            return (v.getWidth());
        }

        @Override
        public int getHeight() {
            return (v.getHeight());
        }
    }

}
