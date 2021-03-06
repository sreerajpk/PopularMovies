package com.sreeraj.popularmovies;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ItemDecoration for Recylcerview with grid of Movies.
 */
public class PMSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public PMSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }

        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = space;
        } else {
            outRect.left = 0;
        }
    }
}
