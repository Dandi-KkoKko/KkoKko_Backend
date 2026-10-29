package com.kkokko.closer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 지도에서 사진을 묶어 보여주는 건물 단위 구역. 팀이 좌표를 찍어 data.sql로 넣는다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Zone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private double lat;

	@Column(nullable = false)
	private double lng;

	// 업로드 좌표가 이 반경 안이면 이 구역에 배정한다.
	@Column(nullable = false)
	private int radiusM;

	// 랜드마크 구역은 사진이 몰리므로 대표 사진을 최대 2장까지 보여준다.
	@Column(nullable = false)
	private boolean isLandmark;

	@Builder
	private Zone(String name, double lat, double lng, int radiusM, boolean isLandmark) {
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.radiusM = radiusM;
		this.isLandmark = isLandmark;
	}
}
