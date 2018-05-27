package com.treader.demo.service.impl;

import com.treader.demo.model.*;
import com.treader.demo.repository.*;
import com.treader.demo.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

    private UrlRepository urlRepository;
    private TagUrlRepository tagUrlRepository;
    private UserUrlRepository userUrlRepository;
    private WebPageRepository webPageRepository;
    private WebpageTagRepository webpageTagRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, TagUrlRepository tagUrlRepository, UserUrlRepository userUrlRepository, WebPageRepository webPageRepository, WebpageTagRepository webpageTagRepository) {
        this.urlRepository = urlRepository;
        this.tagUrlRepository = tagUrlRepository;
        this.userUrlRepository = userUrlRepository;
        this.webPageRepository = webPageRepository;
        this.webpageTagRepository = webpageTagRepository;
    }


    @Override
    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        Optional<Url> urlOptional = urlRepository.findById(id);
        if (!urlOptional.isPresent()) {
            return;
        }
        Url url = urlOptional.get();

        //删除tagUrl关联
        List<TagUrl> tagUrlList = tagUrlRepository.findByUrlId(url.getId());
        tagUrlList.forEach(tagUrl -> tagUrlRepository.deleteById(tagUrl.getId()));

        //删除userUrl关联
        List<UserUrl> userUrlList = userUrlRepository.findByUrlId(url.getId());
        userUrlList.forEach(userUrl -> userUrlRepository.deleteById(userUrl.getId()));

        //删除webpage关联
        List<WebPage> webPageList = webPageRepository.findByUrlId(url.getId());
        webPageList.forEach(webPage -> {
            //webppage删除了，还要删除webpageTag关联
            List<WebpageTag> webpageTagList = webpageTagRepository.findByWebpageId(webPage.getId());

            webpageTagList.forEach(webpageTag -> {
                webpageTagRepository.deleteById(webpageTag.getId());
            });

            webPageRepository.deleteById(webPage.getId());
        });

        //删除url
        urlRepository.deleteById(url.getId());
    }
}
