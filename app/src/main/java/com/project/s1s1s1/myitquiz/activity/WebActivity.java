package com.project.s1s1s1.myitquiz.activity;

import android.content.Intent;

import android.os.Bundle;

import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.utility.Constant;

import static com.project.s1s1s1.myitquiz.utility.Utils.getSound;
import static com.project.s1s1s1.myitquiz.utility.Utils.isNetworkAvailable;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    int PIC_WIDTH = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.drawable.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.myweb);
        if (!isNetworkAvailable(this)) {
            Snackbar.make(webView, Html.fromHtml("<font color=\"#FA0707\"><b>No internet</b></font>"), Snackbar.LENGTH_LONG).show();
            getSound(this,0).start();
        }
        webView.getSettings().setDomStorageEnabled(true);

//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        //webView.setInitialScale(2);
//        webView.getSettings().setUseWideViewPort(true);
//        //webView.getSettings().setLoadWithOverviewMode(true);
//        //webView.getSettings().setBuiltInZoomControls(true);
//        webView.loadUrl("http://10.80.111.103/phpquiz10/account.php?q=1");
//        WebView web = new WebView(this);
//        web.setPadding(0, 0, 0, 0);
//        web.setInitialScale(getScale());
//
//        webView.setWebViewClient(new WebViewClient());

//        webView.setInitialScale(1);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webView.setScrollbarFadingEnabled(false);
//        webView.loadUrl("http://10.80.111.103/phpquiz10/account.php?q=1");

        WebSettings webSettings = webView.getSettings();
        webSettings.setMinimumFontSize(15);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setMinimumLogicalFontSize(1);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(Constant.BASE_URL + "/index.php");


    }

    private int getScale() {
        Display display = ((WindowManager) getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
