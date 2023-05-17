package com.forum.backend.service;

import com.forum.backend.dto.PostDTO;
import com.forum.backend.entity.Post;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PostService {

    List<PostDTO> getAllPosts();

    void createPost(PostDTO postDTO);

    PostDTO getPostById(Long id);

    void deletePost(Long id);

    void updatePost(PostDTO postDTO);

    Post createPost(Post post);

    Post updatePost(Long id, Post post);

    void updatePost(Long id, PostDTO postDTO);
}
