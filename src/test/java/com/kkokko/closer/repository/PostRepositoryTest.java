package com.kkokko.closer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kkokko.closer.domain.Post;
import com.kkokko.closer.domain.PostStatus;
import com.kkokko.closer.domain.User;
import com.kkokko.closer.domain.Zone;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// 로컬 MySQL에서 돌리고, 각 테스트는 끝나면 롤백된다.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private EntityManager em;

	private final LocalDateTime now = LocalDateTime.of(2026, 10, 29, 12, 0);

	private User user;
	private Zone zone;
	private Zone otherZone;

	@BeforeEach
	void setUp() {
		user = User.builder().email(UUID.randomUUID() + "@test.com").password("pw").nickname("테스터").build();
		zone = Zone.builder().name("테스트 구역").lat(35.83).lng(128.75).radiusM(40).isLandmark(false).build();
		otherZone = Zone.builder().name("다른 구역").lat(35.84).lng(128.76).radiusM(40).isLandmark(false).build();
		em.persist(user);
		em.persist(zone);
		em.persist(otherZone);
	}

	@Test
	void 남의_조회는_공개_상태이고_공개_시각이_지난_사진만_나온다() {
		Post published = save(zone, now.minusMinutes(1));
		Post publishedNow = save(zone, now);
		save(zone, now.plusMinutes(1));
		Post hidden = save(zone, now.minusHours(1));
		Post deleted = save(zone, now.minusHours(1));
		deleted.delete();
		save(otherZone, now.minusHours(1));
		em.flush();
		// HIDDEN으로 바꾸는 기능은 아직 없어서 쿼리로 직접 바꾼다.
		em.createQuery("update Post p set p.status = :status where p.id = :id")
				.setParameter("status", PostStatus.HIDDEN)
				.setParameter("id", hidden.getId())
				.executeUpdate();
		em.clear();

		List<Post> result = postRepository.findVisibleByZoneIdIn(List.of(zone.getId()), now);

		assertThat(result).extracting(Post::getId)
				.containsExactlyInAnyOrder(published.getId(), publishedNow.getId());
	}

	@Test
	void 단건_공개_조회도_공개_시각이_안_지난_사진은_없다() {
		Post future = save(zone, now.plusMinutes(1));
		Post published = save(zone, now.minusMinutes(1));
		em.flush();

		assertThat(postRepository.findVisibleById(future.getId(), now)).isEmpty();
		assertThat(postRepository.findVisibleById(published.getId(), now)).isPresent();
	}

	@Test
	void 본인_조회는_공개_전_사진도_나오고_삭제된_사진은_빠진다() {
		Post future = save(zone, now.plusHours(2));
		Post deleted = save(zone, now.minusHours(1));
		deleted.delete();
		em.flush();

		List<Post> result = postRepository.findByUserIdAndStatusNotOrderByCreatedAtDesc(
				user.getId(), PostStatus.DELETED);

		assertThat(result).extracting(Post::getId).containsExactly(future.getId());
	}

	@Test
	void 좋아요_수는_0_아래로_내려가지_않는다() {
		Post post = save(zone, now.minusHours(1));
		em.flush();

		postRepository.increaseLikeCount(post.getId());
		postRepository.decreaseLikeCount(post.getId());
		int updated = postRepository.decreaseLikeCount(post.getId());

		assertThat(updated).isZero();
		assertThat(postRepository.findById(post.getId()).orElseThrow().getLikeCount()).isZero();
	}

	private Post save(Zone zone, LocalDateTime publishAt) {
		Post post = Post.builder()
				.user(user).zone(zone).lat(zone.getLat()).lng(zone.getLng())
				.imageKey(UUID.randomUUID().toString()).questionId(1).oneLine("한 줄")
				.publishAt(publishAt)
				.build();
		return postRepository.save(post);
	}
}
