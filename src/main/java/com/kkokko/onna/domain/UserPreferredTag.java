package com.kkokko.onna.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 온보딩에서 고른 선호 태그. 대표 사진의 취향 점수 계산에 쓴다.
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		name = "uk_user_preferred_tag", columnNames = {"user_id", "tag_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreferredTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tag_id")
	private Tag tag;

	@Builder
	private UserPreferredTag(User user, Tag tag) {
		this.user = user;
		this.tag = tag;
	}
}
