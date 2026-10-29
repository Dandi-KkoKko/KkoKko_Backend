package com.kkokko.closer.global.util;

// 거리 판정과 위치 흐림에 쓰는 좌표 계산. 캠퍼스 안의 수십~수백 m 거리만 다루므로 구면 근사로 충분하다.
public final class GeoUtils {

	private static final double EARTH_RADIUS_M = 6_371_000;

	// 위도 1도의 길이. 하버사인과 같은 반지름을 써야 snap 격자와 distance가 서로 어긋나지 않는다.
	private static final double METERS_PER_DEGREE_LAT = EARTH_RADIUS_M * Math.PI / 180;

	private GeoUtils() {
	}

	// 하버사인 공식. 단위는 m.
	public static double distance(double lat1, double lng1, double lat2, double lng2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		return 2 * EARTH_RADIUS_M * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	}

	// 좌표를 gridM 간격 격자의 가장 가까운 꼭짓점으로 옮긴다.
	// 같은 칸 안의 좌표는 모두 같은 점이 되므로, 저장된 좌표로 실제 위치를 gridM보다 좁혀 알아낼 수 없다.
	public static LatLng snap(double lat, double lng, double gridM) {
		double latStep = gridM / METERS_PER_DEGREE_LAT;
		double snappedLat = Math.round(lat / latStep) * latStep;
		// 경도 1도의 길이는 위도에 따라 줄어든다. 스냅된 위도로 계산해야 같은 칸이 같은 경도 간격을 쓴다.
		double lngStep = latStep / Math.cos(Math.toRadians(snappedLat));
		double snappedLng = Math.round(lng / lngStep) * lngStep;
		return new LatLng(snappedLat, snappedLng);
	}
}
