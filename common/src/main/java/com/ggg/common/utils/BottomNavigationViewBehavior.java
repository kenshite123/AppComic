package com.ggg.common.utils;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Created by Srijith on 22-12-2016.
 */

public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    private int height;

    public interface BottomNavigationChange {
        public void slideUp();

        public void slideDown();
    }

    BottomNavigationChange delegate;

    public void setDelegate(BottomNavigationChange delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }

    public void slideUp(BottomNavigationView child) {
        if (delegate != null) {
            delegate.slideUp();
        }
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200);
    }

    public void slideDown(BottomNavigationView child) {
        if (delegate != null) {
            delegate.slideDown();
        }
        child.clearAnimation();
        child.animate().translationY(height).setDuration(200);
    }


}
