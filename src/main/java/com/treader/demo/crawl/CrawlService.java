package com.treader.demo.crawl;


import com.treader.demo.model.Url;
import com.treader.demo.service.UrlService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static RobotstxtConfig robotstxtConfig;

    private static final Logger log = LoggerFactory.getLogger(CrawlService.class);


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
        config.setMaxDepthOfCrawling(2);
        /*
         * Instantiate the controller for this crawl.
         */

        robotstxtConfig = new RobotstxtConfig();

    }


    //30分钟
    @Scheduled(fixedRate = 1000*60*10)
    public void startCrawl() throws Exception {
        List<Url> urlList = urlService.findAll();

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        for (Url url : urlList) {
            controller.addSeed(url.getUrl());
            log.info("add seed {}", url.getUrl());
        }
        controller.start(mysqlCrawlerFactory, numberOfCrawlers);
    }
}
