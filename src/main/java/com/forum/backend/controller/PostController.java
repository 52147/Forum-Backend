package com.forum.backend.controller;

import com.forum.backend.dto.PostDTO;
import com.forum.backend.entity.Post;
import com.forum.backend.repository.PostRepository;
import com.forum.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        try {
            postRepository.save(post);
            return new ResponseEntity<>("Post created successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception message
            logger.error("Error creating post: {}", e.getMessage());
            // Return an error response
            return new ResponseEntity<>("Error creating post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam(required = true) String keyword) {
        List<Post> posts = postRepository.findByTitleContaining(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

//    @PostMapping("/posts")
//    @ApiOperation(value = "Create a new post", response = Post.class)
//    public Post createPost(@RequestBody Post post) {
//        return postService.createPost(post);
//    }

    @GetMapping("/posts/{id}")
    public PostDTO getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}

