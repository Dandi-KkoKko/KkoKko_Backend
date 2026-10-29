package com.kkokko.closer.repository;

import com.kkokko.closer.domain.Post;
import com.kkokko.closer.domain.PostStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

	// 남의 사진 조회는 모두 아래 두 메서드를 쓴다. 공개 상태이고 공개 시각이 지난 사진만 나온다.

	@Query("""
			select p from Post p
			where p.zone.id in :zoneIds
			  and p.status = com.kkokko.closer.domain.PostStatus.VISIBLE
			  and p.publishAt <= :now
			""")
	List<Post> findVisibleByZoneIdIn(@Param("zoneIds") Collection<Long> zoneIds,
			@Param("now") LocalDateTime now);

	@Query("""
			select p from Post p
			where p.id = :id
			  and p.status = com.kkokko.closer.domain.PostStatus.VISIBLE
			  and p.publishAt <= :now
			""")
	Optional<Post> findVisibleById(@Param("id") Long id, @Param("now") LocalDateTime now);

	// 본인 조회는 공개 시각 조건을 걸지 않는다. 올린 직후에도 바로 보이게 하기 위해서다.
	List<Post> findByUserIdAndStatusNotOrderByCreatedAtDesc(Long userId, PostStatus status);

	// 동시에 눌려도 수가 어긋나지 않게 엔티티 필드가 아니라 DB에서 바로 증감한다.
	@Modifying(clearAutomatically = true)
	@Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :id")
	int increaseLikeCount(@Param("id") Long id);

	@Modifying(clearAutomatically = true)
	@Query("update Post p set p.likeCount = p.likeCount - 1 where p.id = :id and p.likeCount > 0")
	int decreaseLikeCount(@Param("id") Long id);
}
