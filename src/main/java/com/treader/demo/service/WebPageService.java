package com.treader.demo.service;

import com.treader.demo.model.WebPage;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface WebPageService {
    void savePage(Page page);

    void searchTag(WebPage webPage);

    boolean shouldVisit(WebURL url);
}
