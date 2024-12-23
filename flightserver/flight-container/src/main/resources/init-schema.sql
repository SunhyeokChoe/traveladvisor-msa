----------------------------------------
-- 항공권 스키마 및 테이블 정의
----------------------------------------

DROP SCHEMA IF EXISTS flight CASCADE;
CREATE SCHEMA flight;

----------------------------------------
-- Booking Status ENUM
----------------------------------------
DROP TYPE IF EXISTS flight.booking_status;
CREATE TYPE flight.booking_status AS ENUM (
    'COMPLETED',
    'CANCELLED',
    'FAILED');

----------------------------------------
-- Outbox Status ENUM
----------------------------------------
DROP TYPE IF EXISTS flight.outbox_status;
CREATE TYPE flight.outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

----------------------------------------
-- Flight Offers Table
----------------------------------------
DROP TABLE IF EXISTS flight.flight_offers CASCADE;
CREATE TABLE flight.flight_offers
(
    id                     BIGSERIAL PRIMARY KEY,
    carrier_code           VARCHAR(10)                            NOT NULL,
    operating_carrier_code VARCHAR(10),
    flight_number          VARCHAR(50)                            NOT NULL,
    aircraft_code          VARCHAR(10),
    departure_iata         VARCHAR(10)                            NOT NULL,
    departure_at           TIMESTAMP WITH TIME ZONE               NOT NULL,
    arrival_iata           VARCHAR(10)                            NOT NULL,
    arrival_at             TIMESTAMP WITH TIME ZONE               NOT NULL,
    duration INTERVAL NOT NULL,
    blacklisted_in_eu      BOOLEAN,
    number_of_stops        INTEGER,
    price                  NUMERIC(10, 2)                         NOT NULL,
    created_at             TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE
);
COMMENT
ON TABLE hotel.countries IS '항공권 테이블';
COMMENT
ON COLUMN flight.flight_offers.carrier_code IS '항공사 코드 (예: 6X)';
COMMENT
ON COLUMN flight.flight_offers.operating_carrier_code IS '운영 항공사의 코드. 실제 항공편을 운영하는 항공사를 나타냅니다.';
COMMENT
ON COLUMN flight.flight_offers.flight_number IS '항공편 번호 (예: 3618)';
COMMENT
ON COLUMN flight.flight_offers.aircraft_code IS '항공기 코드 (예: 733, 332). 운항 항공기의 유형을 나타냅니다.';
COMMENT
ON COLUMN flight.flight_offers.departure_iata IS '출발 공항의 IATA 코드 (예: ICN)';
COMMENT
ON COLUMN flight.flight_offers.departure_at IS '출발 시간 (타임존 포함)';
COMMENT
ON COLUMN flight.flight_offers.arrival_iata IS '도착 공항의 IATA 코드 (예: JFK)';
COMMENT
ON COLUMN flight.flight_offers.arrival_at IS '도착 시간 (타임존 포함)';
COMMENT
ON COLUMN flight.flight_offers.duration IS '비행 소요 시간 (예: 2 hours 55 minutes)';
COMMENT
ON COLUMN flight.flight_offers.blacklisted_in_eu IS 'EU 블랙리스트에 포함된 항공사인지 여부를 나타냅니다. (true/false)';
COMMENT
ON COLUMN flight.flight_offers.number_of_stops IS '중간 경유 횟수 (0은 직항을 의미)';
COMMENT
ON COLUMN flight.flight_offers.price IS '티켓 가격';
COMMENT
ON COLUMN flight.flight_offers.created_at IS '레코드 생성 시각 (기본값: 현재 시각)';

----------------------------------------
-- Flight Booking Approval Status Table
----------------------------------------
DROP TABLE IF EXISTS flight.booking_approval CASCADE;
CREATE TABLE flight.booking_approval
(
    id               UUID                  NOT NULL,
    flight_offers_id BIGINT                NOT NULL,
    booking_id       UUID                  NOT NULL,
    status           flight.booking_status NOT NULL,
    CONSTRAINT booking_approval_pkey PRIMARY KEY (id)
);

----------------------------------------
-- Flight Booking Outbox Table
----------------------------------------
DROP TABLE IF EXISTS flight.booking_outbox CASCADE;
CREATE TABLE flight.booking_outbox
(
    id             UUID PRIMARY KEY,
    saga_action_id UUID                  NOT NULL,
    event_type     VARCHAR(20)           NOT NULL,
    event_payload  JSONB                 NOT NULL,
    booking_status flight.booking_status NOT NULL,
    outbox_status  flight.outbox_status  NOT NULL DEFAULT 'STARTED',
    created_at     TIMESTAMP WITH TIME ZONE       DEFAULT now() NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE,
    completed_at   TIMESTAMP WITH TIME ZONE,
    version        INTEGER               NOT NULL,
    CONSTRAINT booking_outbox_saga_unique UNIQUE (event_type, saga_action_id, booking_status, outbox_status)
);
CREATE INDEX flight_booking_outbox_booking_status
    ON flight.booking_outbox
        (event_type, booking_status);
