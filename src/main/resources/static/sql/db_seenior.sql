CREATE DATABASE DB_SEENIOR;

USE DB_SEENIOR;

SHOW TRIGGERS;

-- ADMIN 계정 테이블 ----------------------------------------------------------------------------------------------------------------------
CREATE TABLE ADMIN_ACCOUNT (
	NO 						INT AUTO_INCREMENT COMMENT "관리자 NO(PK)",								-- 관리자 NO(PK)
	ID 						VARCHAR(100) NOT NULL UNIQUE COMMENT "관리자 ID(E-MAIL)",					-- 관리자 ID(E-MAIL)
	PW 						VARCHAR(200) NOT NULL COMMENT "관리자 비밀번호",							-- 관리자 비밀번호
	NAME	 				VARCHAR(50) NOT NULL COMMENT "관리자 이름",								-- 관리자 이름
	PHONE 					VARCHAR(50) NOT NULL COMMENT "관리자 연락처",								-- 관리자 연락처
	DEPARTMENT 				VARCHAR(50) NOT NULL COMMENT "관리자 부서",								-- 관리자 부서
	LEVEL 					VARCHAR(50) NOT NULL COMMENT "관리자 직급",								-- 관리자 직급
	POSITION 				VARCHAR(50) NOT NULL COMMENT "관리자 직책",								-- 관리자 직책
	AUTHORITY_ROLE 			VARCHAR(50) DEFAULT "NOT_APPROVED" COMMENT "관리자 인가 권한명", 			-- 관리자 인가 권한명
	ISACCOUNTNONEXPIRED 	TINYINT DEFAULT 1 COMMENT "관리자 계정 만료 유무(만료X = 1, 만료 = 0)",			-- 관리자 계정 만료 유무(만료X = 1, 만료 = 0)
	ISACCOUNTNONLOCKED 		TINYINT DEFAULT 1 COMMENT "관리자 계정 잠김 유무(잠김X = 1, 잠김 = 0)",			-- 관리자 계정 잠김 유무(잠김X = 1, 잠김 = 0)
	ISCREDENTIALSNONEXPIRED TINYINT DEFAULT 1 COMMENT "관리자 자격 증명 만료 유무(만료X = 1, 만료 = 0)",		-- 관리자 자격 증명 만료 유무(만료X = 1, 만료 = 0)
	ISENABLED 				TINYINT DEFAULT 1 COMMENT "관리자 계정 사용 가능 유무 (사용 = 1, 사용X = 0)",	-- 관리자 계정 사용 가능 유무 (사용 = 1, 사용X = 0)
	IS_DELETED 				TINYINT DEFAULT 1 COMMENT "관리자 계정 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)",		-- 관리자 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)
	REG_DATE 				DATETIME DEFAULT NOW() COMMENT "관리자 등록일",								-- 관리자 등록일
	MOD_DATE 				DATETIME DEFAULT NOW() COMMENT "관리자 수정일",								-- 관리자 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM ADMIN_ACCOUNT;
SHOW INDEX FROM ADMIN_ACCOUNT;

		SELECT 
			* 
		FROM 
			ADMIN_ACCOUNT 
		WHERE 
			AUTHORITY_ROLE = "SUB_ADMIN"
		AND 
			AUTHORITY_ROLE = "NOT_APPROVED"
		AND 
			IS_DELETED = 1;


-- ADMIN 인가 테이블(관리자 권한) --------------------------------------------------------------------------------------------------------------
CREATE TABLE ADMIN_AUTHORITY (
	NO 			INT AUTO_INCREMENT COMMENT "관리자 권한 NO",			-- 관리자 권한 NO
	ROLE_NAME 	VARCHAR(50) NOT NULL UNIQUE COMMENT "관리자 권한명",	-- 관리자 권한명
    PRIMARY KEY(NO)
);

SELECT * FROM ADMIN_AUTHORITY;
SHOW INDEX FROM ADMIN_AUTHORITY;

INSERT INTO ADMIN_AUTHORITY(ROLE_NAME) VALUES("SUPER_ADMIN");		-- 최고 권한 관리자
INSERT INTO ADMIN_AUTHORITY(ROLE_NAME) VALUES("ADMIN");				-- 일반 관리자
INSERT INTO ADMIN_AUTHORITY(ROLE_NAME) VALUES("NOT_APPROVED");		-- 승인 되지 않은 관리자



