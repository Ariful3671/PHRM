package com.troubleshooters.diu.phrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TipsAndNews extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_and_news);
        String label = getIntent().getStringExtra("label");
        String url = getIntent().getStringExtra("url");
        webView = (WebView)findViewById(R.id.web_view);
        setTitle(label);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }
}
