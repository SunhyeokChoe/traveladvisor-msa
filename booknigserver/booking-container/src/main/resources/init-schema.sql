----------------------------------------
-- 호텔/항공권/차량 예약 스키마 및 테이블 정의
----------------------------------------

DROP SCHEMA IF EXISTS booking CASCADE;
CREATE SCHEMA booking;

----------------------------------------
-- Booking Status ENUM
----------------------------------------
DROP TYPE IF EXISTS booking.booking_status;
CREATE TYPE booking.booking_status AS ENUM (
    'PENDING',
    'HOTEL_BOOKED',
    'FLIGHT_BOOKED',
    'CAR_BOOKED',
    'HOTEL_CANCELLED',
    'FLIGHT_CANCELLED',
    'CAR_CANCELLED',
    'APPROVED',
    'CANCELLING',
    'CANCELLED');

----------------------------------------
-- Saga Action Status ENUM
-- Saga 상태는 Orchestrator가 다루며,
-- 전체 예약 흐름을 기록하고 관리합니다.
----------------------------------------
DROP TYPE IF EXISTS booking.saga_action_status;
CREATE TYPE booking.saga_action_status AS ENUM (
    'STARTED',
    'PROCESSING',
    'COMPENSATING',
    'SUCCEEDED',
    'COMPENSATED',
    'FAILED');

----------------------------------------
-- Outbox Status ENUM
----------------------------------------
DROP TYPE IF EXISTS booking.outbox_status;
CREATE TYPE booking.outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

DROP TABLE IF EXISTS booking.bookings CASCADE;
CREATE TABLE booking.bookings
(
    id               UUID PRIMARY KEY,
    trace_id         UUID                                   NOT NULL,
    member_email     VARCHAR(30)                            NOT NULL,
    hotel_offer_id   BIGINT                                 NOT NULL,
    flight_offer_id  BIGINT                                 NOT NULL,
    car_offer_id     BIGINT                                 NOT NULL,
    total_price      NUMERIC(10, 2)                         NOT NULL,
    booking_status   booking.booking_status                 NOT NULL,
    failure_messages TEXT,
    created_by       VARCHAR(255)                           NOT NULL,
    modified_by      VARCHAR(255),
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE
);

----------------------------------------
-- booking_items 예약 서비스 아이템
----------------------------------------
-- 필요하다면 서비스 타입 ENUM 추가 (호텔/항공/차량 등)
-- 호텔, 항공권, 차량 외에 확장 고려 시 ENUM 대신 VARCHAR사용 가능
DROP TYPE IF EXISTS booking.service_type;
CREATE TYPE booking.service_type AS ENUM (
    'HOTEL',
    'FLIGHT',
    'CAR'
);
-- 기존 booking_items를 다양한 예약 서비스(호텔/항공/차량)에 대응할 수 있도록 변경
-- product_id -> service_item_id로 명칭 변경
-- service_type 컬럼 추가
-- quantity, price, sub_total는 필요에 따라 유지 (여러 좌석, 여러 방, 여러 대의 차량 등)
DROP TABLE IF EXISTS booking.booking_items CASCADE;
CREATE TABLE booking.booking_items
(
    id              BIGINT               NOT NULL,
    booking_id      UUID                 NOT NULL,
    service_item_id UUID                 NOT NULL,
    service_type    booking.service_type NOT NULL,
    price           NUMERIC(10, 2)       NOT NULL,
    quantity        INTEGER              NOT NULL,
    sub_total       NUMERIC(10, 2)       NOT NULL,
    CONSTRAINT booking_items_pkey PRIMARY KEY (id, booking_id)
);
ALTER TABLE booking.booking_items
    ADD CONSTRAINT booking_items_booking_id_fkey FOREIGN KEY (booking_id)
        REFERENCES booking.bookings (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
    NOT VALID;

----------------------------------------
-- Hotel Outbox Table
----------------------------------------
CREATE TABLE booking.hotel_outbox
(
    id                 UUID PRIMARY KEY,
    saga_action_id     UUID                       NOT NULL,
    event_type         VARCHAR(20)                NOT NULL, -- 이벤트 타입(HOTEL_BOOKED, HOTEL_CANCELLED)
    event_payload      JSONB                      NOT NULL, -- 이벤트 데이터(JSON)
    booking_status     booking.booking_status     NOT NULL,
    outbox_status      booking.outbox_status      NOT NULL DEFAULT 'STARTED',
    saga_action_status booking.saga_action_status NOT NULL DEFAULT 'STARTED',
    created_at         TIMESTAMP WITH TIME ZONE            DEFAULT now() NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE,
    completed_at       TIMESTAMP WITH TIME ZONE,
    version            INTEGER                    NOT NULL,
    CONSTRAINT hotel_outbox_saga_action_status_ux UNIQUE (event_type, saga_action_id, saga_action_status)
);
CREATE INDEX hotel_outbox_idx ON booking.hotel_outbox (event_type, outbox_status, saga_action_status);

----------------------------------------
-- Flight Outbox Table
----------------------------------------
CREATE TABLE booking.flight_outbox
(
    id                 UUID PRIMARY KEY,
    saga_action_id     UUID                       NOT NULL,
    event_type         VARCHAR(20)                NOT NULL, -- 이벤트 타입(FLIGHT_BOOKED, FLIGHT_CANCELLED)
    event_payload      JSONB                      NOT NULL, -- 이벤트 데이터(JSON)
    booking_status     booking.booking_status     NOT NULL,
    outbox_status      booking.outbox_status      NOT NULL DEFAULT 'STARTED',
    saga_action_status booking.saga_action_status NOT NULL DEFAULT 'STARTED',
    created_at         TIMESTAMP WITH TIME ZONE            DEFAULT now() NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE,
    completed_at       TIMESTAMP WITH TIME ZONE,
    version            INTEGER                    NOT NULL,
    CONSTRAINT flight_outbox_saga_action_status_ux UNIQUE (event_type, saga_action_id, saga_action_status)
);
CREATE INDEX flight_outbox_idx ON booking.flight_outbox (event_type, outbox_status, saga_action_status);

----------------------------------------
-- Car Outbox Table
----------------------------------------
CREATE TABLE booking.car_outbox
(
    id                 UUID PRIMARY KEY,
    saga_action_id     UUID                       NOT NULL,
    event_type         VARCHAR(20)                NOT NULL, -- 이벤트 타입(CAR_BOOKED, CAR_CANCELLED)
    event_payload      JSONB                      NOT NULL, -- 이벤트 데이터(JSON)
    booking_status     booking.booking_status     NOT NULL,
    outbox_status      booking.outbox_status      NOT NULL DEFAULT 'STARTED',
    saga_action_status booking.saga_action_status NOT NULL DEFAULT 'STARTED',
    created_at         TIMESTAMP WITH TIME ZONE            DEFAULT now() NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE,
    completed_at       TIMESTAMP WITH TIME ZONE,
    version            INTEGER                    NOT NULL,
    CONSTRAINT car_outbox_saga_action_status_ux UNIQUE (event_type, saga_action_id, saga_action_status)
);
CREATE INDEX car_outbox_idx ON booking.car_outbox (event_type, outbox_status, saga_action_status);
