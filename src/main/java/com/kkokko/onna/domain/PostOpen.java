package com.kkokko.onna.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

// 거리 검증을 통과해 사진을 연 기록. 좋아요 자격, 발견 업적, "처음 연 사람" 계산에 쓴다.
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		name = "uk_post_open", columnNames = {"user_id", "post_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostOpen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "post_id")
	private Post post;

	// 다시 열어도 갱신하지 않는다. 처음 연 시각이 업적 계산 기준이다.
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime openedAt;

	@Builder
	private PostOpen(User user, Post post) {
		this.user = user;
		this.post = post;
	}
}
