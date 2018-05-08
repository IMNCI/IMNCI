package org.ministryofhealth.newimci.helper;

import android.content.Context;
import android.view.ScaleGestureDetector;
import android.widget.Toast;

/**
 * Created by chriz on 1/15/2018.
 */

public class MyOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    Context mContext;
    public MyOnScaleGestureListener(Context context){
        mContext = context;
    }
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }
}
