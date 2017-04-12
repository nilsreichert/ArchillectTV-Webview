package com.vivi.viviarchillecttv;

import android.service.dreams.DreamService;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArchillectDream extends DreamService {

    public ArchillectApplication application;

    public WebView backgroundView;

    String url;

    Runnable runnable;

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        setContentView(R.layout.activity_main);

        backgroundView = (WebView) findViewById(R.id.backgroundview);

        backgroundView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        url = "http://archillect.com/tv";
        backgroundView.loadUrl(url);

        runnable = new Runnable() {
            @Override
            public void run() {
                backgroundView.postDelayed(this, 7000);
                backgroundView.loadUrl(url);
            }
        };

        backgroundView.postDelayed(runnable,7000);
    }

}
