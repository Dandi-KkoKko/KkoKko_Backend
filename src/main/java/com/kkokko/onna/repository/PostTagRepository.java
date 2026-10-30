package com.kkokko.onna.repository;

import com.kkokko.onna.domain.PostTag;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	// 사진 목록의 태그를 사진마다 따로 조회하지 않고 한 번에 가져온다.
	@Query("select pt from PostTag pt join fetch pt.tag where pt.post.id in :postIds")
	List<PostTag> findWithTagByPostIdIn(@Param("postIds") Collection<Long> postIds);
}
