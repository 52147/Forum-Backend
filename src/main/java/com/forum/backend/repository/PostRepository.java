package com.forum.backend.repository;

import com.forum.backend.entity.Post;
import com.forum.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);
    List<Post> findByUserOrderByCreateTimeDesc(User user);
    List<Post> findByUserId(Long id);
}
