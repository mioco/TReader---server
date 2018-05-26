package com.treader.demo.crawl;


import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CrawlService implements InitializingBean {

    private static final String crawlStorageFolder = "/tmp/crawl";

    private static final int numberOfCrawlers = 7;

    private static final CrawlConfig config = new CrawlConfig();

    private CrawlController controller;

    private MysqlCrawlerFactory mysqlCrawlerFactory;

    @Autowired
    public CrawlService(MysqlCrawlerFactory mysqlCrawlerFactory) {
        this.mysqlCrawlerFactory = mysqlCrawlerFactory;
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

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://new.qq.com/omn/:*/:*.html");
    }


    @Scheduled(fixedRate = 30000)
    public void startCrawl() {
        controller.start(mysqlCrawlerFactory, numberOfCrawlers);
    }
}
