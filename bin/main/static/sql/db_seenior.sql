CREATE DATABASE DB_SEENIOR;

USE DB_SEENIOR;

SHOW TRIGGERS;

-- ADMIN 계정 테이블 ----------------------------------------------------------------------------------------------------------------------
CREATE TABLE ADMIN_ACCOUNT (
	A_NO 						INT AUTO_INCREMENT COMMENT "관리자 NO(PK)",								-- 관리자 NO(PK)
	A_ID 						VARCHAR(100) NOT NULL UNIQUE COMMENT "관리자 ID(E-MAIL)",					-- 관리자 ID(E-MAIL)
	A_PW 						VARCHAR(200) NOT NULL COMMENT "관리자 비밀번호",							-- 관리자 비밀번호
	A_NAME	 					VARCHAR(50) NOT NULL COMMENT "관리자 이름",								-- 관리자 이름
	A_PHONE 					VARCHAR(50) NOT NULL COMMENT "관리자 연락처",								-- 관리자 연락처
	A_BIRTH 					DATE NOT NULL COMMENT "관리자 생년월일",									-- 관리자 생년월일
	A_DEPARTMENT 				VARCHAR(50) COMMENT "관리자 부서",											-- 관리자 부서
	A_LEVEL 					VARCHAR(50) COMMENT "관리자 직급",											-- 관리자 직급
	A_POSITION 					VARCHAR(50) COMMENT "관리자 직책",											-- 관리자 직책
	A_AUTHORITY_ROLE 			VARCHAR(50) DEFAULT "NOT_APPROVED" COMMENT "관리자 인가 권한명", 			-- 관리자 인가 권한명
	A_ISACCOUNTNONEXPIRED 		TINYINT DEFAULT 1 COMMENT "관리자 계정 만료 유무(만료X = 1, 만료 = 0)",			-- 관리자 계정 만료 유무(만료X = 1, 만료 = 0)
	A_ISACCOUNTNONLOCKED 		TINYINT DEFAULT 1 COMMENT "관리자 계정 잠김 유무(잠김X = 1, 잠김 = 0)",			-- 관리자 계정 잠김 유무(잠김X = 1, 잠김 = 0)
	A_ISCREDENTIALSNONEXPIRED 	TINYINT DEFAULT 1 COMMENT "관리자 자격 증명 만료 유무(만료X = 1, 만료 = 0)",		-- 관리자 자격 증명 만료 유무(만료X = 1, 만료 = 0)
	A_ISENABLED 				TINYINT DEFAULT 1 COMMENT "관리자 계정 사용 가능 유무 (사용 = 1, 사용X = 0)",	-- 관리자 계정 사용 가능 유무 (사용 = 1, 사용X = 0)
	A_IS_DELETED 				TINYINT DEFAULT 1 COMMENT "관리자 계정 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)",		-- 관리자 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)
	A_REG_DATE 					DATETIME DEFAULT NOW() COMMENT "관리자 등록일",								-- 관리자 등록일
	A_MOD_DATE 					DATETIME DEFAULT NOW() COMMENT "관리자 수정일",								-- 관리자 수정일
    PRIMARY KEY(A_NO)
);

SELECT * FROM ADMIN_ACCOUNT;
SHOW INDEX FROM ADMIN_ACCOUNT;
DROP TABLE ADMIN_ACCOUNT;

INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin10@seenior.com", "12345678", "admin10", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11@seenior.com", "12345678", "admin11", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin12@seenior.com", "12345678", "admin12", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin13@seenior.com", "12345678", "admin13", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin14@seenior.com", "12345678", "admin14", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin15@seenior.com", "12345678", "admin15", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin16@seenior.com", "12345678", "admin16", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin17@seenior.com", "12345678", "admin17", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin18@seenior.com", "12345678", "admin18", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin19@seenior.com", "12345678", "admin19", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin20@seenior.com", "12345678", "admin20", "010-1234-1234", "19870807");

INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin110@seenior.com", "12345678", "admin10", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin111@seenior.com", "12345678", "admin11", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin112@seenior.com", "12345678", "admin12", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin113@seenior.com", "12345678", "admin13", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin114@seenior.com", "12345678", "admin14", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin115@seenior.com", "12345678", "admin15", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin116@seenior.com", "12345678", "admin16", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin117@seenior.com", "12345678", "admin17", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin118@seenior.com", "12345678", "admin18", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin119@seenior.com", "12345678", "admin19", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin120@seenior.com", "12345678", "admin20", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1102@seenior.com", "12345678", "admin10", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1111@seenior.com", "12345678", "admin11", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1112@seenior.com", "12345678", "admin12", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1113@seenior.com", "12345678", "admin13", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1114@seenior.com", "12345678", "admin14", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1115@seenior.com", "12345678", "admin15", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1116@seenior.com", "12345678", "admin16", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1117@seenior.com", "12345678", "admin17", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1118@seenior.com", "12345678", "admin18", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1119@seenior.com", "12345678", "admin19", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1120@seenior.com", "12345678", "admin20", "010-1234-1234", "19870807");

INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21110@seenior.com", "12345678", "admin10", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21111@seenior.com", "12345678", "admin11", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21112@seenior.com", "12345678", "admin12", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21113@seenior.com", "12345678", "admin13", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21114@seenior.com", "12345678", "admin14", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21115@seenior.com", "12345678", "admin15", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21116@seenior.com", "12345678", "admin16", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21117@seenior.com", "12345678", "admin17", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21118@seenior.com", "12345678", "admin18", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21119@seenior.com", "12345678", "admin19", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin21120@seenior.com", "12345678", "admin20", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin1110@seenior.com", "12345678", "admin10", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11111@seenior.com", "12345678", "admin11", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11112@seenior.com", "12345678", "admin12", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11113@seenior.com", "12345678", "admin13", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11114@seenior.com", "12345678", "admin14", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11115@seenior.com", "12345678", "admin15", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11116@seenior.com", "12345678", "admin16", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11117@seenior.com", "12345678", "admin17", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11118@seenior.com", "12345678", "admin18", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11119@seenior.com", "12345678", "admin19", "010-1234-1234", "19870807");
INSERT INTO ADMIN_ACCOUNT(A_ID, A_PW, A_NAME, A_PHONE, A_BIRTH)
VALUES("admin11120@seenior.com", "12345678", "admin20", "010-1234-1234", "19870807");

SELECT 
	* 
FROM 
	ADMIN_ACCOUNT 
WHERE 
	(A_AUTHORITY_ROLE = "SUB_ADMIN" 
	OR 
	A_AUTHORITY_ROLE = "NOT_APPROVED")
AND 
	A_IS_DELETED = 1
ORDER BY A_AUTHORITY_ROLE DESC, A_NO DESC;



-- ADMIN 인가 테이블(관리자 권한) --------------------------------------------------------------------------------------------------------------
-- 사용 안함
/*
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
*/


-- USER 계정 테이블 -------------------------------------------------------------------------------------------------------------------------
CREATE TABLE USER_ACCOUNT (
	U_NO 						INT AUTO_INCREMENT COMMENT "유저 NO(PK)", 								-- 유저 NO(PK)
	U_ID 						VARCHAR(100) NOT NULL UNIQUE COMMENT "유저 ID(E-MAIL)", 					-- 유저 ID(E-MAIL)
	U_PW 						VARCHAR(200) NOT NULL COMMENT "유저 비밀번호", 								-- 유저 비밀번호
	U_NAME 						VARCHAR(100) NOT NULL COMMENT "유저 이름", 								-- 유저 이름
	U_PHONE 					VARCHAR(100) NOT NULL COMMENT "유저 연락처", 								-- 유저 연락처
	U_NICKNAME 					VARCHAR(255) NOT NULL UNIQUE COMMENT "유저 닉네임(별명)", 					-- 유저 닉네임(별명)
	U_GENDER 					CHAR NOT NULL COMMENT "유저 성별",										-- 유저 성별
	U_BIRTH 					DATE NOT NULL COMMENT "유저 생년월일",										-- 유저 생년월일
	U_ADDRESS 					VARCHAR(255) COMMENT "유저 주소",											-- 유저 주소
    U_PROFILE_IMG 				VARCHAR(255) COMMENT "유저프로필 이미지 파일 명",								-- 유저 프로필 이미지 파일 명
	U_COMPANY 					VARCHAR(50) COMMENT "유저 소속 기관",										-- 유저 소속 기관
	U_IS_PERSONAL 				TINYINT COMMENT "유저 개인회원 OR 기관회원 여부 (개인 = 1, 기관 = 0)",			-- 유저 개인회원 OR 기관회원 여부 (개인 = 1, 기관 = 0)
	U_SOCIAL_ID 				VARCHAR(200) COMMENT "유저 3자 로그인 ID",									-- 유저 3자 로그인 ID
	U_IS_BLOCKED 				TINYINT DEFAULT 1 COMMENT "유저 계정 정지 여부 (기본값 = 1. 정지 시 = 0)",		-- 유저 계정 정지 여부 (기본값 = 1. 정지 시 = 0)
	U_ISACCOUNTNONEXPIRED 		TINYINT DEFAULT 1 COMMENT "유저 계정 만료 유무(만료X = 1, 만료 = 0)",			-- 유저 계정 만료 유무(만료X = 1, 만료 = 0)
	U_ISACCOUNTNONLOCKED 		TINYINT DEFAULT 1 COMMENT "유저 계정 잠김 유무(잠김X = 1, 잠김 = 0)",			-- 유저 계정 잠김 유무(잠김X = 1, 잠김 = 0)
	U_ISCREDENTIALSNONEXPIRED 	TINYINT DEFAULT 1 COMMENT "유저 계정 자격 증명 만료 유무(만료X = 1, 만료 = 0)",	-- 유저 자격 증명 만료 유무(만료X = 1, 만료 = 0)
	U_ISENABLED 				TINYINT DEFAULT 1 COMMENT "유저 계정 사용 가능 유무 (사용 = 1, 사용X = 0)",		-- 유저 계정 사용 가능 유무 (사용 = 1, 사용X = 0)
	U_IS_DELETED 				TINYINT DEFAULT 1 COMMENT "유저 계정 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)",		-- 유저 계정 탈퇴 여부(기본값 = 1, 탈퇴 시 = 0)
	U_REG_DATE 					DATETIME DEFAULT NOW() COMMENT "유저 등록일",								-- 유저 등록일
	U_MOD_DATE 					DATETIME DEFAULT NOW() COMMENT "유저 수정일", 								-- 유저 수정일
    PRIMARY KEY(U_NO)
);

SELECT * FROM USER_ACCOUNT;
SHOW INDEX FROM USER_ACCOUNT;
DROP TABLE USER_ACCOUNT;