-- USER 계정 테이블 -------------------------------------------------------------------------------------------------------------------------
CREATE TABLE USER_ACCOUNT (
	NO 						INT AUTO_INCREMENT COMMENT "유저 NO(PK)", 								-- 유저 NO(PK)
	ID 						VARCHAR(100) NOT NULL UNIQUE COMMENT "유저 ID(E-MAIL)", 					-- 유저 ID(E-MAIL)
	PW 						VARCHAR(200) NOT NULL COMMENT "유저 비밀번호", 								-- 유저 비밀번호
	NAME 					VARCHAR(100) NOT NULL COMMENT "유저 이름", 								-- 유저 이름
	PHONE 					VARCHAR(100) NOT NULL COMMENT "유저 연락처", 								-- 유저 연락처
	NICKNAME 				VARCHAR(255) NOT NULL UNIQUE COMMENT "유저 닉네임(별명)", 					-- 유저 닉네임(별명)
	GENDER 					CHAR NOT NULL COMMENT "유저 성별",										-- 유저 성별
	BIRTH 					DATE NOT NULL COMMENT "유저 생년월일",										-- 유저 생년월일
	ADDRESS 				VARCHAR(255) COMMENT "유저 주소",											-- 유저 주소
    PROFILE_IMG 			VARCHAR(255) COMMENT "유저프로필 이미지 파일 명",								-- 유저 프로필 이미지 파일 명
	COMPANY 				VARCHAR(50) COMMENT "유저 소속 기관",										-- 유저 소속 기관
	IS_PERSONAL 			TINYINT COMMENT "유저 개인회원 OR 기관회원 여부 (개인 = 1, 기관 = 0)",			-- 유저 개인회원 OR 기관회원 여부 (개인 = 1, 기관 = 0)
	SOCIAL_ID 				VARCHAR(200) COMMENT "유저 3자 로그인 ID",									-- 유저 3자 로그인 ID
	IS_BLOCKED 				TINYINT DEFAULT 1 COMMENT "유저 계정 정지 여부 (기본값 = 1. 정지 시 = 0)",		-- 유저 계정 정지 여부 (기본값 = 1. 정지 시 = 0)
	ISACCOUNTNONEXPIRED 	TINYINT DEFAULT 1 COMMENT "유저 계정 만료 유무(만료X = 1, 만료 = 0)",			-- 유저 계정 만료 유무(만료X = 1, 만료 = 0)
	ISACCOUNTNONLOCKED 		TINYINT DEFAULT 1 COMMENT "유저 계정 잠김 유무(잠김X = 1, 잠김 = 0)",			-- 유저 계정 잠김 유무(잠김X = 1, 잠김 = 0)
	ISCREDENTIALSNONEXPIRED TINYINT DEFAULT 1 COMMENT "유저 계정 자격 증명 만료 유무(만료X = 1, 만료 = 0)",	-- 유저 자격 증명 만료 유무(만료X = 1, 만료 = 0)
	ISENABLED 				TINYINT DEFAULT 1 COMMENT "유저 계정 사용 가능 유무 (사용 = 1, 사용X = 0)",		-- 유저 계정 사용 가능 유무 (사용 = 1, 사용X = 0)
	IS_DELETED 				TINYINT DEFAULT 1 COMMENT "유저 계정 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)",		-- 유저 계정 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)
	REG_DATE 				DATETIME DEFAULT NOW() COMMENT "유저 등록일",								-- 유저 등록일
	MOD_DATE 				DATETIME DEFAULT NOW() COMMENT "유저 수정일", 								-- 유저 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM USER_ACCOUNT;
SHOW INDEX FROM USER_ACCOUNT;



