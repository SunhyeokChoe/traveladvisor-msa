----------------------------------------
-- 회원 스키마 및 테이블 정의
----------------------------------------

DROP SCHEMA IF EXISTS member CASCADE;
CREATE SCHEMA member;

DROP TYPE IF EXISTS member.gender;
CREATE TYPE member.gender AS ENUM ('MALE', 'FEMALE', 'OTHER', 'UNDISCLOSED');
DROP TABLE IF EXISTS member.members CASCADE;
CREATE TABLE member.members
(
    id             UUID PRIMARY KEY,
    email          VARCHAR(30)                            NOT NULL,
    nickname       VARCHAR(30)                            NOT NULL,
    first_name     VARCHAR(30)                            NOT NULL,
    last_name      VARCHAR(30)                            NOT NULL,
    contact_number VARCHAR(20)                            NOT NULL,
    gender         member.gender                          NOT NULL,
    created_by     VARCHAR(255)                           NOT NULL,
    modified_by    VARCHAR(255),
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE
);
