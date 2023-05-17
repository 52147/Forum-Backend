package com.forum.backend.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.forum.backend.dto.PostDTO;
import com.forum.backend.entity.Post;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
@Mapper
public interface PostMapper extends BaseMapper<Post>{

    List<PostDTO> getAllPosts();

    void createPost(PostDTO postDTO);

    PostDTO getPostById(Long id);

    void deletePost(Long id);

    void updatePost(PostDTO postDTO);
}

