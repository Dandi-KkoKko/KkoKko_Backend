package com.kkokko.closer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(indexes = {
		// 지도·구역 목록은 항상 "구역 + 공개 상태 + 공개 시각"으로 거른다.
		@Index(name = "idx_post_zone_status_publish", columnList = "zone_id, status, publish_at"),
		@Index(name = "idx_post_lat_lng", columnList = "lat, lng")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	// 가까운 구역이 없으면 비워 둔다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zone_id")
	private Zone zone;

	// 흐린 좌표만 저장한다. 원본 좌표·EXIF는 받는 즉시 버려서 서버에도 남기지 않는다.
	@Column(nullable = false)
	private double lat;

	@Column(nullable = false)
	private double lng;

	// 원본·축소본 경로를 이 키 하나로 규칙에 따라 만든다: {key}/s0.jpg, s1, s2, open, original
	@Column(nullable = false, unique = true, length = 100)
	private String imageKey;

	// 질문 목록은 테이블 없이 코드 상수로 두므로 id만 저장한다.
	@Column(nullable = false)
	private int questionId;

	@Column(nullable = false, length = 100)
	private String oneLine;

	// 남의 조회는 이 시각이 지난 것만 보여준다. 스케줄러 없이 조회 조건만으로 지연 공개를 처리한다.
	@Column(nullable = false)
	private LocalDateTime publishAt;

	// MySQL 네이티브 enum으로 만들면 값을 추가할 때 ddl-auto=update가 컬럼을 고치지 않아서 varchar로 둔다.
	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 20)
	private PostStatus status;

	// 좋아요 수는 작성자 본인에게만 보여준다.
	@Column(nullable = false)
	private int likeCount;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder
	private Post(User user, Zone zone, double lat, double lng, String imageKey,
			int questionId, String oneLine, LocalDateTime publishAt) {
		this.user = user;
		this.zone = zone;
		this.lat = lat;
		this.lng = lng;
		this.imageKey = imageKey;
		this.questionId = questionId;
		this.oneLine = oneLine;
		this.publishAt = publishAt;
		this.status = PostStatus.VISIBLE;
		this.likeCount = 0;
	}

	public boolean isOwnedBy(Long userId) {
		return user.getId().equals(userId);
	}

	// 좋아요·열람 기록이 사진을 참조하므로 행을 지우지 않고 상태만 바꾼다.
	public void delete() {
		this.status = PostStatus.DELETED;
	}
}
