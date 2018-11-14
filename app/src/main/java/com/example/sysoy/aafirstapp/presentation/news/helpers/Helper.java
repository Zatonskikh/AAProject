package com.example.sysoy.aafirstapp.presentation.news.helpers;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Helper {

    public static  String dateFormatter(@NonNull String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
        try {
            return DateFormat.getDateInstance().format(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            return "";
        }
    }
}
