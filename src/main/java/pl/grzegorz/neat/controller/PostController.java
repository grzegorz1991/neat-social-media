package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    private static final String NEW_ARTICLE_FRAGMENT = "/home/newarticlepost-fragment";
    private static final String NEW_EVENT_ANNOUNCEMENT_FRAGMENT = "/home/newposteventannouncement-fragment";
    private static final String NEW_MEDIA_POST_FRAGMENT = "/home/newpostmediapost-fragment";


    @GetMapping(NEW_ARTICLE_FRAGMENT)
    public String getNewArticlePostFragment(Model model, Authentication authentication){

        return NEW_ARTICLE_FRAGMENT;
    }

    @GetMapping(NEW_EVENT_ANNOUNCEMENT_FRAGMENT)
    public String getNewEventAnnouncementFragment(Model model, Authentication authentication){

        return NEW_EVENT_ANNOUNCEMENT_FRAGMENT;
    }

    @GetMapping(NEW_MEDIA_POST_FRAGMENT)
    public String getNewMediaPostFragment(Model model, Authentication authentication){

        return NEW_MEDIA_POST_FRAGMENT;
    }
}
