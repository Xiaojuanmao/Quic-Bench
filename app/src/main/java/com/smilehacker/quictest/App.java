package com.smilehacker.quictest;

import android.app.Application;


/**
 * Created by quan.zhou on 2017/8/5.
 */

public class App {
    public static Application INSTANCE ;//= AppGlobals.getInitialApplication();
    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
            }
        } finally {
            INSTANCE = app;
        }
    }
}
