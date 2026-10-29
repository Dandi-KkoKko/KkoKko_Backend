package com.kkokko.closer.repository;

import com.kkokko.closer.domain.PostOpen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOpenRepository extends JpaRepository<PostOpen, Long> {

	boolean existsByUserIdAndPostId(Long userId, Long postId);
}
