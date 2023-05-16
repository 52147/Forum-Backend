package com.forum.backend.mapper;

import com.forum.backend.dto.PostDTO;
import com.forum.backend.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT * FROM posts")
    List<PostDTO> getAllPosts();

    @Insert("INSERT INTO posts (title, content) VALUES (#{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createPost(PostDTO postDTO);

    @Select("SELECT * FROM posts WHERE id = #{id}")
    PostDTO getPostById(Long id);

    @Delete("DELETE FROM posts WHERE id = #{id}")
    void deletePost(Long id);

    @Update("UPDATE posts SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updatePost(PostDTO postDTO);

}
