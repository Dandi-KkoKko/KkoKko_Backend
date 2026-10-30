package com.kkokko.onna.service;

import com.kkokko.onna.domain.Zone;
import com.kkokko.onna.global.util.GeoUtils;
import com.kkokko.onna.repository.ZoneRepository;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

	private final ZoneRepository zoneRepository;

	// 업로드 좌표를 구역에 배정한다. 반경 안의 구역이 여러 개면 중심이 가장 가까운 곳, 없으면 empty.
	// zone은 팀이 찍은 건물 수십 개뿐이라 공간 인덱스 없이 전부 읽어 메모리에서 계산한다.
	public Optional<Zone> findZone(double lat, double lng) {
		return zoneRepository.findAll().stream()
				.filter(zone -> distanceTo(zone, lat, lng) <= zone.getRadiusM())
				.min(Comparator.comparingDouble(zone -> distanceTo(zone, lat, lng)));
	}

	private double distanceTo(Zone zone, double lat, double lng) {
		return GeoUtils.distance(zone.getLat(), zone.getLng(), lat, lng);
	}
}
