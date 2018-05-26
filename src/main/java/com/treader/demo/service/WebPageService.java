package com.treader.demo.service;

import com.treader.demo.model.WebPage;
import edu.uci.ics.crawler4j.crawler.Page;

public interface WebPageService {
    void savePage(Page page);

    void searchTag(WebPage webPage);
}
