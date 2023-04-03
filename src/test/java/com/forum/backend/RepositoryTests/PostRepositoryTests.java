package com.forum.backend.RepositoryTests;

import com.forum.backend.entity.Post;
import com.forum.backend.entity.User;
import com.forum.backend.respository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void whenFindByUserId_thenReturnPostList() {
        // given
        User user = new User();
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        entityManager.persist(user);

        Post post = new Post();
        post.setTitle("First Post");
        post.setContent("Content of first post");
        post.setUser(user);
        entityManager.persist(post);

        post = new Post();
        post.setTitle("Second Post");
        post.setContent("Content of second post");
        post.setUser(user);
        entityManager.persist(post);

        entityManager.flush();

        // when
        List<Post> postsByUser = postRepository.findByUserId(user.getId());

        // then
        assertThat(postsByUser).hasSize(2);
        assertThat(postsByUser.get(0).getTitle()).isEqualTo("First Post");
        assertThat(postsByUser.get(1).getTitle()).isEqualTo("Second Post");
    }

}
