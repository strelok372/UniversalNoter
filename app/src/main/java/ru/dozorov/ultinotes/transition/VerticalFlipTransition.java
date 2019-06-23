package ru.dozorov.ultinotes.transition;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class VerticalFlipTransition implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setCameraDistance(10000);
        if (position < -0.5){ //(-inf : -0.5]
            page.setAlpha(0);
        }else if(position <= 0){ //[-0.5 : 0]
            page.setAlpha(1+position*2);
//            page.setPivotX(page.getWidth());
            page.setScaleY(Math.max(0.85f, 1-Math.abs(position)/3));
            page.setRotationY(40*Math.abs(position));
        }else if(position <= 0.5){ //[0 : 0.5]
            page.setAlpha(1-position*2);
//            page.setPivotX(0);
            page.setScaleY(Math.max(0.85f, 1-Math.abs(position)/3));
            page.setRotationY(-40*Math.abs(position));
        }else { //[0.5 : inf)
            page.setAlpha(0);
        }
    }
}
