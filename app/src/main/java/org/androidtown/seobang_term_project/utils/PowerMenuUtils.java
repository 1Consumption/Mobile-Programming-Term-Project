package org.androidtown.seobang_term_project.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.androidtown.seobang_term_project.R;

/**
 * @When:
 * This class is called when the user clicks the power menu from the MainActivity tool bar
 *
 * @functions:
 * This shows menus which contains 튜토리얼, 일치도 설정, 요리 사진 찍기
 *
 * @technique:
 * By utilizing open source libraries, we made good design for users' convenient
 */


public class PowerMenuUtils {
    public static PowerMenu getHamburgerPowerMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener) {
        return new PowerMenu.Builder(context)
                .addItem(new PowerMenuItem("튜토리얼", false))
                .addItem(new PowerMenuItem("일치도 설정", false))
                .addItem(new PowerMenuItem("요리 사진 찍기", false))
                .setLifecycleOwner(lifecycleOwner)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(Color.WHITE)
                .setMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSelectedEffect(false)
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
    }
}
