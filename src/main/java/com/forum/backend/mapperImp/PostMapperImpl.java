package com.forum.backend.mapperImp;

import com.forum.backend.dto.PostDTO;
import com.forum.backend.mapper.PostMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // or @Component or @Mapper
public class PostMapperImpl implements PostMapper {
    @Override
    public List<PostDTO> getAllPosts() {
        return null;
    }

    @Override
    public void createPost(PostDTO postDTO) {

    }

    @Override
    public PostDTO getPostById(Long id) {
        return null;
    }

    @Override
    public void deletePost(Long id) {

    }

    @Override
    public void updatePost(PostDTO postDTO) {

    }
    // Implement the methods of the PostMapper interface
    // ...
}
