package com.treader.demo.service.impl;

import com.treader.demo.model.WebPage;
import com.treader.demo.repository.WebPageRepository;
import com.treader.demo.service.WebPageService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WebPageServiceImpl implements WebPageService {

    private static final Logger log = LoggerFactory.getLogger(WebPageServiceImpl.class);

    private WebPageRepository webPageRepository;

    @Autowired
    public WebPageServiceImpl(WebPageRepository webPageRepository) {
        this.webPageRepository = webPageRepository;
    }

    @Override
    public void savePage(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            try {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                WebPage webPage = new WebPage();
                webPage.setHtml(htmlParseData.getHtml());
                webPage.setText(htmlParseData.getText());
                webPage.setUrl(page.getWebURL().getURL());
                webPage.setSeen(new Date());
                webPageRepository.save(webPage);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