-- 게시판 카테고리 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_CATEGORY (
	BC_NO			INT AUTO_INCREMENT COMMENT "게시판 NO(PK)",						-- 게시판 NO(PK)
	BC_NAME			VARCHAR(100) NOT NULL UNIQUE COMMENT "게시판 명",					-- 게시판 명
	BC_IDX			INT COMMENT "게시판 정렬 순서",										-- 게시판 정렬 순서
	BC_IS_DELETED	TINYINT DEFAULT 1 COMMENT "게시판 삭제 여부 (기본값 = 1, 삭제 시 = 0)",	-- 게시판 삭제 여부 (기본값 = 1, 삭제시 = 0)
	BC_REG_DATE		DATETIME DEFAULT NOW() COMMENT "게시판 등록일",						-- 게시판 등록일
	BC_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "게시판 수정일",						-- 게시판 수정일
    PRIMARY KEY(BC_NO)
);

SELECT * FROM BOARD_CATEGORY;
SHOW INDEX FROM BOARD_CATEGORY;
DROP TABLE BOARD_CATEGORY;

SHOW TRIGGERS;

-- board cate 더미 데이터 -----------------------------------------------------------------------------------------
INSERT INTO BOARD_CATEGORY(BC_NAME) VALUES("게시판1");
INSERT INTO BOARD_CATEGORY(BC_NAME,BC_IDX) VALUES("게시판2",1);
INSERT INTO BOARD_CATEGORY(BC_NAME) VALUES("게시판3");
INSERT INTO BOARD_CATEGORY(BC_NAME,BC_IDX) VALUES("게시판4",3);

-- IDX (게시판 정렬 순서) AUTO_INCREMENT 트리거
DELIMITER //

CREATE TRIGGER BEFORE_INSERT_BOARD_CATEGORY
BEFORE INSERT ON BOARD_CATEGORY
FOR EACH ROW
BEGIN
    SET NEW.BC_IDX = (SELECT IFNULL(MAX(BC_IDX), 0) + 1 FROM BOARD_CATEGORY);
END//

DELIMITER ;



-- 일반 게시판 게시물 테이블 -----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_POSTS (
	BP_NO				INT	AUTO_INCREMENT COMMENT "게시물 NO(PK)",										-- 게시물 NO(PK)
	BP_CATEGORY_NO		INT NOT NULL COMMENT "게시물 게시판 NO(BOARD_CATEGORY TABLE PK)",					-- 게시물 게시판 NO(BOARD_CATEGORY TABLE PK)
	BP_TITLE			VARCHAR(200) NOT NULL COMMENT "게시물 제목", 										-- 게시물 제목
	BP_BODY				TEXT NOT NULL COMMENT "게시물 본문", 												-- 게시물 본문
	BP_WRITER_NO		INT NOT NULL COMMENT "게시물 작성자 NO(USER_ACCOUNT TABLE PK)", 					-- 게시물 작성자 NO(USER_ACCOUNT TABLE PK)
	BP_REPORT_STATE		TINYINT DEFAULT 1 COMMENT "게시물 신고 진행 상태(기본값 = 1, 처리중 = 2, 처리 완료 = 0)",	-- 게시물 신고 진행 상태(기본값 = 1, 처리중 = 2, 처리 완료 = 0)
	BP_VIEW_CNT			INT DEFAULT 0 COMMENT "게시물 조회수", 												-- 게시물 조회수 
	BP_IS_DELETED		TINYINT DEFAULT 1 COMMENT "게시물 삭제 여부(기본값 = 1, 삭제 시 = 0)",					-- 게시물 삭제 여부(기본값 = 1, 삭제 시 = 0)
	BP_REG_DATE			DATETIME DEFAULT NOW() COMMENT "게시물 등록일",										-- 게시물 등록일
	BP_MOD_DATE			DATETIME DEFAULT NOW() COMMENT "게시물 수정일",										-- 게시물 수정일
    PRIMARY KEY(BP_NO)
);

SELECT * FROM BOARD_POSTS;
SHOW INDEX FROM BOARD_POSTS;
DROP TABLE BOARD_POSTS;



-- 일반 게시판 댓글 테이블 ------------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_REPLY (
	BR_NO			INT AUTO_INCREMENT COMMENT "댓글 NO(PK)", 							-- 댓글 NO(PK)
	BR_CATEGORY_NO	INT	NOT NULL COMMENT "댓글 게시판 게시판 NO(BOARD_CATEGORY TABLE PK)", 	-- 댓글 게시판 NO(BOARD_CATEGORY TABLE PK)
	BR_POST_NO		INT	NOT NULL COMMENT "댓글 게시물 게시물 NO(BOARD_POSTS TABLE PK)",		-- 댓글 게시물 NO(BOARD_POSTS TABLE PK)
	BR_STATE		TINYINT	NOT NULL COMMENT "댓글, 대댓글 여부(댓글 = 1, 대댓글 = 0)",		-- 댓글, 대댓글 여부(댓글 = 1, 대댓글 = 0)
	BR_TEXT			VARCHAR(255) NOT NULL COMMENT "댓글 내용",							-- 댓글 내용
	BR_WRITER_NO	VARCHAR(255) NOT NULL COMMENT "댓글 작성자 NO(USER_ACCOUNT TABLE PK)",	-- 댓글 작성자 NO(USER_ACCOUNT TABLE PK)
	BR_IS_DELETED	TINYINT DEFAULT 1 COMMENT "댓글 삭제 여부(기본값 = 1, 삭제 시 = 0)",		-- 댓글 삭제 여부(기본값 = 1, 삭제 시 = 0)
	BR_REG_DATE		DATETIME DEFAULT NOW() COMMENT "댓글 등록일",							-- 댓글 등록일
	BR_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "댓글 수정일",							-- 댓글 수정일
    PRIMARY KEY(BR_NO)
);

SELECT * FROM BOARD_REPLY;
SHOW INDEX FROM BOARD_REPLY;
DROP TABLE BOARD_REPLY;



