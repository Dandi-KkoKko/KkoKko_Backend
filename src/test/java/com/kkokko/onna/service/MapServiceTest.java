package com.kkokko.onna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.kkokko.onna.domain.Zone;
import com.kkokko.onna.repository.ZoneRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapServiceTest {

	@Mock
	private ZoneRepository zoneRepository;

	@InjectMocks
	private MapService mapService;

	// 위도 0.0001도는 약 11.1m.
	private final Zone a = zone("A동", 35.8325, 128.7544, 40);
	private final Zone b = zone("B동", 35.8331, 128.7544, 40);

	@Test
	void 반경_안의_구역이_여러_개면_중심이_가장_가까운_구역을_준다() {
		given(zoneRepository.findAll()).willReturn(List.of(a, b));

		// A 중심에서 북쪽 약 44m, B 중심에서 남쪽 약 22m. 반경 40m 안에 드는 건 B뿐이다.
		assertThat(mapService.findZone(35.8329, 128.7544)).contains(b);
		// A에서 약 22m, B에서 약 44m.
		assertThat(mapService.findZone(35.8327, 128.7544)).contains(a);
	}

	@Test
	void 두_구역_반경이_겹치는_곳에서는_더_가까운_구역을_준다() {
		Zone wideA = zone("A동", 35.8325, 128.7544, 60);
		Zone wideB = zone("B동", 35.8331, 128.7544, 60);
		given(zoneRepository.findAll()).willReturn(List.of(wideA, wideB));

		// A에서 약 22m, B에서 약 44m. 둘 다 반경 60m 안이다.
		assertThat(mapService.findZone(35.8327, 128.7544)).contains(wideA);
	}

	@Test
	void 반경_경계는_구역_안으로_본다() {
		Zone exact = zone("C동", 35.8325, 128.7544, 22);
		given(zoneRepository.findAll()).willReturn(List.of(exact));

		// 약 22.24m 떨어진 점. 반경 22m 밖, 23m 안.
		assertThat(mapService.findZone(35.8327, 128.7544)).isEmpty();
		given(zoneRepository.findAll()).willReturn(List.of(zone("C동", 35.8325, 128.7544, 23)));
		assertThat(mapService.findZone(35.8327, 128.7544)).isPresent();
	}

	@Test
	void 반경_안에_구역이_없으면_비어_있다() {
		given(zoneRepository.findAll()).willReturn(List.of(a, b));

		assertThat(mapService.findZone(35.8400, 128.7544)).isEqualTo(Optional.empty());
	}

	private static Zone zone(String name, double lat, double lng, int radiusM) {
		return Zone.builder().name(name).lat(lat).lng(lng).radiusM(radiusM).isLandmark(false).build();
	}
}