-- 게시판 카테고리 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_CATEGORY (
	NO			INT AUTO_INCREMENT COMMENT "게시판 NO(PK)",						-- 게시판 NO(PK)
	NAME		VARCHAR(100) NOT NULL UNIQUE COMMENT "게시판 명",					-- 게시판 명
	IDX			INT COMMENT "게시판 정렬 순서",										-- 게시판 정렬 순서
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "게시판 삭제 여부 (기본값 = 0, 삭제 시 = 1)",	-- 게시판 삭제 여부 (기본값 = 1, 삭제시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "게시판 등록일",						-- 게시판 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "게시판 수정일",						-- 게시판 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_CATEGORY;
SHOW INDEX FROM BOARD_CATEGORY;

SHOW TRIGGERS;

-- IDX (게시판 정렬 순서) AUTO_INCREMENT 트리거
DELIMITER //

CREATE TRIGGER BEFORE_INSERT_BOARD_CATEGORY
BEFORE INSERT ON BOARD_CATEGORY
FOR EACH ROW
BEGIN
    SET NEW.IDX = (SELECT IFNULL(MAX(IDX), 0) + 1 FROM BOARD_CATEGORY);
END//

DELIMITER ;



-- 일반 게시판 게시물 테이블 -----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_CONTENTS (
	NO				INT	AUTO_INCREMENT COMMENT "게시물 NO(PK)",										-- 게시물 NO(PK)
	CATEGORY_NO		INT NOT NULL COMMENT "게시판 NO(BOARD_CATEGORY TABLE PK)",						-- 게시판 NO(BOARD_CATEGORY TABLE PK)
	TITLE			VARCHAR(200) NOT NULL COMMENT "게시물 제목", 										-- 게시물 제목
	BODY			TEXT NOT NULL COMMENT "게시물 본문", 												-- 게시물 본문
	WRITER_NO		INT NOT NULL COMMENT "게시물 작성자 NO(USER_ACCOUNT TABLE PK)", 					-- 게시물 작성자 NO(USER_ACCOUNT TABLE PK)
	REPORT_STATE	TINYINT DEFAULT 1 COMMENT "게시물 신고 진행 상태(기본값 = 1, 처리중 = 2, 처리 완료 = 0)",	-- 게시물 신고 진행 상태(기본값 = 1, 처리중 = 2, 처리 완료 = 0)
	VIEW_CNT		INT DEFAULT 0 COMMENT "게시물 조회수", 												-- 게시물 조회수 
	IS_DELETED		TINYINT DEFAULT 1 COMMENT "게시물 삭제 여부(기본값 = 1, 삭제 시 = 0)",					-- 게시물 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE		DATETIME DEFAULT NOW() COMMENT "게시물 등록일",										-- 게시물 등록일
	MOD_DATE		DATETIME DEFAULT NOW() COMMENT "게시물 수정일",										-- 게시물 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_CONTENTS;
SHOW INDEX FROM BOARD_CONTENTS;



-- 일반 게시판 댓글 테이블 ------------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_COMMENTS (
	NO			INT AUTO_INCREMENT COMMENT "댓글 NO(PK)", 							-- 댓글 NO(PK)
	CATEGORY_NO	INT	NOT NULL COMMENT "게시판 NO(BOARD_CATEGORY TABLE PK)", 			-- 게시판 NO(BOARD_CATEGORY TABLE PK)
	CONTENT_NO	INT	NOT NULL COMMENT "게시물 NO(BOARD_CONTENT TABLE PK)",				-- 게시물 NO(BOARD_CONTENT TABLE PK)
	STATE		TINYINT	NOT NULL COMMENT "댓글, 대댓글 여부(댓글 = 1, 대댓글 = 0)",		-- 댓글, 대댓글 여부(댓글 = 1, 대댓글 = 0)
	TEXT		VARCHAR(255) NOT NULL COMMENT "댓글 내용",							-- 댓글 내용
	WRITER_NO	VARCHAR(255) NOT NULL COMMENT "댓글 작성자 NO(USER_ACCOUNT TABLE PK)",	-- 댓글 작성자 NO(USER_ACCOUNT TABLE PK)
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "댓글 삭제 여부(기본값 = 1, 삭제 시 = 0)",		-- 댓글 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "댓글 등록일",							-- 댓글 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "댓글 수정일",							-- 댓글 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_COMMENTS;
SHOW INDEX FROM BOARD_COMMENTS;



-- 일반 게시판 공지사항 테이블 ----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_NOTICE (
	NO			INT AUTO_INCREMENT COMMENT "공지사항 NO(PK)",							-- 공지사항 NO(PK)
	CATEGORY_NO	INT	NOT NULL COMMENT "공지사항이 등록될 게시판 NO(BOARD_CATEGORY TABLE)",	-- 공지사항이 등록 될 게시판 NO(BOARD_CATEGORY TABLE)
	TITLE		VARCHAR(255) NOT NULL COMMENT "공지사항 제목",							-- 공지사항 제목
	BODY		TEXT NOT NULL COMMENT "공지사항 내용",									-- 공지사항 내용
	WRITER_NO	INT	NOT NULL COMMENT "공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)",		-- 공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)
	VIEW_CNT	INT DEFAULT 0 COMMENT "공지사항 조회수", 			 					-- 공지사항 조회수
	STATE		TINYINT	DEFAULT 1 COMMENT "공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)",	-- 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "공지사항 등록일",						-- 공지사항 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "공지사항 수정일",						-- 공지사항 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_NOTICE;
SHOW INDEX FROM BOARD_NOTICE;



-- Q&A 게시판 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_QNA (
	NO			INT	AUTO_INCREMENT COMMENT "Q&A NO(PK)",							-- Q&A NO(PK)
	TITLE		VARCHAR(200) NOT NULL COMMENT "Q&A 제목",							-- Q&A 제목
	BODY		TEXT NOT NULL COMMENT "Q&A 본문",									-- Q&A 본문
	USER_NO		VARCHAR(255) NOT NULL COMMENT "Q&A 작성자 NO(USER_ACCOUNT TABLE PK)",	-- Q&A 작성자 NO(USER_ACCOUNT TABLE PK)
	STATE		TINYINT DEFAULT 1 COMMENT "Q&A 답변 여부(기본값 = 1, 답변 완료 = 0)",		-- Q&A 답변 여부(기본값 = 1, 답변 완료 = 0)
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "Q&A 삭제 여부(기본값 = 1, 삭제 시 = 0)",		-- Q&A 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 등록일",							-- Q&A 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 수정일",							-- Q&A 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_QNA;
SHOW INDEX FROM BOARD_QNA;



-- Q&A 게시판 답변 테이블 ----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_QNA_COMMNET (
	NO			INT	AUTO_INCREMENT COMMENT "Q&A 답변 NO(PK)",						-- Q&A 답변 NO(PK)
	QNA_NO		INT	NOT NULL COMMENT "Q&A NO(BOARD_QNA TABLE PK)",					-- Q&A NO(BOARD_QNA TABLE PK)
	ANSWER		TEXT NOT NULL COMMENT "Q&A 답변 내용",								-- Q&A 답변 내용
	ADMIN_NO	INT	NOT NULL COMMENT "Q&A 답변 작성자 NO(ADMIN_ACCOUNT TABLE PK)",		-- Q&A 답변 작성자 NO(ADMIN_ACCOUNT TABLE PK)
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "Q&A 답변 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- Q&A 답변 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 답변 등록일",						-- Q&A 답변 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 답변 수정일",						-- Q&A 답변 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_QNA_COMMNET;
SHOW INDEX FROM BOARD_QNA_COMMNET;



-- Q&A 게시판 공지사항 테이블 --------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_QNA_NOTICE (
	NO			INT	AUTO_INCREMENT COMMENT "Q&A 공지사항 NO(PK)", 						-- Q&A 공지사항 NO(PK)
	TITLE		VARCHAR(255) NOT NULL COMMENT "Q&A 공지사항 제목",							-- Q&A 공지사항 제목
	BODY		TEXT NOT NULL COMMENT "Q&A 공지사항 내용",									-- Q&A 공지사항 내용
	WRITER_NO	INT	NOT NULL COMMENT "Q&A 공지사항 작성자(ADMIN_ACCOUNT TABLE PK)",			-- Q&A 공지사항 작성자(ADMIN_ACCOUNT TABLE PK)
	VIEW_CNT	INT DEFAULT 0 COMMENT "Q&A 공지사항 조회수",			 					-- Q&A 공지사항 조회수
	STATE		TINYINT DEFAULT 1 COMMENT "Q&A 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)",	-- Q&A 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "Q&A 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- Q&A 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 공지사항 등록일",						-- Q&A 공지사항 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 공지사항 수정일",						-- Q&A 공지사항 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_QNA_NOTICE;
SHOW INDEX FROM BOARD_QNA_NOTICE;



-- 신고 게시판 테이블 -----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_REPORT (
	NO			INT	AUTO_INCREMENT COMMENT "신고 NO(PK)", 											-- 신고 NO(PK)
	CONTENT_NO	INT	NOT NULL COMMENT "신고된 게시글 NO(BOARD_CONTENT TABLE PK)",						-- 신고된 게시글 NO(BOARD_CONTENT TABLE PK)
	REASON		VARCHAR(255) NOT NULL COMMENT "신고 사유",											-- 신고 사유
	REPORTER_NO	VARCHAR(255) NOT NULL COMMENT "신고자 NO(USER_ACCOUNT TABLE PK)",						-- 신고자 NO(USER_ACCOUNT TABLE PK)
	RESULT		VARCHAR(255) COMMENT "신고 처리 결과(내용)",												-- 신고 처리 결과(내용)
	STATE		TINYINT DEFAULT 1 COMMENT "신고 진행 상태(기본값 = 1, 처리 중 = 2, 반려 = 3, 처리 완료 = 0)",	-- 신고 진행 상태(기본값 = 1, 처리 중 = 2, 반려 = 3, 처리 완료 = 0)
	IS_DELETED	TINYINT	DEFAULT 1 COMMENT "신고 취소 여부(기본값 = 1, 취소 시 = 0)", 						-- 신고 취소 여부(기본값 = 1, 취소 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "신고 등록일",											-- 신고 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "신고 수정일",											-- 신고 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM BOARD_REPORT;
SHOW INDEX FROM BOARD_REPORT;



-- 전체 공지사항 테이블 -----------------------------------------------------------------------------------------------------------------
CREATE TABLE NOTICE (
	NO			INT	AUTO_INCREMENT COMMENT "공지사항 NO(PK)", 						-- 공지사항 NO(PK)
	TITLE		VARCHAR(255) NOT NULL COMMENT "공지사항 제목",							-- 공지사항 제목
	BODY		TEXT NOT NULL COMMENT "공지사항 내용",									-- 공지사항 내용
	WRITER_NO	INT	NOT NULL COMMENT "공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)",		-- 공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)
	VIEW_CNT	INT DEFAULT 0 COMMENT "공지사항 조회수",			 					-- 공지사항 조회수
	STATE		TINYINT DEFAULT 1 COMMENT "공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)",	-- 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)
	IS_DELETED	TINYINT	DEFAULT 1 COMMENT "공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "공지사항 등록일",						-- 공지사항 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "공지사항 수정일",						-- 공지사항 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM NOTICE;
SHOW INDEX FROM NOTICE;



-- 질환 카테고리 테이블 --------------------------------------------------------------------------------------------------------------
CREATE TABLE DISEASE_CATEGORY (
	NO			INT	AUTO_INCREMENT COMMENT "질환 분류 NO(PK)", 						-- 질환 분류 NO(PK)
	NAME		VARCHAR(100) NOT NULL COMMENT "질환 분류 명",							-- 질환 분류 명
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "질환 분류 삭제 여부(기본값 = 0, 삭제 시 = 1)",	-- 질환 분류 삭제 여부(기본값 = 0, 삭제 시 = 1)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "질환 분류 등록일",						-- 질환 분류 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "질환 분류 수정일",						-- 질환 분류 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM DISEASE_CATEGORY;
SHOW INDEX FROM DISEASE_CATEGORY;



-- 질환 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE DISEASE (
	NO			INT	AUTO_INCREMENT COMMENT "질환 NO(PK)", 						-- 질환 NO(PK)
	CATEGORY_NO	INT	NOT NULL COMMENT "질환 분류 NO(DISEASE_CATEGORY TABLE PK)",	-- 질환 분류 NO(DISEASE_CATEGORY TABLE PK)
	NAME		VARCHAR(100) NOT NULL COMMENT "질환 명",							-- 질환 명
	INFO		TEXT NOT NULL COMMENT "질환 정보",								-- 질환 정보
	GOOD_FOOD	VARCHAR(100) NOT NULL COMMENT "질환에 좋은 추천 음식 재료",			-- 질환에 좋은 추천 음식 재료
	BAD_FOOD	VARCHAR(100) NOT NULL COMMENT "질환에 나쁜 비추천 음식 재료",			-- 질환에 나쁜 비추천 음식 재료
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "질환 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 질환 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "질환 등록일",						-- 질환 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "질환 수정일",						-- 질환 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM DISEASE;
SHOW INDEX FROM DISEASE;



-- 추천 영상 테이블 --------------------------------------------------------------------------------------------------------------
CREATE TABLE VIDEO (
	NO			INT	AUTO_INCREMENT COMMENT "추천 영상 NO(PK)", 						-- 추천 영상 NO(PK)
	DISEASE_NO	INT	NOT NULL COMMENT "질환 NO(DISEASE TABLE PK)",					-- 질환 NO(DISEASE TABLE PK)
	TITLE		VARCHAR(100) NOT NULL COMMENT "추천 영상 제목",							-- 추천 영상 제목
	TEXT		VARCHAR(255) NOT NULL COMMENT "추천 영상 설명",							-- 추천 영상 설명
	LINK		VARCHAR(255) NOT NULL COMMENT "추천 영상 URL",						-- 추천 영상 URL
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "추천 영상 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 추천 영상 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "추천 영상 등록일",						-- 추천 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "추천 영상 수정일",						-- 추천 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM VIDEO;
SHOW INDEX FROM VIDEO;



-- 광고 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE ADVERTISEMENT (
	NO			INT	AUTO_INCREMENT COMMENT "광고 NO(PK)", 						-- 광고 NO(PK)
	POSITION	INT	NOT NULL COMMENT "광고 위치", 								-- 광고 위치
	IMG			VARCHAR(255) NOT NULL COMMENT "광고 사진",						-- 광고 사진
	URL			VARCHAR(255) NOT NULL COMMENT "광고 URL",						-- 광고 URL
	START_DATE	DATE NOT NULL COMMENT "광고 시작일",								-- 광고 시작일
	END_DATE	DATE NOT NULL COMMENT "광고 종료일",								-- 광고 종료일
	CLIENT		VARCHAR(100) NOT NULL COMMENT "광고주",							-- 광고주 
	IS_DELETED	TINYINT DEFAULT 1 COMMENT "광고 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 광고 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE	DATETIME DEFAULT NOW() COMMENT "광고 등록일",						-- 광고 등록일
	MOD_DATE	DATETIME DEFAULT NOW() COMMENT "광고 수정일",						-- 광고 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM ADVERTISEMENT;
SHOW INDEX FROM ADVERTISEMENT;



-- 환자 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE CARE_LIST (
	NO					INT	AUTO_INCREMENT COMMENT "환자 NO(PK)", 							-- 환자 NO(PK)
	USER_NO				INT	NOT NULL COMMENT "관리하는 USER NO(USER_ACCOUNT TABLE PK)",		-- 관리하는 USER NO(USER_ACCOUNT TABLE PK)
	`GROUP`				VARCHAR(50) COMMENT "환자 그룹",										-- 환자 그룹
	NAME				VARCHAR(255) NOT NULL COMMENT "환자 이름",							-- 환자 이름
	GENDER				CHAR(1) NOT NULL COMMENT "환자 성별",									-- 환자 성별 
	AGE					INT	NOT NULL COMMENT "환자 나이",										-- 환자 나이
	ADDRESS				VARCHAR(255) NOT NULL COMMENT "환자 실 거주지",							-- 환자 실 거주지
	BLOOD_TYPE			CHAR(3) NOT NULL COMMENT "환자 혈액형",								-- 환자 혈액형
	PHONE				VARCHAR(100) NOT NULL COMMENT "환자 연락처",							-- 환자 연락처
	EMERGENCY_CONTACT_1	VARCHAR(100) NOT NULL COMMENT "환자 비상 연락처 1. 최소 한개 필수",			-- 환자 비상 연락처 1. 최소 한개 필수
	EMERGENCY_CONTACT_2	VARCHAR(100) COMMENT "환자 비상 연락처 2",								-- 환자 비상 연락처 2
	EMERGENCY_CONTACT_3	VARCHAR(100) COMMENT "환자 비상 연락처 3",								-- 환자 비상 연락처 3
	EMERGENCY_CONTACT_4	VARCHAR(100) COMMENT "환자 비상 연락처 4",								-- 환자 비상 연락처 4
	FAVOR_FOOD			VARCHAR(100) COMMENT "환자 선호 음식",									-- 환자 선호 음식
	HATE_FOOD			VARCHAR(100) COMMENT "환자 비선호 음식",								-- 환자 비선호 음식
	DIABETIC_FOOD		TINYINT NOT NULL COMMENT "환자 당뇨식 유무",							-- 환자 당뇨식 유무
	MEDICATIONS			VARCHAR(100) COMMENT "환자 복용 중인 약",								-- 환자 복용중인 약
	HOSPITAL			VARCHAR(100) COMMENT "환자 담당 병원",									-- 환자 담당 병원
	DOCTOR				VARCHAR(100) COMMENT "환자 담당의",									-- 환자 담당의
	HOSPITAL_TEL		VARCHAR(100) COMMENT "환자 담당 병원 연락처",							-- 환자 담당 병원 연락처
	WORK_STATE			TINYINT NOT NULL COMMENT "환자 보행 상태",								-- 환자 보행 상태
	ASSISTIVE_DEVICE	VARCHAR(100) COMMENT "환자 보조 기구 유무", 								-- 환자 보조 기구 유무
	WASHING_ASSISTANCE	TINYINT NOT NULL COMMENT "환자 세면 도움 정도",							-- 환자 세면 도움 정도
	TOILET_ASSISTANCE	TINYINT NOT NULL COMMENT "환자 대소변 도움 정도",						-- 환자 대소변 도움 정도
	MENTAL_STATE		TINYINT NOT NULL COMMENT "환자 정신 건강 상태(우울증, 고립감 정도)",			-- 환자 정신 건강 상태(우울증, 고립감 정도)
	SOCIAL_STATE		TINYINT NOT NULL COMMENT "환자 사회적 교류 정도",						-- 환자 사회적 교류 정도
	ETC					VARCHAR(255) COMMENT "환자 기타 특이사항",								-- 환자 기타 특이사항
	FAVORITES			TINYINT DEFAULT 1 COMMENT "환자 즐겨찾기 여부(기본값 = 1, 즐겨찾기 시 = 0)",	-- 환자 즐겨찾기 여부 (기본값 = 1 즐겨찾기 시 = 0)
	IS_DELETED			TINYINT	DEFAULT 1 COMMENT "환자 삭제 여부(기본값 = 1, 즐겨찾기 시 = 0)",	-- 환자 삭제 여부(기본값 = 1, 즐겨찾기 시 = 0)
	REG_DATE			DATETIME DEFAULT NOW() COMMENT "환자 등록일",							-- 환자 등록일
	MOD_DATE			DATETIME DEFAULT NOW() COMMENT "환자 수정일",							-- 환자 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM CARE_LIST;
SHOW INDEX FROM CARE_LIST;



-- 환자 보유 질환 테이블 -------------------------------------------------------------------------------------------------------------
CREATE TABLE CARE_PERSEN_DISEASE (
	NO				INT	AUTO_INCREMENT COMMENT "환자 보유 질환 NO(PK)", 						-- 환자 보유 질환 NO(PK)
	CARE_LIST_NO	INT	NOT NULL COMMENT "환자 NO(CARE_LIST TABLE PK)",						-- 환자 NO(CARE_LIST TABLE PK)
	DISEASE_NO		INT	NOT NULL COMMENT "질환 NO(DISEASE TABLE PK)", 						-- 질환 NO(DISEASE TABLE PK)
	IS_DELETED		TINYINT	DEFAULT 1 COMMENT "환자 보유 질환 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 환자 보유 질환 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE		DATETIME DEFAULT NOW() COMMENT "환자 보유 질환 등록일",						-- 환자 보유 질환 등록일
	MOD_DATE		DATETIME DEFAULT NOW() COMMENT "환자 보유 질환 수정일",						-- 환자 보유 질환 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM CARE_PERSEN_DISEASE;
SHOW INDEX FROM CARE_PERSEN_DISEASE;



-- 일정 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE SCHEDULER (
	NO				INT	AUTO_INCREMENT COMMENT "일정 NO(PK)", 						-- 일정 NO(PK)
	USER_NO			INT	NOT NULL COMMENT "유저 NO(USER_ACCOUNT TABLE PK)",			-- 유저 NO(USER_ACCOUNT TABLE PK)
	CARE_LIST_NO	INT	NOT NULL COMMENT "환자 NO(CARE_LIST TABLE PK)",				-- 환자 NO(CARE_LIST TABLE PK)
	TITLE			VARCHAR(100) NOT NULL COMMENT "일정 제목",						-- 일정 제목
	COMMENT			VARCHAR(255) NOT NULL COMMENT "일정 상세",						-- 일정 상세
	ALARM_CHECK		TINYINT	DEFAULT 1 COMMENT "일정 알람 유무(기본값 = 1, 알람 시 = 0)",	-- 일정 알람 유무(기본값 = 1, 알람 시 = 0)
	ALARM_TIME		DATETIME COMMENT "일정 알람 시간",									-- 일정 알람 시간
	YEAR			INT NOT NULL COMMENT "일정 년도",									-- 일정 년도
	MONTH			INT	NOT NULL COMMENT "일정 월",									-- 일정 월
	DATE			INT	NOT NULL COMMENT "일정 일",									-- 일정 일
	START_DATE		DATE NOT NULL COMMENT "일정 시작일",								-- 일정 시작일
	END_DATE		DATE NOT NULL COMMENT "일정 종료일", 								-- 일정 종료일
	IS_DELETED		TINYINT DEFAULT 1 COMMENT "일정 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 일정 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE		DATETIME DEFAULT NOW() COMMENT "일정 등록일",						-- 일정 등록일
	MOD_DATE		DATETIME DEFAULT NOW() COMMENT "일정 수정일",						-- 일정 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM SCHEDULER;
SHOW INDEX FROM SCHEDULER;



-- 복용약 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE PRESCRIPTIONS (
	NO					INT	AUTO_INCREMENT COMMENT "복용 약 NO(PK)", 							-- 복용 약 NO(PK)
	USER_NO				INT	NOT NULL COMMENT "유저 NO(USER_ACCOUNT TABLE PK)",				-- 유저 NO(USER_ACCOUNT TABLE PK)
	CARE_LIST_NO		INT	NOT NULL COMMENT "환자 NO(CARE_LIST TABLE PK)",					-- 환자 NO(CARE_LIST TABLE PK)
	TITLE				VARCHAR(100) NOT NULL COMMENT "복용 약 알림 제목",						-- 복용 약 알림 제목
	COMMENT				VARCHAR(255) NOT NULL COMMENT "복용 약 알림 상세",						-- 복용 약 알림 상세
	ALARM_CHECK			TINYINT	DEFAULT 1 COMMENT "복용 약 알람 유무(기본값 = 1, 알람 시 = 0)",		-- 복용 약 알람 유무(기본값 = 1, 알람 시 = 0)
	ALARM_TIME_1		VARCHAR(100) COMMENT "복용 약 알람 시간 1",								-- 복용 약 알람 시간 1
	TAKING_IT_OR_NOT_1	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 1(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 1(기본값 = 1, 복용 시 = 0)
	ALARM_TIME_2		VARCHAR(100) COMMENT "복용 약 알람 시간 2",								-- 복용 약 알람 시간 2
	TAKING_IT_OR_NOT_2	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 2(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 2(기본값 = 1, 복용 시 = 0)
	ALARM_TIME_3		VARCHAR(100) COMMENT "복용 약 알람 시간 3",								-- 복용 약 알람 시간 3
	TAKING_IT_OR_NOT_3	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 3(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 3(기본값 = 1, 복용 시 = 0)
	ALARM_TIME_4		VARCHAR(100) COMMENT "복용 약 알람 시간 4",								-- 복용 약 알람 시간 4
	TAKING_IT_OR_NOT_4	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 4(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 4(기본값 = 1, 복용 시 = 0)
	ALARM_TIME_5		VARCHAR(100) COMMENT "복용 약 알람 시간 5",								-- 복용 약 알람 시간 5
	TAKING_IT_OR_NOT_5	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 5(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 5(기본값 = 1, 복용 시 = 0)	
    BEFORE_AFTER		TINYINT COMMENT "복용 약 식전 OR 식후(식전 = 1, 식후 = 0)",				-- 복용 약 식전 OR 식후(식전 = 1, 식후 = 0)
	START_DATE			DATE NOT NULL COMMENT "복용 시작일",									-- 복용 시작일
	END_DATE			DATE NOT NULL COMMENT "복용 종료일",									-- 복용 종료일
	IS_DELETED			TINYINT DEFAULT 1 COMMENT "복용 약 삭제 여부(기본값 = 1, 삭제 시 = 0)",		-- 복용 약 삭제 여부(기본값 = 1, 삭제 시 = 0)
	REG_DATE			DATETIME DEFAULT NOW() COMMENT "복용 약 등록일",						-- 복용 약 등록일
	MOD_DATE			DATETIME DEFAULT NOW() COMMENT "복용 약 수정일",						-- 복용 약 수정일
    PRIMARY KEY(NO)
);

SELECT * FROM PRESCRIPTIONS;
SHOW INDEX FROM PRESCRIPTIONS;


