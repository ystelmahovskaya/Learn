package com.example.yuliiastelmakhovska.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Statistics extends Fragment {
    public WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_statistics, container, false);
         mWebView = (WebView) rootView.findViewById(R.id.webstatistics);
        mWebView.loadUrl("http://"+MainActivity.ip+"/statistic.html#"+MainActivity.user_id);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        mWebView.setWebViewClient(new WebViewClient());


        return rootView;
    }
}
