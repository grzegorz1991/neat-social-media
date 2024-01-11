package pl.grzegorz.neat.model.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public PostEntity getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public PostEntity createPost(PostEntity post) {
        // Set current time when creating a post
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostEntity> getNewPosts() {
        return postRepository.findAllOrderByCreatedAtDesc();
    }
}
