package com.forum.backend.controller;

import com.forum.backend.entity.Post;
import com.forum.backend.respository.PostRepository;
import com.forum.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/posts")
@Api(value = "PostController", description = "REST APIs related to Post Entity")
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
    @ApiOperation(value = "View a list of all posts", response = List.class)
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

//    @PostMapping("/posts")
//    @ApiOperation(value = "Create a new post", response = Post.class)
//    public Post createPost(@RequestBody Post post) {
//        return postService.createPost(post);
//    }

    @GetMapping("/posts/{id}")
    @ApiOperation(value = "Get a post by its ID", response = Post.class)
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/posts/{id}")
    @ApiOperation(value = "Update a post", response = Post.class)
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/posts/{id}")
    @ApiOperation(value = "Delete a post by its ID", response = Void.class)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}

