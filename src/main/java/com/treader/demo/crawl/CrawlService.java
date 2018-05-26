package com.treader.demo.crawl;


import com.treader.demo.model.Url;
import com.treader.demo.service.UrlService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CrawlService implements InitializingBean {

    private static final String crawlStorageFolder = "/tmp/crawl";

    private static final int numberOfCrawlers = 3;

    private static final CrawlConfig config = new CrawlConfig();

    private CrawlController controller;

    private MysqlCrawlerFactory mysqlCrawlerFactory;
    private UrlService urlService;


    @Autowired
    public CrawlService(MysqlCrawlerFactory mysqlCrawlerFactory, UrlService urlService) {
        this.mysqlCrawlerFactory = mysqlCrawlerFactory;
        this.urlService = urlService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        config.setPolitenessDelay(100);
        config.setCrawlStorageFolder(crawlStorageFolder);
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        this.controller = new CrawlController(config, pageFetcher, robotstxtServer);

    }


    //30分钟
    @Scheduled(fixedRate = 1000*60*30)
    public void startCrawl() {
        List<Url> urlList = urlService.findAll();

        for (Url url : urlList) {
            controller.addSeed(url.getUrl());
        }

        controller.start(mysqlCrawlerFactory, numberOfCrawlers);
    }
}
