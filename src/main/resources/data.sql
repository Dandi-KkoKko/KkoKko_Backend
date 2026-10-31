-- 태그 사전과 구역(zone) 기초 데이터. 앱이 뜰 때마다 실행된다(spring.sql.init.mode=always).
-- 매번 실행돼도 중복되지 않게 id를 고정하고 upsert로 쓴다. 값을 고치면 다음 부팅 때 반영된다.
-- post_tag, user_preferred_tag, post가 이 id를 참조하므로 한 번 쓴 id는 다른 뜻으로 바꾸지 않는다.
-- 행을 지울 때는 이 파일에서 빼는 것만으로는 DB에서 지워지지 않는다.

-- TODO 임시값: 기획에서 속정 유형 사전(15~20개)을 받으면 교체한다.
INSERT INTO tag (id, name, category) VALUES
    (1, '말없이 챙겨준 사람', '임시'),
    (2, '계산 없이 내어준 마음', '임시'),
    (3, '무뚝뚝한 안부', '임시'),
    (4, '늘 그 자리에 있는 곳', '임시'),
    (5, '처음 와도 편한 곳', '임시')
AS new
ON DUPLICATE KEY UPDATE name = new.name, category = new.category;

-- TODO 임시값: 실제 건물 좌표가 아니다. 팀이 구글맵에서 영남대 건물 좌표를 찍으면 교체한다.
-- 반경(radius_m) 안의 업로드가 이 구역에 배정된다. 랜드마크는 대표 사진을 최대 2장 보여준다.
INSERT INTO zone (id, name, lat, lng, radius_m, is_landmark) VALUES
    (1, '임시 구역 A', 35.8326, 128.7540, 40, false),
    (2, '임시 구역 B', 35.8335, 128.7555, 40, false),
    (3, '임시 랜드마크 C', 35.8318, 128.7560, 60, true)
AS new
ON DUPLICATE KEY UPDATE name = new.name, lat = new.lat, lng = new.lng,
                        radius_m = new.radius_m, is_landmark = new.is_landmark;
