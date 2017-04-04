package com.vivi.viviarchillecttv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends Activity {

    private Tracker mTracker;
    public ArchillectApplication application;

    public WebView backgroundView;

    String url;

    Context context;

    public static boolean Donezo = false;

    PowerManager.WakeLock wakeLock;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the shared Tracker instance.
        application = (ArchillectApplication) getApplication();

        mTracker = application.getDefaultTracker();
        mTracker.enableExceptionReporting(true);
        mTracker.enableAutoActivityTracking(true);
        mTracker.setScreenName("Main Screen");

        backgroundView = (WebView) findViewById(R.id.backgroundview);

        backgroundView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        context = this;

        //TODO: Grab WakeLock
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();



        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                if(wakeLock.isHeld()){
                    wakeLock.release();
                }
            }
        });

        url = "http://archillect.com/tv";
        backgroundView.loadUrl(url);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!Donezo) {
                    backgroundView.postDelayed(this, 7000);
                    backgroundView.loadUrl(url);
                }
            }
        };

        backgroundView.postDelayed(runnable,7000);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            if(wakeLock != null){
                if(wakeLock.isHeld()){
                    System.out.println("Wake Lock is Released!");
                    wakeLock.release();
                }
            }
        } else if(keyCode == KeyEvent.KEYCODE_BACK){
            if(wakeLock != null){
                if(wakeLock.isHeld()){
                    wakeLock.release();
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //TODO: Release WakeLock
        System.out.println("On Destroy!");
        if(wakeLock != null){
            if(wakeLock.isHeld()){
                wakeLock.release();
            }
        }

        mTracker.setScreenName("ExitScreen");
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("Click")
                .setLabel("Exit")
                .build());
    }


}