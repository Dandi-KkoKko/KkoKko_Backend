package com.kkokko.onna.repository;

import com.kkokko.onna.domain.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);

	// 내 취향 = 온보딩 태그 + 좋아요 누른 사진의 태그. 그 사진 목록을 먼저 뽑는다.
	@Query("select pl.post.id from PostLike pl where pl.user.id = :userId")
	List<Long> findPostIdsByUserId(@Param("userId") Long userId);
}
