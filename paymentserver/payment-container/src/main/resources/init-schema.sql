----------------------------------------
-- 결제 스키마 및 테이블 정의
----------------------------------------

DROP SCHEMA IF EXISTS payment CASCADE;
CREATE SCHEMA payment;

DROP TYPE IF EXISTS payment_status;
-- COMPLETED: 결제 완료
-- CANCELLED: 결제 취소
-- FAILED: 결제 실패
CREATE TYPE payment.payment_status AS ENUM ('PENDING', 'COMPLETED', 'CANCELLED', 'FAILED');
DROP TABLE IF EXISTS payment.payments CASCADE;
CREATE TABLE payment.payments
(
    id             uuid                   NOT NULL,
    member_id      uuid                   NOT NULL,
    reservation_id uuid                   NOT NULL,
    price          NUMERIC(10, 2)         NOT NULL,
    status         payment.payment_status NOT NULL DEFAULT 'PENDING',
    created_by     VARCHAR(255)           NOT NULL,
    modified_by    VARCHAR(255),
    created_at     TIMESTAMP WITH TIME ZONE        DEFAULT now() NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE,
    CONSTRAINT payments_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS payment.point_entries CASCADE;
CREATE TABLE payment.point_entries
(
    id                 uuid                                   NOT NULL,
    member_id          uuid                                   NOT NULL,
    total_point_amount numeric(10, 2)                         NOT NULL,
    created_by         VARCHAR(255)                           NOT NULL,
    modified_by        VARCHAR(255),
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE,
    CONSTRAINT point_entries_pkey PRIMARY KEY (id)
);

DROP TYPE IF EXISTS point_tran_type;
-- EARN: 포인트 적립
-- REDEEM: 포인트 차감
CREATE TYPE payment.point_tran_type AS ENUM ('EARN', 'REDEEM');

DROP TABLE IF EXISTS payment.point_histories CASCADE;
CREATE TABLE payment.point_histories
(
    id          uuid                                   NOT NULL,
    member_id   uuid                                   NOT NULL,
    amount      numeric(10, 2)                         NOT NULL,
    type        payment.point_tran_type                NOT NULL,
    created_by  VARCHAR(255)                           NOT NULL,
    modified_by VARCHAR(255),
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    CONSTRAINT point_histories_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS event.events CASCADE;
DROP TYPE IF EXISTS event.event_status;
DROP TYPE IF EXISTS event.reward_type;
DROP TYPE IF EXISTS event.action_type;
CREATE SCHEMA IF NOT EXISTS event;
-- WILL_OPEN: 오픈 예정
-- ACTIVE: 이벤트 진행 중
-- CLOSE: 이벤트 종료
CREATE TYPE event.event_status AS ENUM ('WILL_OPEN', 'ACTIVE', 'CLOSE');
-- 리워드 타입
CREATE TYPE event.reward_type AS ENUM ('POINT', 'GIFT', 'VOUCHER', 'DISCOUNT');
-- 액션 타입
-- SIGNUP: 회원가입 시 보상 지급
-- REFERRAL: 추천 시 보상 지급
-- REVIEW: 리뷰 작성
CREATE TYPE event.action_type AS ENUM ('SIGNUP', 'REFERRAL', 'REVIEW');
CREATE TABLE event.events
(
    id                BIGSERIAL PRIMARY KEY,
    event_name        VARCHAR(255)             NOT NULL,
    event_description TEXT                     NOT NULL,
    action_type       event.action_type        NOT NULL,
    reward_type       event.reward_type        NOT NULL,
    reward_value      VARCHAR(255)             NOT NULL,
    start_date        TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date          TIMESTAMP WITH TIME ZONE,
    status            event.event_status       NOT NULL DEFAULT 'WILL_OPEN',
    created_by        VARCHAR(255)             NOT NULL,
    modified_by       VARCHAR(255),
    created_at        TIMESTAMP WITH TIME ZONE          DEFAULT now() NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE,
);
