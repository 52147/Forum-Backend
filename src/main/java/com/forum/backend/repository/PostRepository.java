package com.forum.backend.repository;

import com.forum.backend.entity.Post;
import com.forum.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreateTimeDesc();

    List<Post> findByTitleContainingOrderByCreateTimeDesc(String keyword);

    List<Post> findByUserOrderByCreateTimeDesc(User user);

    List<Post> findByTitleContaining(String keyword);

    Post save(Post post);

    List<Post> findByUserId(Long id);
}
