package com.treader.demo.util;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

public class UrlUtil {

    public static String getDomain(String url) {
        String canonicalUrl = URLCanonicalizer.getCanonicalURL(url);
        WebURL webUrl = new WebURL();
        webUrl.setURL(canonicalUrl);
        return webUrl.getDomain();
    }
}
