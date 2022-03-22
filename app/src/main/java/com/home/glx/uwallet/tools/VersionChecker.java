package com.home.glx.uwallet.tools;

import android.os.AsyncTask;

import com.home.glx.uwallet.BuildConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VersionChecker extends AsyncTask<String, String, String> {

    private String newVersion;

    @Override
    protected String doInBackground(String... params) {

        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    .timeout(3000)
                    //.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.log("version IOException:" + e.toString());
            e.printStackTrace();
            newVersion = "";
        }
        return newVersion;
    }
}
