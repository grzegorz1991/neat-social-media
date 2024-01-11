package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.grzegorz.neat.model.post.PostEntity;
import pl.grzegorz.neat.model.post.PostService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;

import java.time.LocalDateTime;


@Controller
public class PostController {

    private static final String NEW_ARTICLE_FRAGMENT = "/home/postFragments/newarticlepost-fragment";
    private static final String NEW_ARTICLE_ENDPOINT = "/home/newarticlepost-fragment";

    private static final String NEW_EVENT_ANNOUNCEMENT_ENDPOINT = "/home/newposteventannouncement-fragment";
    private static final String NEW_EVENT_ANNOUNCEMENT_FRAGMENT = "/home/postFragments/newposteventannouncement-fragment";

    private static final String NEW_MEDIA_POST_ENDPOINT = "/home/newpostmediapost-fragment";
    private static final String NEW_MEDIA_POST_FRAGMENT = "/home/postFragments/newpostmediapost-fragment";

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(NEW_ARTICLE_ENDPOINT)
    public String getNewArticlePostFragment(Model model, Authentication authentication) {
        model.addAttribute("post", new PostEntity());

        return NEW_ARTICLE_FRAGMENT;
    }
    @PostMapping("/savepost")
    public String savePost(@ModelAttribute PostEntity post, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        post.setUser(currentUser);
        post.setCreatedAt(LocalDateTime.now());

        postService.createPost(post);
        return "redirect:/"; // Redirect to home or another appropriate endpoint
    }

    @GetMapping(NEW_EVENT_ANNOUNCEMENT_ENDPOINT)
    public String getNewEventAnnouncementFragment(Model model, Authentication authentication) {

        return NEW_EVENT_ANNOUNCEMENT_FRAGMENT;
    }

    @GetMapping(NEW_MEDIA_POST_ENDPOINT)
    public String getNewMediaPostFragment(Model model, Authentication authentication) {

        return NEW_MEDIA_POST_FRAGMENT;
    }


}
