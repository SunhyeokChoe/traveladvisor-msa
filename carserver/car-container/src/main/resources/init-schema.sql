----------------------------------------
-- 차량(렌트카) 스키마 및 테이블 정의
----------------------------------------

DROP TABLE IF EXISTS car.cars CASCADE;
CREATE SCHEMA IF NOT EXISTS car;

----------------------------------------
-- Booking Status ENUM
----------------------------------------
DROP TYPE IF EXISTS car.booking_status;
CREATE TYPE car.booking_status AS ENUM (
    'CAR_BOOKED',
    'CAR_CANCELLED');

----------------------------------------
-- Outbox Status ENUM
----------------------------------------
DROP TYPE IF EXISTS car.outbox_status;
CREATE TYPE car.outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

----------------------------------------
-- Car Offers Table
----------------------------------------
CREATE TABLE car.car_offers
(
    id           BIGSERIAL PRIMARY KEY,
    vehicle_code VARCHAR(20)                            NOT NULL,
    category     VARCHAR(10)                            NOT NULL,
    description  TEXT                                   NOT NULL,
    image_url    TEXT                                   NOT NULL,
    baggages     JSONB                                  NOT NULL,
    seats        JSONB                                  NOT NULL,
    price        NUMERIC(10, 2)                         NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE
);
COMMENT
ON TABLE car.car_offers IS '렌트 차량 정보 테이블';
COMMENT
ON COLUMN car.car_offers.vehicle_code IS '차량 코드 (예: CAR, VAN, ELC)';
COMMENT
ON COLUMN car.car_offers.category IS '차량 카테고리 (예: ST, BU, FC)';
COMMENT
ON COLUMN car.car_offers.description IS '차량 설명 (예: Mercedes E Class...등)';
COMMENT
ON COLUMN car.car_offers.image_url IS '차량 이미지 URL';
COMMENT
ON COLUMN car.car_offers.baggages IS '수하물 정보 (예: 가방 개수, 사이즈 등)';
COMMENT
ON COLUMN car.car_offers.seats IS '좌석 정보 (예: 좌석 수, 사이즈 등)';
COMMENT
ON COLUMN car.car_offers.price IS '차량 가격 정보';
COMMENT
ON COLUMN car.car_offers.created_at IS '레코드 생성 시각 (기본값: 현재 시각)';

----------------------------------------
-- Car Booking Approval Status Table
----------------------------------------
DROP TABLE IF EXISTS car.booking_approval CASCADE;
CREATE TABLE car.booking_approval
(
    id            UUID               NOT NULL,
    car_offers_id BIGINT             NOT NULL,
    booking_id    UUID               NOT NULL,
    status        car.booking_status NOT NULL,
    CONSTRAINT booking_approval_pkey PRIMARY KEY (id)
);

----------------------------------------
-- Car Booking Outbox Table
----------------------------------------
DROP TABLE IF EXISTS car.booking_outbox CASCADE;
CREATE TABLE car.booking_outbox
(
    id             UUID PRIMARY KEY,
    saga_action_id UUID               NOT NULL,
    event_type     VARCHAR(20)        NOT NULL, -- 이벤트 타입(CAR_BOOKED, CAR_CANCELLED)
    event_payload  JSONB              NOT NULL, -- 이벤트 데이터(JSON)
    booking_status car.booking_status NOT NULL,
    outbox_status  car.outbox_status  NOT NULL DEFAULT 'STARTED',
    created_at     TIMESTAMP WITH TIME ZONE    DEFAULT now() NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE,
    completed_at   TIMESTAMP WITH TIME ZONE,
    version        INTEGER            NOT NULL,
    CONSTRAINT booking_outbox_saga_unique UNIQUE (event_type, saga_action_id, booking_status, outbox_status)
);
CREATE INDEX car_booking_outbox_booking_status
    ON car.booking_outbox
        (event_type, booking_status);

-- DROP
-- MATERIALIZED VIEW IF EXISTS car.reservation_car_m_view;
-- CREATE
-- MATERIALIZED VIEW car.reservation_car_m_view TABLESPACE pg_default
-- AS
-- SELECT r.id        AS car_offers_id,
--        r.name      AS car_name,
--        p.id        AS product_id,
--        p.name      AS product_name,
--        p.price     AS product_price,
--        p.available AS product_available
-- FROM car.car_offers r,
--      car.products p,
--      car.car_products rp
-- WHERE r.id = rp.car_offers_id
--   AND p.id = rp.product_id WITH DATA;
--
-- refresh
-- materialized VIEW car.reservation_car_m_view;
-- DROP function IF EXISTS car.refresh_reservation_car_m_view;
-- CREATE
-- OR replace function car.refresh_reservation_car_m_view()
--               returns trigger
--               AS '
-- BEGIN
--     refresh materialized VIEW car.reservation_car_m_view;
--     return null;
-- END;
-- '  LANGUAGE plpgsql;
--
-- DROP trigger IF EXISTS refresh_reservation_car_m_view ON car.car_products;
--
-- CREATE trigger refresh_reservation_car_m_view
--     after INSERT OR
-- UPDATE OR
-- DELETE
-- OR truncate
-- ON car.car_products FOR each statement
--     EXECUTE PROCEDURE car.refresh_reservation_car_m_view();