-- 일반 게시판 공지사항 테이블 ----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_NOTICE (
	BN_NO			INT AUTO_INCREMENT COMMENT "공지사항 NO(PK)",							-- 공지사항 NO(PK)
	BN_CATEGORY_NO	INT	NOT NULL COMMENT "공지사항이 등록될 게시판 NO(BOARD_CATEGORY TABLE)",	-- 공지사항이 등록 될 게시판 NO(BOARD_CATEGORY TABLE)
	BN_TITLE		VARCHAR(255) NOT NULL COMMENT "공지사항 제목",							-- 공지사항 제목
	BN_BODY			TEXT NOT NULL COMMENT "공지사항 내용",									-- 공지사항 내용
	BN_WRITER_NO	INT	NOT NULL COMMENT "공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)",		-- 공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)
	BN_VIEW_CNT		INT DEFAULT 0 COMMENT "공지사항 조회수", 			 					-- 공지사항 조회수
	BN_STATE		TINYINT	DEFAULT 1 COMMENT "공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)",	-- 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)
	BN_IS_DELETED	TINYINT DEFAULT 1 COMMENT "공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)
	BN_REG_DATE		DATETIME DEFAULT NOW() COMMENT "공지사항 등록일",						-- 공지사항 등록일
	BN_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "공지사항 수정일",						-- 공지사항 수정일
    PRIMARY KEY(BN_NO)
);

SELECT * FROM BOARD_NOTICE;
SHOW INDEX FROM BOARD_NOTICE;
DROP TABLE BOARD_NOTICE;



-- Q&A 게시판 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_QNA (
	BQ_NO			INT	AUTO_INCREMENT COMMENT "Q&A NO(PK)",							-- Q&A NO(PK)
	BQ_TITLE		VARCHAR(200) NOT NULL COMMENT "Q&A 제목",							-- Q&A 제목
	BQ_BODY			TEXT NOT NULL COMMENT "Q&A 본문",									-- Q&A 본문
	BQ_USER_NO		VARCHAR(255) NOT NULL COMMENT "Q&A 작성자 NO(USER_ACCOUNT TABLE PK)",	-- Q&A 작성자 NO(USER_ACCOUNT TABLE PK)
	BQ_STATE		TINYINT DEFAULT 1 COMMENT "Q&A 답변 여부(기본값 = 1, 답변 완료 = 0)",		-- Q&A 답변 여부(기본값 = 1, 답변 완료 = 0)
	BQ_IS_DELETED	TINYINT DEFAULT 1 COMMENT "Q&A 삭제 여부(기본값 = 1, 삭제 시 = 0)",		-- Q&A 삭제 여부(기본값 = 1, 삭제 시 = 0)
	BQ_REG_DATE		DATETIME DEFAULT NOW() COMMENT "Q&A 등록일",							-- Q&A 등록일
	BQ_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "Q&A 수정일",							-- Q&A 수정일
    PRIMARY KEY(BQ_NO)
);

SELECT * FROM BOARD_QNA;
SHOW INDEX FROM BOARD_QNA;
DROP TABLE BOARD_QNA;


-- Q&A 게시판 답변 테이블 ----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_QNA_ANSWER (
	BQA_NO			INT	AUTO_INCREMENT COMMENT "Q&A 답변 NO(PK)",						-- Q&A 답변 NO(PK)
	BQA_QNA_NO		INT	NOT NULL COMMENT "Q&A 답변 Q&A NO(BOARD_QNA TABLE PK)",			-- Q&A 답변 Q&A NO(BOARD_QNA TABLE PK)
	BQA_ANSWER		TEXT NOT NULL COMMENT "Q&A 답변 내용",								-- Q&A 답변 내용
	BQA_ADMIN_NO	INT	NOT NULL COMMENT "Q&A 답변 작성자 NO(ADMIN_ACCOUNT TABLE PK)",		-- Q&A 답변 작성자 NO(ADMIN_ACCOUNT TABLE PK)
	BQA_IS_DELETED	TINYINT DEFAULT 1 COMMENT "Q&A 답변 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- Q&A 답변 삭제 여부(기본값 = 1, 삭제 시 = 0)
	BQA_REG_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 답변 등록일",						-- Q&A 답변 등록일
	BQA_MOD_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 답변 수정일",						-- Q&A 답변 수정일
    PRIMARY KEY(BQA_NO)
);

SELECT * FROM BOARD_QNA_ANSWER;
SHOW INDEX FROM BOARD_QNA_ANSWER;
DROP TABLE BOARD_QNA_ANSWER;


-- Q&A 게시판 공지사항 테이블 --------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_QNA_NOTICE (
	BQN_NO			INT	AUTO_INCREMENT COMMENT "Q&A 공지사항 NO(PK)", 						-- Q&A 공지사항 NO(PK)
	BQN_TITLE		VARCHAR(255) NOT NULL COMMENT "Q&A 공지사항 제목",							-- Q&A 공지사항 제목
	BQN_BODY		TEXT NOT NULL COMMENT "Q&A 공지사항 내용",									-- Q&A 공지사항 내용
	BQN_WRITER_NO	INT	NOT NULL COMMENT "Q&A 공지사항 작성자(ADMIN_ACCOUNT TABLE PK)",			-- Q&A 공지사항 작성자(ADMIN_ACCOUNT TABLE PK)
	BQN_VIEW_CNT	INT DEFAULT 0 COMMENT "Q&A 공지사항 조회수",			 					-- Q&A 공지사항 조회수
	BQN_STATE		TINYINT DEFAULT 1 COMMENT "Q&A 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)",	-- Q&A 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)
	BQN_IS_DELETED	TINYINT DEFAULT 1 COMMENT "Q&A 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- Q&A 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)
	BQN_REG_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 공지사항 등록일",						-- Q&A 공지사항 등록일
	BQN_MOD_DATE	DATETIME DEFAULT NOW() COMMENT "Q&A 공지사항 수정일",						-- Q&A 공지사항 수정일
    PRIMARY KEY(BQN_NO)
);

