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

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<PostDTO> getAllPosts() {
        return postMapper.getAllPosts();
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
    public void updatePost(PostDTO postDTO) {
        postMapper.updatePost(postDTO);
    }

    @Override
    public Post createPost(Post post) {
        PostDTO postDTO = mapToPostDTO(post);
        postMapper.createPost(postDTO);
        return mapToPost(postDTO);
    }

    @Override
    public Post updatePost(Long id, Post post) {
        PostDTO postDTO = mapToPostDTO(post);
        postDTO.setId(id);
        postMapper.updatePost(postDTO);
        return mapToPost(postDTO);
    }

    @Override
    public void updatePost(Long id, PostDTO postDTO) {
        postDTO.setId(id);
        postMapper.updatePost(postDTO);
    }

    private PostDTO mapToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        // Map other properties as needed
        return postDTO;
    }

    private Post mapToPost(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        // Map other properties as needed
        return post;
    }
}
