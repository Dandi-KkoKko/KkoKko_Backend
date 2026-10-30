package com.kkokko.onna.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 속정 유형 고정 사전. data.sql로 넣고, AI는 이 사전 안에서만 후보를 고른다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	@Column(nullable = false, length = 50)
	private String category;

	@Builder
	private Tag(String name, String category) {
		this.name = name;
		this.category = category;
	}
}