SELECT * FROM BOARD_QNA_NOTICE;
SHOW INDEX FROM BOARD_QNA_NOTICE;
DROP TABLE BOARD_QNA_NOTICE;


-- 신고 게시판 테이블 -----------------------------------------------------------------------------------------------------------------
CREATE TABLE BOARD_REPORT (
	BR_NO			INT	AUTO_INCREMENT COMMENT "신고 NO(PK)", 											-- 신고 NO(PK)
	BR_POST_NO		INT	NOT NULL COMMENT "신고된 게시글 NO(BOARD_POSTS TABLE PK)",							-- 신고된 게시글 NO(BOARD_POSTS TABLE PK)
	BR_REASON		VARCHAR(255) NOT NULL COMMENT "신고 사유",											-- 신고 사유
	BR_REPORTER_NO	VARCHAR(255) NOT NULL COMMENT "신고자 NO(USER_ACCOUNT TABLE PK)",						-- 신고자 NO(USER_ACCOUNT TABLE PK)
	BR_RESULT		VARCHAR(255) COMMENT "신고 처리 결과(내용)",												-- 신고 처리 결과(내용)
	BR_STATE		TINYINT DEFAULT 1 COMMENT "신고 진행 상태(기본값 = 1, 처리 중 = 2, 반려 = 3, 처리 완료 = 0)",	-- 신고 진행 상태(기본값 = 1, 처리 중 = 2, 반려 = 3, 처리 완료 = 0)
	BR_IS_DELETED	TINYINT	DEFAULT 1 COMMENT "신고 취소 여부(기본값 = 1, 취소 시 = 0)", 						-- 신고 취소 여부(기본값 = 1, 취소 시 = 0)
	BR_REG_DATE		DATETIME DEFAULT NOW() COMMENT "신고 등록일",											-- 신고 등록일
	BR_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "신고 수정일",											-- 신고 수정일
    PRIMARY KEY(BR_NO)
);

SELECT * FROM BOARD_REPORT;
SHOW INDEX FROM BOARD_REPORT;
DROP TABLE BOARD_REPORT;


-- 전체 공지사항 테이블 -----------------------------------------------------------------------------------------------------------------
CREATE TABLE NOTICE (
	N_NO			INT	AUTO_INCREMENT COMMENT "전체 공지사항 NO(PK)", 							-- 전체 공지사항 NO(PK)
	N_TITLE			VARCHAR(255) NOT NULL COMMENT "전체 공지사항 제목",							-- 전체 공지사항 제목
	N_BODY			TEXT NOT NULL COMMENT "전체 공지사항 내용",									-- 전체 공지사항 내용
	N_WRITER_NO		INT	NOT NULL COMMENT "전체 공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)",		-- 전체 공지사항 작성자 NO(ADMIN_ACCOUNT TABLE PK)
	N_VIEW_CNT		INT DEFAULT 0 COMMENT "전체 공지사항 조회수",			 					-- 전체 공지사항 조회수
	N_STATE			TINYINT DEFAULT 1 COMMENT "전체 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)",	-- 전체 공지사항 숨김 상태(기본값 = 1, 숨김 시 = 0)
	N_IS_DELETED	TINYINT	DEFAULT 1 COMMENT "전체 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 전체 공지사항 삭제 여부(기본값 = 1, 삭제 시 = 0)
	N_REG_DATE		DATETIME DEFAULT NOW() COMMENT "전체 공지사항 등록일",						-- 전체 공지사항 등록일
	N_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "전체 공지사항 수정일",						-- 전체 공지사항 수정일
    PRIMARY KEY(N_NO)
);

SELECT * FROM NOTICE;
SHOW INDEX FROM NOTICE;
DROP TABLE NOTICE;


-- 질환 카테고리 테이블 --------------------------------------------------------------------------------------------------------------
CREATE TABLE DISEASE_CATEGORY (
	DC_NO			INT	AUTO_INCREMENT COMMENT "질환 분류 NO(PK)", 						-- 질환 분류 NO(PK)
	DC_NAME			VARCHAR(100) NOT NULL COMMENT "질환 분류 명",							-- 질환 분류 명
	DC_IS_DELETED	TINYINT DEFAULT 1 COMMENT "질환 분류 삭제 여부(기본값 = 0, 삭제 시 = 1)",	-- 질환 분류 삭제 여부(기본값 = 0, 삭제 시 = 1)
	DC_REG_DATE		DATETIME DEFAULT NOW() COMMENT "질환 분류 등록일",						-- 질환 분류 등록일
	DC_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "질환 분류 수정일",						-- 질환 분류 수정일
    PRIMARY KEY(DC_NO)
);

SELECT * FROM DISEASE_CATEGORY;
SHOW INDEX FROM DISEASE_CATEGORY;
DROP TABLE DISEASE_CATEGORY;

INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("간 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("갑상선 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("심혈관계 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("내과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("정형외과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("신경과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("안과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("내분비내과 질환");
INSERT INTO DISEASE_CATEGORY(DC_NAME) VALUES("정신과 질환");

-- 질환 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE DISEASE (
	D_NO			INT	AUTO_INCREMENT COMMENT "질환 NO(PK)", 						-- 질환 NO(PK)
	D_CATEGORY_NO	INT	NOT NULL COMMENT "질환 분류 NO(DISEASE_CATEGORY TABLE PK)",	-- 질환 분류 NO(DISEASE_CATEGORY TABLE PK)
	D_NAME			VARCHAR(100) NOT NULL COMMENT "질환 명",							-- 질환 명
	D_INFO			TEXT NOT NULL COMMENT "질환 정보",								-- 질환 정보
	D_GOOD_FOOD		VARCHAR(100) NOT NULL COMMENT "질환에 좋은 추천 음식 재료",			-- 질환에 좋은 추천 음식 재료
	D_BAD_FOOD		VARCHAR(100) NOT NULL COMMENT "질환에 나쁜 비추천 음식 재료",			-- 질환에 나쁜 비추천 음식 재료
	D_IS_DELETED	TINYINT DEFAULT 1 COMMENT "질환 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 질환 삭제 여부(기본값 = 1, 삭제 시 = 0)
	D_REG_DATE		DATETIME DEFAULT NOW() COMMENT "질환 등록일",						-- 질환 등록일
	D_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "질환 수정일",						-- 질환 수정일
    PRIMARY KEY(D_NO)
);

SELECT * FROM DISEASE;
SHOW INDEX FROM DISEASE;
DROP TABLE DISEASE;

-- 추천 영상 테이블 --------------------------------------------------------------------------------------------------------------
CREATE TABLE VIDEO (
	V_NO			INT	AUTO_INCREMENT COMMENT "추천 영상 NO(PK)", 						-- 추천 영상 NO(PK)
	V_DISEASE_NO	INT	NOT NULL COMMENT "추천 영상 질환 NO(DISEASE TABLE PK)",				-- 추천 영상 질환 NO(DISEASE TABLE PK)
	V_TITLE			VARCHAR(100) NOT NULL COMMENT "추천 영상 제목",							-- 추천 영상 제목
	V_TEXT			VARCHAR(255) NOT NULL COMMENT "추천 영상 설명",							-- 추천 영상 설명
	V_LINK			VARCHAR(255) NOT NULL COMMENT "추천 영상 URL",						-- 추천 영상 URL
	V_IS_DELETED	TINYINT DEFAULT 1 COMMENT "추천 영상 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 추천 영상 삭제 여부(기본값 = 1, 삭제 시 = 0)
	V_REG_DATE		DATETIME DEFAULT NOW() COMMENT "추천 영상 등록일",						-- 추천 영상 등록일
	V_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "추천 영상 수정일",						-- 추천 영상 수정일
    PRIMARY KEY(V_NO)
);

SELECT * FROM VIDEO;
SHOW INDEX FROM VIDEO;
DROP TABLE VIDEO;


-- 광고 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE ADVERTISEMENT (
	AD_NO			INT	AUTO_INCREMENT COMMENT "광고 NO(PK)", 						-- 광고 NO(PK)
	AD_POSITION		INT	NOT NULL COMMENT "광고 위치", 								-- 광고 위치
	AD_IMG			VARCHAR(255) NOT NULL COMMENT "광고 사진",						-- 광고 사진
	AD_URL			VARCHAR(255) NOT NULL COMMENT "광고 URL",						-- 광고 URL
	AD_START_DATE	DATE NOT NULL COMMENT "광고 시작일",								-- 광고 시작일
	AD_END_DATE		DATE NOT NULL COMMENT "광고 종료일",								-- 광고 종료일
	AD_CLIENT		VARCHAR(100) NOT NULL COMMENT "광고주",							-- 광고주 
	AD_IS_DELETED	TINYINT DEFAULT 1 COMMENT "광고 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 광고 삭제 여부(기본값 = 1, 삭제 시 = 0)
	AD_REG_DATE		DATETIME DEFAULT NOW() COMMENT "광고 등록일",						-- 광고 등록일
	AD_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "광고 수정일",						-- 광고 수정일
    PRIMARY KEY(AD_NO)
);

SELECT * FROM ADVERTISEMENT;
SHOW INDEX FROM ADVERTISEMENT;
DROP TABLE ADVERTISEMENT;


-- 환자 테이블 -------------------------------------------------------------------------------------------------------------------
CREATE TABLE CARE_LIST (
	CL_NO					INT	AUTO_INCREMENT COMMENT "환자 NO(PK)", 							-- 환자 NO(PK)
	CL_USER_NO				INT	NOT NULL COMMENT "환자 관리하는 USER NO(USER_ACCOUNT TABLE PK)",	-- 환자 관리하는 USER NO(USER_ACCOUNT TABLE PK)
	CL_GROUP				VARCHAR(50) COMMENT "환자 그룹",										-- 환자 그룹
	CL_NAME					VARCHAR(255) NOT NULL COMMENT "환자 이름",							-- 환자 이름
	CL_GENDER				CHAR(1) NOT NULL COMMENT "환자 성별",									-- 환자 성별 
	CL_AGE					INT	NOT NULL COMMENT "환자 나이",										-- 환자 나이
	CL_ADDRESS				VARCHAR(255) NOT NULL COMMENT "환자 실 거주지",							-- 환자 실 거주지
	CL_BLOOD_TYPE			CHAR(3) NOT NULL COMMENT "환자 혈액형",								-- 환자 혈액형
	CL_PHONE				VARCHAR(100) NOT NULL COMMENT "환자 연락처",							-- 환자 연락처
	CL_EMERGENCY_CONTACT_1	VARCHAR(100) NOT NULL COMMENT "환자 비상 연락처 1. 최소 한개 필수",			-- 환자 비상 연락처 1. 최소 한개 필수
	CL_EMERGENCY_CONTACT_2	VARCHAR(100) COMMENT "환자 비상 연락처 2",								-- 환자 비상 연락처 2
	CL_EMERGENCY_CONTACT_3	VARCHAR(100) COMMENT "환자 비상 연락처 3",								-- 환자 비상 연락처 3
	CL_EMERGENCY_CONTACT_4	VARCHAR(100) COMMENT "환자 비상 연락처 4",								-- 환자 비상 연락처 4
	CL_FAVOR_FOOD			VARCHAR(100) COMMENT "환자 선호 음식",									-- 환자 선호 음식
	CL_HATE_FOOD			VARCHAR(100) COMMENT "환자 비선호 음식",								-- 환자 비선호 음식
	CL_DIABETIC_FOOD		TINYINT NOT NULL COMMENT "환자 당뇨식 유무",							-- 환자 당뇨식 유무
	CL_MEDICATIONS			VARCHAR(100) COMMENT "환자 복용 중인 약",								-- 환자 복용중인 약
	CL_HOSPITAL				VARCHAR(100) COMMENT "환자 담당 병원",									-- 환자 담당 병원
	CL_DOCTOR				VARCHAR(100) COMMENT "환자 담당의",									-- 환자 담당의
	CL_HOSPITAL_TEL			VARCHAR(100) COMMENT "환자 담당 병원 연락처",							-- 환자 담당 병원 연락처
	CL_WALK_STATE			TINYINT NOT NULL COMMENT "환자 보행 상태",								-- 환자 보행 상태
	CL_ASSISTIVE_DEVICE		VARCHAR(100) COMMENT "환자 보조 기구 유무", 								-- 환자 보조 기구 유무
	CL_WASHING_ASSISTANCE	TINYINT NOT NULL COMMENT "환자 세면 도움 정도",							-- 환자 세면 도움 정도
	CL_TOILET_ASSISTANCE	TINYINT NOT NULL COMMENT "환자 대소변 도움 정도",						-- 환자 대소변 도움 정도
	CL_MENTAL_STATE			TINYINT NOT NULL COMMENT "환자 정신 건강 상태(우울증, 고립감 정도)",			-- 환자 정신 건강 상태(우울증, 고립감 정도)
	CL_SOCIAL_STATE			TINYINT NOT NULL COMMENT "환자 사회적 교류 정도",						-- 환자 사회적 교류 정도
	CL_ETC					VARCHAR(255) COMMENT "환자 기타 특이사항",								-- 환자 기타 특이사항
	CL_FAVORITES			TINYINT DEFAULT 1 COMMENT "환자 즐겨찾기 여부(기본값 = 1, 즐겨찾기 시 = 0)",	-- 환자 즐겨찾기 여부 (기본값 = 1 즐겨찾기 시 = 0)
	CL_IS_DELETED			TINYINT	DEFAULT 1 COMMENT "환자 삭제 여부(기본값 = 1, 즐겨찾기 시 = 0)",	-- 환자 삭제 여부(기본값 = 1, 즐겨찾기 시 = 0)
	CL_REG_DATE				DATETIME DEFAULT NOW() COMMENT "환자 등록일",							-- 환자 등록일
	CL_MOD_DATE				DATETIME DEFAULT NOW() COMMENT "환자 수정일",							-- 환자 수정일
    PRIMARY KEY(CL_NO)
);

SELECT * FROM CARE_LIST;
SHOW INDEX FROM CARE_LIST;
DROP TABLE CARE_LIST;


-- 환자 보유 질환 테이블 -------------------------------------------------------------------------------------------------------------
CREATE TABLE CARE_PERSON_DISEASE (
	CPD_NO				INT	AUTO_INCREMENT COMMENT "환자 보유 질환 NO(PK)", 						-- 환자 보유 질환 NO(PK)
	CPD_CARE_LIST_NO	INT	NOT NULL COMMENT "환자 NO(CARE_LIST TABLE PK)",						-- 환자 보유 질환 환자 NO(CARE_LIST TABLE PK)
	CPD_DISEASE_NO		INT	NOT NULL COMMENT "환자 보유 질환 질환 NO(DISEASE TABLE PK)", 			-- 환자 보유 질환 질환 NO(DISEASE TABLE PK)
	CPD_IS_DELETED		TINYINT	DEFAULT 1 COMMENT "환자 보유 질환 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 환자 보유 질환 삭제 여부(기본값 = 1, 삭제 시 = 0)
	CPD_REG_DATE		DATETIME DEFAULT NOW() COMMENT "환자 보유 질환 등록일",						-- 환자 보유 질환 등록일
	CPD_MOD_DATE		DATETIME DEFAULT NOW() COMMENT "환자 보유 질환 수정일",						-- 환자 보유 질환 수정일
    PRIMARY KEY(CPD_NO)
);

SELECT * FROM CARE_PERSON_DISEASE;
SHOW INDEX FROM CARE_PERSON_DISEASE;
DROP TABLE CARE_PERSON_DISEASE;


-- 일정 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE SCHEDULER (
	S_NO				INT	AUTO_INCREMENT COMMENT "일정 NO(PK)", 						-- 일정 NO(PK)
	S_USER_NO			INT	NOT NULL COMMENT "일정 유저 NO(USER_ACCOUNT TABLE PK)",		-- 일정 유저 NO(USER_ACCOUNT TABLE PK)
	S_CARE_LIST_NO		INT	NOT NULL COMMENT "일정 환자 NO(CARE_LIST TABLE PK)",			-- 일정 환자 NO(CARE_LIST TABLE PK)
	S_TITLE				VARCHAR(100) NOT NULL COMMENT "일정 제목",						-- 일정 제목
	S_COMMENT			VARCHAR(255) NOT NULL COMMENT "일정 상세",						-- 일정 상세
	S_ALARM_CHECK		TINYINT	DEFAULT 1 COMMENT "일정 알람 유무(기본값 = 1, 알람 시 = 0)",	-- 일정 알람 유무(기본값 = 1, 알람 시 = 0)
	S_ALARM_TIME		DATETIME COMMENT "일정 알람 시간",									-- 일정 알람 시간
	S_YEAR				INT NOT NULL COMMENT "일정 년도",									-- 일정 년도
	S_MONTH				INT	NOT NULL COMMENT "일정 월",									-- 일정 월
	S_DATE				INT	NOT NULL COMMENT "일정 일",									-- 일정 일
	S_START_DATE		DATE NOT NULL COMMENT "일정 시작일",								-- 일정 시작일
	S_END_DATE			DATE NOT NULL COMMENT "일정 종료일", 								-- 일정 종료일
	S_IS_DELETED		TINYINT DEFAULT 1 COMMENT "일정 삭제 여부(기본값 = 1, 삭제 시 = 0)",	-- 일정 삭제 여부(기본값 = 1, 삭제 시 = 0)
	S_REG_DATE			DATETIME DEFAULT NOW() COMMENT "일정 등록일",						-- 일정 등록일
	S_MOD_DATE			DATETIME DEFAULT NOW() COMMENT "일정 수정일",						-- 일정 수정일
    PRIMARY KEY(S_NO)
);

SELECT * FROM SCHEDULER;
SHOW INDEX FROM SCHEDULER;
DROP TABLE SCHEDULER;


-- 복용약 테이블 --------------------------------------------------------------------------------------------------------------------
CREATE TABLE PRESCRIPTIONS (
	P_NO					INT	AUTO_INCREMENT COMMENT "복용 약 NO(PK)", 							-- 복용 약 NO(PK)
	P_USER_NO				INT	NOT NULL COMMENT "복용 약 유저 NO(USER_ACCOUNT TABLE PK)",			-- 복용 약 유저 NO(USER_ACCOUNT TABLE PK)
	P_CARE_LIST_NO			INT	NOT NULL COMMENT "복용 약 환자 NO(CARE_LIST TABLE PK)",			-- 복용 약 환자 NO(CARE_LIST TABLE PK)
	P_TITLE					VARCHAR(100) NOT NULL COMMENT "복용 약 알림 제목",						-- 복용 약 알림 제목
	P_COMMENT				VARCHAR(255) NOT NULL COMMENT "복용 약 알림 상세",						-- 복용 약 알림 상세
	P_ALARM_CHECK			TINYINT	DEFAULT 1 COMMENT "복용 약 알람 유무(기본값 = 1, 알람 시 = 0)",		-- 복용 약 알람 유무(기본값 = 1, 알람 시 = 0)
	P_ALARM_TIME_1			VARCHAR(100) COMMENT "복용 약 알람 시간 1",								-- 복용 약 알람 시간 1
	P_TAKING_IT_OR_NOT_1	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 1(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 1(기본값 = 1, 복용 시 = 0)
	P_ALARM_TIME_2			VARCHAR(100) COMMENT "복용 약 알람 시간 2",								-- 복용 약 알람 시간 2
	P_TAKING_IT_OR_NOT_2	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 2(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 2(기본값 = 1, 복용 시 = 0)
	P_ALARM_TIME_3			VARCHAR(100) COMMENT "복용 약 알람 시간 3",								-- 복용 약 알람 시간 3
	P_TAKING_IT_OR_NOT_3	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 3(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 3(기본값 = 1, 복용 시 = 0)
	P_ALARM_TIME_4			VARCHAR(100) COMMENT "복용 약 알람 시간 4",								-- 복용 약 알람 시간 4
	P_TAKING_IT_OR_NOT_4	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 4(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 4(기본값 = 1, 복용 시 = 0)
	P_ALARM_TIME_5			VARCHAR(100) COMMENT "복용 약 알람 시간 5",								-- 복용 약 알람 시간 5
	P_TAKING_IT_OR_NOT_5	TINYINT DEFAULT 1 COMMENT "복용 유무 체크 5(기본값 = 1, 복용 시 = 0)",		-- 복용 유무 체크 5(기본값 = 1, 복용 시 = 0)	
    P_BEFORE_AFTER			TINYINT COMMENT "복용 약 식전 OR 식후(식전 = 1, 식후 = 0)",				-- 복용 약 식전 OR 식후(식전 = 1, 식후 = 0)
	P_START_DATE			DATE NOT NULL COMMENT "복용 시작일",									-- 복용 시작일
	P_END_DATE				DATE NOT NULL COMMENT "복용 종료일",									-- 복용 종료일
	P_IS_DELETED			TINYINT DEFAULT 1 COMMENT "복용 약 삭제 여부(기본값 = 1, 삭제 시 = 0)",		-- 복용 약 삭제 여부(기본값 = 1, 삭제 시 = 0)
	P_REG_DATE				DATETIME DEFAULT NOW() COMMENT "복용 약 등록일",						-- 복용 약 등록일
	P_MOD_DATE				DATETIME DEFAULT NOW() COMMENT "복용 약 수정일",						-- 복용 약 수정일
    PRIMARY KEY(P_NO)
);

SELECT * FROM PRESCRIPTIONS;
SHOW INDEX FROM PRESCRIPTIONS;
DROP TABLE PRESCRIPTIONS;

