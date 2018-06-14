package com.android.pokemonillustratedhandbook.app;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * application
 */

public class APP extends Application {

    private static APP INStANCE;

    public APP() {
        INStANCE = this;
    }

    public static APP getInstance() {
        if (INStANCE == null) {
            synchronized (APP.class) {
                if (INStANCE == null) {
                    INStANCE = new APP();
                }
            }
        }
        return INStANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CrashReport.initCrashReport(getApplicationContext(), "01e2104b9c", false);
    }
}
