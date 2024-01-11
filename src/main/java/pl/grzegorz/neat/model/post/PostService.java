package pl.grzegorz.neat.model.post;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface PostService {
    List<PostEntity> getAllPosts();

    List<PostEntity> getNewPosts();
    PostEntity getPostById(Long postId);

    PostEntity createPost(PostEntity post);


    void deletePost(Long postId);
}
