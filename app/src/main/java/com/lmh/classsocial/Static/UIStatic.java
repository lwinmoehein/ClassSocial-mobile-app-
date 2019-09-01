package com.lmh.classsocial.Static;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by E on 6/17/2018.
 */

public class UIStatic {
    public static void showSnack(Window window, String message,String type){
        Toast toast=Toast.makeText(window.getContext(),message,Toast.LENGTH_SHORT);

        toast.show();
    }
}
