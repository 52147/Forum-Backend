package com.forum.backend.serviceImp;

import com.forum.backend.dto.PostDTO;
import com.forum.backend.entity.Post;
import com.forum.backend.mapper.PostMapper;
import com.forum.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public List<PostDTO> getAllPosts() {
        return postMapper.getAllPosts();
    }

    @Override
    public Post createPost(Post post) {
        return null;
    }

    @Override
    public void createPost(PostDTO postDTO) {
        postMapper.createPost(postDTO);
    }

    @Override
    public PostDTO getPostById(Long id) {
        return postMapper.getPostById(id);
    }

    @Override
    public void deletePost(Long id) {
        postMapper.deletePost(id);
    }

    @Override
    public Post updatePost(Long id, Post post) {
        return null;
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        return null;
    }

    @Override
    public void updatePost(PostDTO postDTO) {
        postMapper.updatePost(postDTO);
    }
}
