package com.kkokko.onna.repository;

import com.kkokko.onna.domain.UserPreferredTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPreferredTagRepository extends JpaRepository<UserPreferredTag, Long> {

	// 온보딩 완료 여부는 컬럼 대신 선호 태그가 있는지로 판단한다.
	boolean existsByUserId(Long userId);

	@Query("select upt.tag.id from UserPreferredTag upt where upt.user.id = :userId")
	List<Long> findTagIdsByUserId(@Param("userId") Long userId);

	// 선호 태그를 다시 저장할 때 한 건씩 지우지 않고 한 번에 지운다.
	@Modifying(clearAutomatically = true)
	@Query("delete from UserPreferredTag upt where upt.user.id = :userId")
	void deleteAllByUserId(@Param("userId") Long userId);
}
