package com.kkokko.onna.repository;

import com.kkokko.onna.domain.PostOpen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOpenRepository extends JpaRepository<PostOpen, Long> {

	boolean existsByUserIdAndPostId(Long userId, Long postId);
}
