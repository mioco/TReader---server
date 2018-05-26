package com.treader.demo.crawl;

import com.treader.demo.service.WebPageService;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MysqlCrawlerFactory implements CrawlController.WebCrawlerFactory<MysqlWebCrawler> {

    private WebPageService webPageService;

    @Autowired
    public MysqlCrawlerFactory(WebPageService webPageService) {
        this.webPageService = webPageService;
    }

    @Override
    public MysqlWebCrawler newInstance() throws Exception {
        return new MysqlWebCrawler(webPageService);
    }
}