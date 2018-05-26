package com.treader.demo.service.impl;

import com.treader.demo.model.*;
import com.treader.demo.repository.*;
import com.treader.demo.service.WebPageService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class WebPageServiceImpl implements WebPageService {

    private static final Logger log = LoggerFactory.getLogger(WebPageServiceImpl.class);

    private WebPageRepository webPageRepository;
    private UrlRepository urlRepository;
    private TagUrlRepository tagUrlRepository;
    private TagRepository tagRepository;
    private WebpageTagRepository webpageTagRepository;

    @Autowired
    public WebPageServiceImpl(WebPageRepository webPageRepository, UrlRepository urlRepository, TagUrlRepository tagUrlRepository, TagRepository tagRepository, WebpageTagRepository webpageTagRepository) {
        this.webPageRepository = webPageRepository;
        this.urlRepository = urlRepository;
        this.tagUrlRepository = tagUrlRepository;
        this.tagRepository = tagRepository;
        this.webpageTagRepository = webpageTagRepository;
    }

    @Override
    public void savePage(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            try {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                Url url = urlRepository.findByDomain(page.getWebURL().getDomain());
                if (url == null) {
                    log.error("can not find domain {}", page.getWebURL().getDomain());
                    return;
                }

                WebPage webPage = new WebPage();
                webPage.setUrlId(url.getId());
                webPage.setHtml(htmlParseData.getHtml());
                webPage.setText(htmlParseData.getText());
                webPage.setUrl(page.getWebURL().getURL());
                webPage.setSeen(new Date());

                webPage = webPageRepository.save(webPage);

                searchTag(webPage);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void searchTag(WebPage webPage) {
        List<TagUrl> tagUrlList = tagUrlRepository.findByUrlId(webPage.getUrlId());
        if (CollectionUtils.isEmpty(tagUrlList)) {
            return;
        }
        tagUrlList
                .forEach(tagUrl ->  {
                    Tag tag = tagRepository.findById(tagUrl.getTagId()).get();
                    if (webPage.getText().contains(tag.getTag())) {
                        WebpageTag webpageTag = webpageTagRepository.findByWebpageIdAndTagId(webPage.getId(), tagUrl.getTagId());
                        if (webpageTag == null) {
                            webpageTag = new WebpageTag();
                            webpageTag.setTagId(tagUrl.getTagId());
                            webpageTag.setWebpageId(webPage.getId());
                            webpageTagRepository.save(webpageTag);
                        }
                    }
                });
    }


}
