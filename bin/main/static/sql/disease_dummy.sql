-- 질환 카테고리 더미데이터
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("간 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("갑상선 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("심혈관계 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("내과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("정형외과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("신경과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("안과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("내분비내과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("정신과 질환");

-- 질환 더미데이터
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        1, 
        '지방간', 
        '지방간이란 트리글리세라이드(TG)라는 지방이 간세포 속에 축적되는 질환으로 간 무게의 5% 이상을 이 트리글리세라이드가 차지할 때를 말합니다. 지방간은 기름(TG)이 간세포 속에 쌓이게 되므로 간기능이 나빠질 수 밖에 없습니다.\n요즘은 기름진 음식이나 고칼로리를 섭취하는 식생활의 영향으로서 꾸준히 늘어나는 흔한 병이 되었습니다. 증상은 대개 무증상, 우연히 발견되는 경우가 많으며 식욕부진, 구역질, 피곤함 등의 일반적인 증상이 나타난다. 드물게는 오른쪽 상복부의 뻐근한 팽만감 등이 나타납니다.', 
        '녹차, 등푸른 생선, 두부, 미나리, 배추, 브로콜리, 사과, 양송이버섯, 올리브 오일, 자몽, 조개, 쑥, 칡, 헛개나무',
        '견과류, 녹즙, 술, 육류, 튀김류, 흰 빵');    
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        1, 
        '급성 간염', 
        '어떤 원인에 의하여 간 조직에 염증이 생기고 간 세포의 파괴, 간기능 장애 등이 나타나는 질환으로 이러한 변화가 갑자기 발생하여 비교적 단기일 내(6개월)에 회복되는 경우를 급성간염이라고 합니다.\n잠복기에는 증상이 거의 없습니다. 그러나 전구증상기가 되면 발열, 두통, 전신피로, 권태감, 식욕부진, 구토, 소화불량, 상복부 동통 등 소화기 증상이 나타납니다. 황달기에 접어들면 소변의 색이 갈색 또는 흑갈색으로 변하고, 눈의 공막이 사진처럼 황색으로 변하며 병이 진전되면서 피부까지 노란색으로 착색됩니다.이러한 증세가 몇 주에서 몇 개월에 걸쳐 진행되다가 호전됩니다.', 
        '녹차, 등푸른 생선, 두부, 미나리, 배추, 브로콜리, 사과, 양송이버섯, 올리브 오일, 자몽, 조개, 쑥, 칡, 헛개나무', 
        '견과류, 녹즙, 술, 육류, 튀김류, 흰 빵');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        1, 
        '바이러스성 간염', 
        '만성간염은 간 조직 내 염증이 6개월 이상 만성적으로 지속되는 질환으로 장기간 만성간염을 앓을때 간 조직이 딱딱하게 굳어지는 간경변증으로 이행할 수 있습니다.간경변증은 간 조직 내 정상 간세포의 나열구조가 파괴되고 섬유화가 형성되는 단계로 간성 혼수, 복수, 출혈 등의 여러가지 합병증으로 나타날 수 있습니다. 만성 간염에서 간경변증에 이르기까지 다양하며 증상도 무증상부터 심한 간 기능 부전까지 다양합니다. 일반적으로 만성 간질환은 증상이 없으나 있더라도 심하지 않고 비특이적입니다. 만성간염 초기에는 피로감, 전신 권태, 구역질, 식욕부진, 소화불량 등이며 흑갈색의 얼굴, 손, 발바닥의 모세혈관 확장됩니다. 그리고 말기가 되면 간세포 기능 장애와 이에 따른 합병증으로 짙은 소변 색깔, 황달, 잇몸이나 코의 출혈이 나타나고 간문맥압 항진으로 인해 비장이 커지고 배에 복수가 차며 다리에 부종이 생기며 토혈, 혈변, 혼수 등이 생길 수도 있을 수도 있습니다.', 
        '녹차, 등푸른 생선, 두부, 미나리, 배추, 브로콜리, 사과, 양송이버섯, 올리브 오일, 자몽, 조개, 쑥, 칡, 헛개나무', 
        '견과류, 녹즙, 술, 육류, 튀김류, 흰 빵');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        1, 
        '간암', 
        '간의 악성 종양에는 여러가지가 있으나 우리나라 사람들에게 흔한 것은 간세포암과 담관세포함으로 약 65%를 차지합니다.특히 간세포암(Hepatocellular carcinoma, 90%)이 가장 흔합니다.우리나라에서 위암 다음으로 간암이 많으며 (전체 암의 11.6%), 남성의 암 사망 중 위암에 이어서 2위를 차지하고, 여성의 경우 위암, 폐암에 이어 암 사망 3위를 차지하고 있습니다. 간암초기의 증상은 별로 없습니다. 가장 잘 나타나는 것이 피곤함이 심하게 느껴집니다. 체중이 급격히 감소하며 윗배의 오른쪽 부분에 통증이 느껴지고 식욕감소와 함께 심한 포만감이 느껴집니다.', 
        '녹차, 등푸른 생선, 두부, 미나리, 배추, 브로콜리, 사과, 양송이버섯, 올리브 오일, 자몽, 조개, 쑥, 칡, 헛개나무', 
        '견과류, 녹즙, 술, 육류, 튀김류, 흰 빵');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        2, 
        '갑상선 기능 항진증', 
        '우리가 흔히 말하는 갑상선 중독증이랑 같은 의미입니다.항진증은 우리 몸의 신진대사가 활발해져 몸에서 열이 나고, 더위를 잘 타며, 체중이 빠지고, 심장도 운동을 활발히 해서 가슴이 두근거리고, 장도 운동이 활발해져서 설사가 유발될 수 있으며, 아무리 음식을 많이 먹어도 몸무게가 감소합니다. 즉 에너지를 많이 소모하는 방향으로 증상이 나타납니다.\n대개 항진증은 젊은 여성들에게 호발하고, 눈이 튀어나오고(안구돌출증), 갑상선의 종대가 있는 그레이브스병이 대부분입니다. 종대의 정도는 육안으로 확연히 보이는 경우부터 전문의가 만져야만 간신히 알 수 있는 경우까지 다양합니다.', 
        '감자, 고구마, 검은콩, 복숭아, 블루베리, 쌀, 아보카도, 양배추, 유제품, 통곡물, 흰 빵', 
        '가공식품, 김치류, 달걀, 장아찌, 젓갈, 콩');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        2, 
        '갑상선 기능 저하증', 
        '우리 몸의 대사상태가 감소되어 몸이 붓고, 체중이 증가할 수 있으며, 몸에서 열의 생산이 안되어 추위를 잘 타고, 맥박수도 느려지고, 장의 운동도 느려져서 변비가 생길 수도 있으며, 무력감이나 쇠약감이 극심하고, 말도 느려지고, 여자의 경우 생리량이 늘 수도 있습니다. 맥박도 느려집니다.', 
        '굴, 달걀, 버섯, 잡곡밥, 토마토, 해조류, 현미, 흰살 생선', 
        '배추, 브로콜리, 복숭아, 설탕, 양배추, 콩, 밀가루, 무우');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        2, 
        '갑상선결절(종양)', 
        '갑상선결절(종양)이란 갑상선에서 만져지는 모든 종류의 멍울(혹)을 통칭하는 것으로 성인의 약 5% 정도에서 확인이 되며, 질환의 종류는 결절성 증식, 양성종양, 암, 갑상선염, 낭종(물혹) 등이 있고 그 중 약 10%가 갑상선암으로 진단됩니다.', 
        '검은콩, 브로콜리, 양배추', 
        '갈치, 김, 꽁치, 다시마, 달걀, 미역, 아이스크림, 이면수, 치즈, 파래, 요구르트');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        3, 
       '고혈압', 
       '고혈압은 비정상적으로 계속해서 혈압이 높은 질환입니다. 혈압 (blood pressure) 이란 인체의 동맥혈관에 흐르는 혈액의 압력을 말합니다. 이 혈압은 혈액을 순환시키는 펌프인 심장으로 인하여 생깁니다. 심장은 주기적으로 수축과 확장을 되풀이하면서 혈액을 보내므로 혈압은 한 번의 맥박마다 최고값 (수축기혈압) 과 최저값 (확장기혈압) 이 있습니다. 혈압은 둘을 함께 표기하여 "수축기혈압 (mmHg) / 확장기혈압 (mmHg)" 으로 나타냅니다. 인체의 모든 조직에 혈액이 원활하게 순환되려면 혈압이 적절히 유지되어야 합니다. 특히 인체의 가장 위에는 가장 중요한 장기인 뇌가 자리잡은 머리가 있으므로 혈압은 적어도 뇌의 혈액순환에 문제가 없을 정도는 되어야 합니다. 그러므로 혈압이 지나치게 높으면 문제가 되는 것입니다.', 
       '가지, 감자, 고구마, 귤, 김, 다시마, 두부, 땅콩, 마늘, 메밀, 무, 미역, 보리, 사과, 옥수수, 인삼, 율무, 조, 콩, 표고버섯, 호박', 
       '가공식품, 게, 과자, 도넛, 마요네즈, 맥주, 베이컨, 버터, 새우, 아이스크림, 오징어, 장아찌, 젓갈류, 조개, 초콜릿, 치즈, 케이크, 해삼, 햄');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        3, 
        '관상동맥경화', 
        '관상동맥이란 심장에 산소와 영양소를 공급하는 혈관으로 심장을 싸고 있는데, 이 동맥에 경화가 일어나면 이를 관상동맥경화라고 부릅니다. 심하면 혈관이 막혀 심근경색을 일으켜 심장근육이 죽습니다.', 
        '가지, 고등어, 귤, 귀리, 다시마, 두부, 마늘, 미역, 버섯, 사과, 삼치, 양배추, 양파, 콩, 꽁치, 참치', 
        '간, 과자, 돼지비계, 베이컨, 삼겹살, 소시지, 오징어, 젓갈류, 탄산음료, 핫도그');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        3, 
        '말초동맥경화', 
        '관상동맥이란 심장에 산소와 영양소를 공급하는 혈관으로 심장을 싸고 있는데, 이 동맥에 경화가 일어나면 이를 관상동맥경화라고 부릅니다. 심하면 혈관이 막혀 심근경색을 일으켜 심장근육이 죽습니다.', 
        '가지, 고등어, 귤, 귀리, 다시마, 두부, 마늘, 미역, 버섯, 사과, 삼치, 양배추, 양파, 콩, 꽁치, 참치', 
        '간, 과자, 돼지비계, 베이컨, 삼겹살, 소시지, 오징어, 젓갈류, 탄산음료, 핫도그');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        3, 
        '뇌동맥경화', 
        '관상동맥이란 심장에 산소와 영양소를 공급하는 혈관으로 심장을 싸고 있는데, 이 동맥에 경화가 일어나면 이를 관상동맥경화라고 부릅니다. 심하면 혈관이 막혀 심근경색을 일으켜 심장근육이 죽습니다.', 
        '가지, 고등어, 귤, 귀리, 다시마, 두부, 마늘, 미역, 버섯, 사과, 삼치, 양배추, 양파, 콩, 꽁치, 참치', 
        '간, 과자, 과자, 돼지비계, 베이컨, 삼겹살, 소시지, 오징어, 젓갈류, 탄산음료, 핫도그');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '노인성빈혈', 
        '노인에서 나이에 따라 빈혈의 빈도가 증가합니다. 이것은 단백영양 결핍과 관계 있을 수 있다. 따라서 노인성 빈혈은 종양이나 다른 전신 질환이 있는지를 확인해야 합니다.\n노화한 조혈계는 작은 스트레스에도 민감하여 젊은 사람에 비해 질병이 쉽게 발생합니다.\n노인의 평균 혈색소치 ; 12.4∼15.3 g/dL\n노인의 정상 혈색소치의 하한값 ; 12 g/dL\n대부분의 무증상 노인에서 낮은 혈색소치는 대개 철결핍성 빈혈과 만성 질환에 의한 빈혈이 그 원인입니다.', 
        '감자, 건복숭아, 건살구, 건포도, 고기, 달걀, 돼지고기, 딸기, 망고, 멜론, 브로콜리, 상추, 생선, 쇠고기, 수박, 시금치, 양배추, 오렌지, 조개, 치즈, 키위, 토마토, 후추', 
        '녹차, 커피, 청량음료, 홍차');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '변비', 
        '변비란 하나의 질환이라기보다는 증세라고 볼 수 있습니다. 일반적으로 변을 보는 횟수가 감소하거나 변보고 싶은 생각이 없는 상태를 변비라고 합니다. 엄격히 설명하면 아래 다섯 가지 사항 중 두 가지 이상이 3개월 이상 지속되는 경우 만성 변비라 정의합니다.\n대변보는 횟수가 1주일에 2회 이하\n하루에 본 대변의 양이 35gm 이하\n전체 배변 횟수 중 25%이상에서 굉장히 많은 힘을 주어야 변이 나오는 경우\n전체 배변 횟수 중 25%이상에서 딱딱하고 굳은 변이 나오는 경우\n전체 배변 횟수 중 25%이상에서 배변이 끝난 후에도 여전히 변이 남아 있는 느낌', 
        '귀리, 고구마, 매실, 사과, 양배추, 오렌지, 배, 파파야, 키위', 
        '감, 바나나, 돼지고기, 소고기, 술, 커피');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '신장병', 
        '신장이 하는 일은 오줌을 만들어 체내의 노폐물을 체외로 내보내는 것입니다. 노폐물이 혈액 중에 너무 많아지는 것을 막기 위해 신장에서 오줌이 계속 만들어지고 있습니다. 오줌 속에는 노폐물 외에 혈액 중에서 남는 비타민이나 호르몬도 포함됩니다. 예컨대 체내의 노폐물과 지나친 불필요 물질을 버리는 일을 합니다. 그러므로 신장의 기능이 떨어지면 노폐물을 배설할 수가 없으므로 요독증 같은 병을 일으키게 되는 것입니다.', 
        '가지, 검은깨, 검은콩, 레드비트, 블루베리, 사과, 샐러리, 수박, 딸기, 마늘, 아스파라거스, 오디, 옥수수수염, 양배추, 양상추, 양파, 콜리플라워, 팥, 파인애플, 포도, 크랜베리, 밤, 율무', 
        '감자, 고구마, 근대, 바나나, 베이컨, 아보카도, 살구, 소시지, 마른오징어, 멸치, 육포, 치즈, 청량음료, 키위, 오렌지, 쑥, 건포도, 햄');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '위염', 
        '위염에는 급성과 만성 증세가 있는데, 급성 위염은 위벽을 보호해주는 점막이 헐어서 나타나는 증상입니다. 과음, 과식, 식중독 등이 그 원인으로 가슴이 타는 듯한 통증이 느껴지고 구역질이 나며, 설사를 하기 쉽습니다. 만성 위염은 위가 조이는 느낌이 있어 식욕이 뚝 떨어지고, 가슴이 쓰리거나 구역질이 나는 증세를 보입니다.', 
        '김치, 두부, 마, 무우, 브로콜리, 사과, 생강, 연근, 애호박, 양배추, 단호박, 토마토, 흰살생선', 
        '고구마, 바나나, 밀가루, 술, 청량음료, 커피, 현미');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '위궤양', 
        '위궤양은 위염이 오래가면 위 점막이 점점 더 헐어 위궤양이 됩니다. 한 번 상한 위 점막은 심하면 움푹 파여서 음식물을 소화시키기가 어렵습니다. 식사하고 1∼2시간 후 속이 쓰리고 아픈 십이지장궤양과는 달리 위궤양은 식사하자마자 배가 아픈 경우가 많습니다.', 
        '김치, 두부, 마, 무우, 브로콜리, 사과, 생강, 연근, 애호박, 양배추, 단호박, 토마토, 흰살생선', 
        '고구마, 바나나, 밀가루, 술, 청량음료, 커피, 현미');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '위무력증', 
        '위무력증은 음식을 먹을 때는 아무렇지도 않은데, 조금만 먹어도 속이 더부룩해지고 결국 소화제를 찾는 사람들이 있습니다. 이러한 증상은 위가 무력하고 힘이 없어 먹은 음식을 잘 소화시키지 못하기 때문에 나타납니다. 사람으로 치면 맥이 풀려서 아무것도 할 수 없는 상태입니다.', 
        '김치, 두부, 마, 무우, 브로콜리, 사과, 생강, 연근, 애호박, 양배추, 단호박, 토마토, 흰살생선', 
        '고구마, 바나나, 밀가루, 술, 청량음료, 커피, 현미');

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
        4, 
        '신경성 위염', 
        '신경성 위염은 생각을 지나치게 많이 하는 사람들은 인체의 기운이 정체됨에 따라 위장의 근육 활동력이 저하되어 소화불량 증세를 보입니다. 동의보감에도 "생각을 많이 하면 비장이 상한다"라는 구절이 있는데, 이를 보면 신경을 많이 쓰는 사람에게 위장 질환이 쉽게 온다는 것을 알 수 있습니다.', 
        '김치, 두부, 마, 무우, 브로콜리, 사과, 생강, 연근, 애호박, 양배추, 단호박, 토마토, 흰살생선', 
        '고구마, 바나나, 밀가루, 술, 청량음료, 커피, 현미');
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '골다공증', 
        '골다공증은 심혈관계질환 및 유방암, 자궁암과 함께 여성의 건강을 위협하는 대표적인 질환 입니다. 인체의 대들보 역할을 하는 뼈의 구성성분인 칼슘과 인 등 뼈를 이루고 있는 구성물질들이 점차적으로 빠져 나가서 전체적인 뼈에 구멍이 생기는 것을 말합니다. 갱년기가 되어 에스트로겐 호르몬이 감소하면, 뼈에서 칼슘과 단백질이 소실되어 뼈가 약해지게 되며, 작은 충격에도 쉽게 골절이 될 수 있습니다.\n뼈의 손실은 폐경이 시작된후 3 - 6년 사이에 대부분 일어 나게 되며, 한번 손실된 뼈는 다시 회복되기 어렵습니다. 초기에는 아무런 증세도 없이 진행되는 골다공증은 45세 여성의 약 50%에서 이미 소리없이 진행되고 있으며, 75세 여성의 90%가 심각한 골다공증을 맞이하게 됩니다.', 
        '고등어, 귀리, 달걀, 두부, 미역, 브로콜리, 보리, 시금치, 샐러리, 아몬드, 아보카도, 캐슈넛, 피스타치오, 홍화씨, 흑미, 다시마, 연어, 올리브오일, 표고버섯, 땅콩, 찹쌀, 현미, 우유', 
        '과자, 담배, 라면, 소시지, 삼겹살, 술, 사탕, 케이크, 청량음료, 핫도그, 햄버거, 피자, 튀김, 커피');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '골조송증', 
        '연령이 많아지면서 뼈의 위축이 커져 동작에 지장을 주는 상태를 말하는데 해부학적으로는 뼈가 희박해진 상태로서 골조송증은 골절을 가져올 만큼 중증이 되기도 하는데 여성에 있어서 폐경후 골손실은 가속적으로 일어납니다.', 
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '골연화증', 
        '전신성 골질환으로서 골구조에 있어 석회화가 일어나지 않는 질환으로 증상은 골절이 쉽고 뼈의 통증이 있는데 골연화증의 많은 증례는 비타민D의 부족 또는 흡수 불량이 원인이 됩니다.', 
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '변형성 관절염', 
        '노인의 관절염 중에서 가장 주된 것으로 그 원인은 환경, 유전, 비만 등이며 주요 병리변화는 연골의 상실에 있습니다', 
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '류마티성 관절염', 
        '이는 중년에 비롯하여 노년기에도 지속되는 것으로 일반적으로 통증, 종창, 피하 결절이 주증상이며 노인에 있어서는 고도의 골조송증이 있어 더욱 증상을 악화 시킵니다.', 
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '노인성 고관절증', 
        '노인의 고관절에 생기는 퇴행성 관절증으로 심한 동통과 장애를 초래하는 질환으로 방사선 촬영에서 60 세 이상의 약 절반에서 퇴행성변화가 나타나지만 증상을 호소하는 경우는 드뭅니다. 일반적으로 증상은 서서히 나타나며 초기에는 고관절 둘레의 동통과 가벼운 경직감을 호소하고 심한 운동후에는 대퇴부나 무릎관절부위로 내려가는 동통을 느끼게 되는데 이는 휴식을 취하면 저절로 낫는 것이 보통이다. 질병이 진행되면서 증상이 심해지고 고관절의 병변이 진행되면서 증상이 심해져, 고관절의 굴절나 내변기형과 운동제한을 초래합니다.', 
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '노인성 골조송증', 
        '노인성 골다공증 또는 노년기 골조송증이라고도 하며 골조송증은 골기질, 골질량이 정상보다 적은 상태를 말하는데 골조송증이 원인은 여러 가지이지만 노인성 골조송증은 일차성 골조송증중 65세 이상의 노인층이 대부분입니다. 임상적 특징은 골절하기 쉬운 점이며, 척추 주요부와 대퇴골 경부, 장골의 뼈줄기 끝등에 주로 발생합니다.\n그 결과 요통, 신장단축, 척추후굴 등이 일어나는데 일반적으로 나이가 많아짐에 따른 골질량의 감소는 30-40세에서, 여자쪽이 좀더 많으며 노년기에는 청년기 골질량의 15%이상 감소한다고 되어 있으며, 이 질환의 원인은 골개변에 있어서 골흡수가 골형성을 밑돌기 때문이라고 생각됩니다.', 
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '퇴행성슬관절염', 
        '슬관절의 퇴행성 변화에 의해 관절의 염증이 생겨 여러 가지 증상이 나타나는 상태로, 관절 연골의 퇴행성 변화는 고령에 기인한 것 외에도 슬관절부의 병변이나 손상(반월상 연골 손상, 골절, 탈구, 관절 내 유리 체), 비만증, 내, 외반 슬 등 기계적 부하 축의 이상, 감염증 또는 여러 가지 관절염 등에 의해서도 촉진됩니다.\n증상은 슬관절의 동통과 이상음이 조기 증상으로 나타납니다. 계단 오르내리기, 기립하기가 힘들게 됩니다. 활액막의 비후, 관절 액의 증가, 근 경련 등이 오고 결국 근 위축, 운동 제한, 관절 잠김(관절을 펴지도 굽히지도 못하는 상태)과 같은 소견이 나타납니다.',
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '퇴행성고관절염', 
        '관절의 퇴행성 변화에 의해 관절의 염증이 생겨 여러가지 증상이 나타나는 것으로, 특히 우리나라의 경우 원발성 퇴행성 고관절염의 빈도는 극히 낮습니다. 그러나 발달성 고관절 이형성증, 선천성 혹은 발달성 내반고, 대퇴 골두 골단 분리증, 화농성 혹은 결핵성 고관절염, Legg-Calve-Perthes 병, 대퇴 골두 무혈성 괴사, 외상성 변형 등에 의한 속발성 퇴행성 고관절염은 비교적 흔합니다.\n증상은 초기에는 하루나 이틀 정도 지속되는 통증이 간헐적으로 나타나는데, 이 경우 통증의 시작이 장거리 보행 등 육체적 과로와 자주 연관됩니다. 통증은 춥고 습기가 많은 날씨에 심해지는데, 대퇴 전방 및 내측을 따라 슬관절 내측부까지 뻗칠 수 있습니다. 병이 진행됨에 따라 통증의 발현 간격이 줄어들고, 주위 근육의 긴장에 의한 굴곡, 내전, 내 회전 변형과 관절 운동 제한이 생기며 파행이 나타납니다. 원발성의 경우는 진행이 늦지만 속발성은 일단 증상이 시작되면 급속히 악화되는 것이 보통입니다.',
        '달걀, 브로콜리, 마늘, 목이버섯, 부추, 양송이버섯, 양파, 치즈, 표고버섯, 우유, 창담귀', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '요통', 
        '요통은 허리에서 발생하는 통증으로 대부분의 사람이 일생에서 한번쯤은 느껴보았을 정도로 아주 흔한 병입니다. 그 지속기간에 따라 급성, 아급성, 만성으로 나뉘며, 요통만 있는 경우와, 요통과 다리로 통증이 뻗치는 방사통이 있는 경우(좌골신경통), 또는 다리의 통증만 있는 경우가 있습니다.', 
        '고구마, 귀리, 다시마, 두부, 달걀, 미역, 버섯, 블루베리, 브로콜리, 보리, 간, 연어, 아몬드, 파인애플, 호박, 해바라기씨, 요거트, 우유, 현미', 
        '과자, 담배, 라면, 베이컨, 소시지, 삼겹살, 술, 사탕, 케이크, 커피, 청량음료, 핫도그, 햄버거, 튀김, 피자');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '통풍', 
        '어떤 원인이든지 몸 안의 혈액에 요산 성분이 높아지고 그로 인해 조직 특히 관절이나, 신장에 요산 성분이 많이 침착되는 상태를 통풍이라고 합니다.\n이런 통풍은 조기에 발견하여 치료하면 건강하게 일생을 보낼 수 있으므로 통풍에 대한 일반적인 지식과 조기 진단 및 예방조치가 상당히 중요합니다.', 
        '딸기, 당근, 블루베리, 바나나체리, 사과, 오렌지, 연어, 굴비, 아몬드, 치즈, 커피, 파인애플, 호두, 참깨, 땅콩', 
        '게, 고등어, 내장, 돼지고기, 닭고기, 멸치, 오징어, 삼치, 술, 양고기, 청량음료, 치킨, 조개, 새우, 과일주스, 쇠고기, 꽁치');   

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		5, 
        '오십견', 
        '오십견은 어깨에 통증이 있고, 어깨 관절 움직임에 장애가 생기는 질병으로, 전 인구의 2-5%에서 오십견을 앓고 있을 정도로 흔한 질병입니다.\n오십견은 흔히 40대에서 60대에 걸쳐 잘 생기고, 50대에 가장 많이 생긴다고 하여 오십견이라 이름 붙여졌습니다. 오십견을 지칭하는 다른 이름으로는 "유착성 관절낭염", "동결견" 등이 있습니다.', 
        '달걀, 다시마, 두부, 브로콜리, 버섯, 미역, 시금치, 우유, 고등어, 콩, 현미, 호두, 연어, 참치, 치즈', 
        '담배, 녹차, 술, 커피, 홍차'); 
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		6, 
        '뇌경색', 
        '뇌경색(허혈성 뇌졸중)은 뇌혈관이 막혀서 생기는 뇌졸중으로, 혈전(피떡)에 의해 혈관이 서서히 좁아지다가 막혀서 생기는 혈전성 뇌경색과 혈전이 심장이나 목의 큰 혈관에서 생긴 후 떨어져 나와 뇌혈관을 막게 되는 색전성 뇌경색, 오래된 고혈압에 의해서 뇌 안의 작은 동맥이 손상되어 막히는 열공성 뇌경색으로 나눌 수 있습니다.\n갑자기 생기는 것이 특징입니다. 흔한 증상들은 반신마비(몸 한쪽의 힘이 빠지거나 감각이 이상하게 느껴짐), 실어증(말을 못하거나 못 알아 들음), 발음장애, 연하곤란(음식이나 침을 삼키기 어려움), 두통과 구토, 비틀거림, 시야장애, 의식장애, 어지럼증, 복시(물체가 둘로 보임) 등 입니다. 가끔 이러한 증상들이 생긴 후에 24시간내에 완전히 회복되는 수가 있는데 이를 일과성 허혈발작 이라고 부르며 곧 뇌경색이 생긴다는 경고신호라고 볼 수 있습니다.', 
        '가다랑어, 고구마, 고등어, 굴, 다시마, 딸기, 라즈베리, 블루베리, 사과, 시금치, 오이, 연어, 참치, 청어, 토마토, 파인애플, 키위, 정어리, 배', 
        '고추장, 된장, 라면, 빵, 젓갈, 장아찌, 햄버거');       
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		6, 
        '뇌출혈', 
        '고혈압에 의해서 뇌혈관이 터져서 생기는 뇌졸중입니다. 두통과 오심, 구토가 흔하며 그 외에는 뇌경색과 비슷합니다. 진단을 위해서는 먼저 신경과 의사의 정확한 진찰을 받은 후에 뇌의 전산화 단층촬영(CT), 자기공명영상(MRI와 MRA)과 같은 검사가 필요합니다.', 
        '가다랑어, 고구마, 고등어, 굴, 다시마, 당근, 딸기, 블루베리, 라즈베리, 사과, 시금치, 오이, 연어, 정어리, 참치, 청어, 토마토, 파인애플, 키위, 배', 
        '고추장, 된장, 라면, 빵, 젓갈, 장아찌, 햄버거');  
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		6, 
        '지주막하출혈', 
        '뇌혈관 벽이 부분적으로 약해져서 생기는 꽈리모양의 뇌동맥류나 뇌혈관 기형이 터져서 뇌와 그것을 둘러싼 막 사이에 출혈이 생기는 뇌졸중입니다. 증상은 갑자기 시작되는 심한 두통과 오심, 구토, 의식감소 등입니다.', 
        '가다랑어, 고구마, 고등어, 굴, 다시마, 당근, 딸기, 라즈베리, 블루베리, 사과, 시금치, 오이, 연어, 정어리, 참치, 청어, 토마토, 파인애플, 키위, 배', 
        '된장, 고추장, 라면, 빵, 젓갈, 장아찌, 햄버거');  

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		6, 
        '치매', 
        '치매는 정상적인 지적능력을 가지고 살아오던 사람이 서서히 만성적으로 기억력, 공간 지각능력, 사고력, 계산능력, 언어능력 및 판단력 등을 포함하는 뇌기능이 손상됨으로 인해 더이상 정상적인 가족관계를 유지하지 못하며, 직업적인 업무 수행과 대인관계 등을 유지할 수 없는 지경에 이르게 되는 상태를 말합니다. 신경과 외래를 찾는 환자 중에는 "혹시 자신이 치매가 아닌가?" 걱정하시는 분들이 많습니다. 최근에 건망증이 심해져서 평소에 잘 알고 있던 친구의 전화번호를 잊어버려 한참만에 기억해 내었다든지, 직장에 출근해보니 중요한 것을 집에 놓고 온 것이 기억나서 애태운 적이 있다는 것이 보통입니다.\n이렇게 기억력이 떨어져 치매의 초기 증상이 아닐까 걱정하는 분들이 의외로 많습니다. 하지만 기억력의 장애가 대개 사소한 일에 국한되고, 이로 인해 일상적인 사회생활에 중대한 장애를 주지 않고, 정신을 집중하거나 메모를 통해 극복할 수 있다면 이는 정상적인 노화과정의 결과이지 치매라고 할 수는 없습니다.', 
        '건포도, 고등어, 근대, 블루베리, 브로콜리, 아몬드, 아마씨, 아스파라거스, 자두, 잣, 땅콩, 들기름, 올리브오일, 삼치, 정어리, 전갱이, 꽁치, 시금치, 토마토, 딸기', 
        '과자, 담배, 마가린, 마요네즈, 버터, 삼겹살, 술, 치킨, 치즈, 햄버거');  
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		6, 
        '파킨슨병', 
        '파킨슨병이란 근육의 강직, 진전증(떨림증), 몸동작이 느려지는 운동완서가 대표적 증상인 퇴행성 신경질환입니다. 뇌동맥경화증, 일산화탄소 중독에서도 비슷한 증세가 나타나는데, 이 증세들까지 포함해서 부를 때는 파킨슨 증후군이라고 합니다. 1817년 제임스 파킨슨이란 영국 의사에 의해 처음 하나의 질환군으로 언급되었습니다.', 
        '고구마, 고등어, 꽁치, 딸기, 블루베리, 연어, 청어, 땅콩, 잣, 호두', 
        '닭가슴살, 마가린, 버터, 소시지, 우유, 치즈, 요거트');  
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		7, 
        '개방각 녹내장', 
        '눈의 방수배출구가 점진적으로 망가지면서 진행이 서서히 되는 가장 흔한 녹내장입니다. 안압이 높으나 자각증세가 초기에는 없고 눈도 겉으로 보기에는 정상으로 보이므로 환자가 전혀 모르는 사이에 진행이 되는 경우가 많습니다. 만약 적절한 치료를 받지 않으면 급기야 시력을 상실하게 됩니다.', 
        '가지, 검은콩, 고등어, 닭고기, 달걀, 들기름, 견과류, 멸치, 바나나, 블루베리, 사과, 적포도, 땅콩, 꿀', 
        '밀가루, 사탕, 설탕, 아이스크림, 커피, 청량음료, 케이크, 홍차');        
        
INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		7, 
        '폐쇄각 녹내장', 
        '대부분 급성으로 발병하는 녹내장으로, 눈의 방수배출구가 갑자기 막혀 안압이 급속도로 증가하고 오심, 구토, 어깨결림, 심한 안통 및 두통 등을 호소하게 되며 밝은 빛을 보면 주위에 무지개 같은 것이 보이면서 시력도 급작스럽게 떨어지게 됩니다. 이런 경우 빨리 응급처치를 받아야 하겠습니다.', 
        '가지, 검은콩, 고등어, 닭고기, 달걀, 들기름, 견과류, 멸치, 바나나, 블루베리, 사과, 적포도, 땅콩, 꿀', 
        '밀가루, 사탕, 설탕, 아이스크림, 커피, 청량음료, 케이크, 홍차');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		7, 
        '속발성 녹내장', 
        '눈의 외상, 염증, 종양이나 오래된 백내장 및 당뇨병 등에 의해 생기는 녹내장으로 치료방법도 녹내장을 유발한 요인에 따라 다르나 그 원인에 관계없이 방치하게 되면 실명하는 병입니다.', 
        '가지, 검은콩, 고등어, 닭고기, 달걀, 들기름, 견과류, 멸치, 바나나, 블루베리, 사과, 적포도, 땅콩, 꿀', 
        '밀가루, 사탕, 설탕, 아이스크림, 커피, 청량음료, 케이크, 홍차');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		7, 
        '정상안압 녹내장', 
        '안압이 높지 않고 정상수준인데도 시신경이 망가져 시력을 상실하는 질환으로 아직까지 왜 시신경이 비교적 낮은 안압에서 망가지는지 확실히 알려져 있지 않고 있습니다. 역시 안압을 더욱 낮추어야만 시야가 망가지는 것을 방지할 수가 있습니다.', 
        '가지, 검은콩, 고등어, 닭고기, 달걀, 들기름, 견과류, 멸치, 바나나, 블루베리, 사과, 적포도, 땅콩, 꿀', 
        '밀가루, 사탕, 설탕, 아이스크림, 커피, 청량음료, 케이크, 홍차');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		7, 
        '선천 녹내장', 
        '태아시기에 눈의 방수배출로 구조가 제대로 만들어지지 않아 생기는 질환으로 신생아의 눈이 지나치게 크거나, 각막이 맑지 않으며, 눈물을 흘리는 경우 진찰을 받아 보아야 하고 확진되면 빨리 수술을 받는 것이 좋습니다.', 
        '가지, 검은콩, 고등어, 닭고기, 달걀, 들기름, 견과류, 멸치, 바나나, 블루베리, 사과, 적포도, 땅콩, 꿀', 
        '밀가루, 사탕, 설탕, 아이스크림, 커피, 청량음료, 케이크, 홍차');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		7, 
        '백내장', 
        '백내장은 눈 안쪽이 하얗게 변하는 장애라는 뜻으로, 노화 등으로 수정체가 하얗게 혼탁해지는 현상을 말한다. 50세 이상의 사람들에게 자주 보이며, 상당수의 75세 이상 노인층에게서 발병하는 흔한 질병. 당뇨병 합병증으로 오기도 하고, 자외선에 의해 오기도 하며, 외부의 충격이나 안와골절 등으로 발생하는 외상성 백내장도 있다. ', 
        '가지, 검은콩, 고등어, 닭고기, 달걀, 들기름, 견과류, 멸치, 바나나, 블루베리, 사과, 적포도, 땅콩, 꿀', 
        '밀가루, 사탕, 설탕, 아이스크림, 커피, 청량음료, 케이크, 홍차');        

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		8, 
        '당뇨병', 
        '인슐린량(量)의 부족으로 혈액 중의 포도당(혈당)이 정상인보다 그 농도가 높아져서 소변에 포도당을 배출하는 만성질환입니다.', 
        '가지, 귀리, 고등어, 닭가슴살, 메밀, 보리, 시금치, 아보카도, 연어, 정어리, 케일, 콩, 피망, 당근', 
        '과일, 과일주스, 밀가루, 빵, 시리얼, 술, 설탕, 청량음료');    

