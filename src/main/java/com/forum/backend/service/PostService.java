package com.forum.backend.service;

import com.forum.backend.dto.PostDTO;
import com.forum.backend.entity.Post;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();


    Post createPost(Post post);

    void createPost(PostDTO postDTO);

    PostDTO getPostById(Long id);

    void deletePost(Long id);

    Post updatePost(Long id, Post post);

    PostDTO updatePost(Long id, PostDTO postDTO);

    void updatePost(PostDTO postDTO);
}