INSERT INTO 
	DISEASE(
		D_CATEGORY_NO, 
        D_NAME, 
        D_INFO, 
        D_GOOD_FOOD, 
        D_BAD_FOOD)
VALUES(
		9, 
        '노인성 우울증', 
        '주위에서 노인들이 기운이 없다고 하거나, 행동이 느려지고, 어디가 자꾸 아프다고 호소할 때, 우리는 흔히 "나이가 들만큼 들었으니까 그렇지" 하고 흘려 버리게 됩니다. 노인성 우울증 환자는 젊은 연령의 우울증 환자보다 자살 기도율이 훨씬 높으며, 자살의 성공률 또한 다른 질병들과 비교할 수 없을 만큼 높습니다.\n우울증을 앓는 기간이 오래 될수록 치료하기가 어려워질 수 있기 때문에, 조기에 발견하여 치료하는 것이 절실히 요구됩니다.\n다행히 우울증은 현재 적절한 항우울제 등을 사용하면서 정신과적인 치료를 병행할 경우에 비교적 치료 효과가 좋은 편이므로 노인들에 대한 애정어린 관심과 세심한 관찰을 통해서 조기에 발견하는 것이 중요하다고 하겠습니다.', 
        '고등어, 달걀, 브로콜리, 상추, 시금치, 아스파라거스, 오리고기, 케일, 닭고기, 정어리, 청어, 참치, 호두, 소간, 연어', 
        '커피, 술, 케이크, 쿠키, 햄버거, 피자');    