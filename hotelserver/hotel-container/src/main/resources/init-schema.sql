----------------------------------------
-- 호텔, 국가 스키마 및 테이블 정의
----------------------------------------

DROP SCHEMA IF EXISTS hotel CASCADE;
CREATE SCHEMA hotel;

----------------------------------------
-- Booking Status ENUM
----------------------------------------
DROP TYPE IF EXISTS hotel.booking_status;
CREATE TYPE hotel.booking_status AS ENUM (
    'COMPLETED',
    'CANCELLED',
    'FAILED');

----------------------------------------
-- Outbox Status ENUM
----------------------------------------
DROP TYPE IF EXISTS hotel.outbox_status;
CREATE TYPE hotel.outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

----------------------------------------
-- Country Table
----------------------------------------
DROP TABLE IF EXISTS hotel.countries CASCADE;
CREATE TABLE hotel.countries
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255)                               NOT NULL,
    name_translated VARCHAR(255)                               NOT NULL,
    iso_code        VARCHAR(3)                                 NOT NULL,
    iso_code2       VARCHAR(2)                                 NOT NULL,
    iata_code       VARCHAR(3)                                 NOT NULL,
    longitude       NUMERIC(10, 7)           DEFAULT 0.0000000 NOT NULL,
    latitude        NUMERIC(10, 7)           DEFAULT 0.0000000 NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now()     NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    CONSTRAINT hotel_countries_iso_code_uq UNIQUE (iso_code),
    CONSTRAINT hotel_countries_iso_code2_uq UNIQUE (iso_code2)
);
COMMENT
ON TABLE hotel.countries IS '국가 정보 테이블';
COMMENT
ON COLUMN hotel.countries.name IS '국가 이름';
COMMENT
ON COLUMN hotel.countries.name_translated IS '번역된 국가 이름';
COMMENT
ON COLUMN hotel.countries.iso_code IS '국가 ISO';
COMMENT
ON COLUMN hotel.countries.iso_code2 IS '국가 ISO-2';
COMMENT
ON COLUMN hotel.countries.iata_code IS '공항 코드';
COMMENT
ON COLUMN hotel.countries.longitude IS '경도';
COMMENT
ON COLUMN hotel.countries.latitude IS '위도';
COMMENT
ON COLUMN hotel.countries.created_at IS '등록 일시';
COMMENT
ON COLUMN hotel.countries.updated_at IS '최종 수정 일시';

----------------------------------------
-- Hotel Metadata Table
----------------------------------------
-- OTA 업체는 다양하고, 실제 호텔 예약 시스템을 만들 때 여러 OTA로부터 호텔 데이터를 받아와 정규화 하는 작업이 필요합니다.
-- 이 데모 프로젝트에서는 도메인을 너무 복잡하지 않게 설계하기 위해 Amadeus OTA 하나만 사용하기로 했으므로 호텔 테이블 이름도 hotels로 단일화 했습니다.
DROP TABLE IF EXISTS hotel.hotels CASCADE;
CREATE TABLE hotel.hotels
(
    id              BIGSERIAL PRIMARY KEY,
    ota_hotel_id    CHAR(8)                                NOT NULL,
    name            VARCHAR(255)                           NOT NULL,
    iata_code       VARCHAR(3)                             NOT NULL,
    longitude       NUMERIC(10, 7)                         NOT NULL,
    latitude        NUMERIC(10, 7)                         NOT NULL,
    chain_code      VARCHAR(10)                            NOT NULL,
    country_code    VARCHAR(2)                             NOT NULL,
    dupe_id         BIGINT                                 NOT NULL,
    ota_last_update TIMESTAMP                              NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    CONSTRAINT hotel_hotels_ota_hotel_id_uq UNIQUE (ota_hotel_id)
);
COMMENT
ON TABLE hotel.hotels IS '호텔 정보 테이블';
COMMENT
ON COLUMN hotel.hotels.ota_hotel_id IS 'OTA 시스템에서 제공하는 호텔 ID';
COMMENT
ON COLUMN hotel.hotels.name IS '호텔 이름';
COMMENT
ON COLUMN hotel.hotels.iata_code IS 'IATA 공항 코드';
COMMENT
ON COLUMN hotel.hotels.longitude IS '호텔의 경도';
COMMENT
ON COLUMN hotel.hotels.latitude IS '호텔의 위도';
COMMENT
ON COLUMN hotel.hotels.chain_code IS '호텔 체인 코드';
COMMENT
ON COLUMN hotel.hotels.country_code IS '호텔의 국가 코드 (ISO-2)';
COMMENT
ON COLUMN hotel.hotels.dupe_id IS '중복 검출을 위한 ID';
COMMENT
ON COLUMN hotel.hotels.ota_last_update IS 'OTA에서 마지막 업데이트 시간';
COMMENT
ON COLUMN hotel.hotels.created_at IS '등록 일시';
COMMENT
ON COLUMN hotel.hotels.updated_at IS '최종 수정 일시';

DROP TABLE IF EXISTS hotel.hotel_offers CASCADE;
CREATE TABLE hotel.hotel_offers
(
    id                    BIGSERIAL PRIMARY KEY,
    hotel_id              BIGINT                                 NOT NULL,
    check_in_date         DATE                                   NOT NULL,
    check_out_date        DATE                                   NOT NULL,
    rate_code             VARCHAR(20),
    rate_family_code      VARCHAR(20),
    rate_family_type      VARCHAR(10),
    room_type             VARCHAR(20),
    room_category         VARCHAR(50),
    room_beds             INTEGER,
    room_bed_type         VARCHAR(50),
    room_description_text TEXT,
    room_description_lang VARCHAR(5),
    guests_adults         INTEGER,
    price_currency        VARCHAR(3)                             NOT NULL,
    price_base            NUMERIC(10, 2)                         NOT NULL,
    price_total           NUMERIC(10, 2)                         NOT NULL,
    price_variations      JSONB,
    cancellation_policies JSONB,
    payment_type          VARCHAR(20),
    created_at            TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at            TIMESTAMP WITH TIME ZONE,
    CONSTRAINT hotel_offers_hotel_id_fk FOREIGN KEY (hotel_id)
        REFERENCES hotel.hotels (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT hotel_offers_hotel_id_uq UNIQUE (hotel_id)
);

COMMENT
ON TABLE hotel.hotel_offers IS '호텔 예약 가능 오퍼 정보 테이블';
COMMENT
ON COLUMN hotel.hotel_offers.hotel_id IS 'hotel.hotels 테이블의 PK 참조 외래키';
COMMENT
ON COLUMN hotel.hotel_offers.check_in_date IS '체크인 날짜';
COMMENT
ON COLUMN hotel.hotel_offers.check_out_date IS '체크아웃 날짜';
COMMENT
ON COLUMN hotel.hotel_offers.rate_code IS '요금 코드 (예: RAC)';
COMMENT
ON COLUMN hotel.hotel_offers.rate_family_code IS '요금 패밀리 코드 (예: BAR)';
COMMENT
ON COLUMN hotel.hotel_offers.rate_family_type IS '요금 패밀리 타입 (예: P)';
COMMENT
ON COLUMN hotel.hotel_offers.room_type IS '객실 타입 (예: REG)';
COMMENT
ON COLUMN hotel.hotel_offers.room_category IS '객실 카테고리 (예: DELUXE_ROOM)';
COMMENT
ON COLUMN hotel.hotel_offers.room_beds IS '객실 침대 수';
COMMENT
ON COLUMN hotel.hotel_offers.room_bed_type IS '침대 타입 (예: QUEEN)';
COMMENT
ON COLUMN hotel.hotel_offers.room_description_text IS '객실 설명 텍스트';
COMMENT
ON COLUMN hotel.hotel_offers.room_description_lang IS '객실 설명 언어 코드';
COMMENT
ON COLUMN hotel.hotel_offers.guests_adults IS '성인 투숙객 수';
COMMENT
ON COLUMN hotel.hotel_offers.price_currency IS '가격 통화 코드 (예: GBP)';
COMMENT
ON COLUMN hotel.hotel_offers.price_base IS '기본 요금';
COMMENT
ON COLUMN hotel.hotel_offers.price_total IS '총 요금';
COMMENT
ON COLUMN hotel.hotel_offers.price_variations IS '가격 변동 정보';
COMMENT
ON COLUMN hotel.hotel_offers.cancellation_policies IS '취소 정책';
COMMENT
ON COLUMN hotel.hotel_offers.payment_type IS '지불 타입 (예: guarantee)';
COMMENT
ON COLUMN hotel.hotel_offers.created_at IS '레코드 생성 시각';
COMMENT
ON COLUMN hotel.hotel_offers.updated_at IS '레코드 수정 시각';

----------------------------------------
-- Hotel Booking Approval Status Table
----------------------------------------
DROP TABLE IF EXISTS hotel.booking_approval CASCADE;
CREATE TABLE hotel.booking_approval
(
    id              UUID                 NOT NULL,
    hotel_offers_id BIGINT               NOT NULL,
    booking_id      UUID                 NOT NULL,
    status          hotel.booking_status NOT NULL,
    CONSTRAINT booking_approval_pkey PRIMARY KEY (id)
);
ALTER TABLE hotel.booking_approval
    ADD CONSTRAINT booking_approval_hotel_offers_id_fkey FOREIGN KEY (hotel_offers_id)
        REFERENCES hotel.hotels (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

DROP TABLE IF EXISTS hotel.products CASCADE;
CREATE TABLE hotel.products
(
    id        uuid           NOT NULL,
    name      VARCHAR(30)    NOT NULL,
    price     numeric(10, 2) NOT NULL,
    available boolean        NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS hotel.hotel_products CASCADE;
CREATE TABLE hotel.hotel_products
(
    id         uuid   NOT NULL,
    hotel_id   BIGINT NOT NULL,
    product_id uuid   NOT NULL,
    CONSTRAINT hotel_products_pkey PRIMARY KEY (id)
);
ALTER TABLE hotel.hotel_products
    ADD CONSTRAINT FK_HOTEL_ID FOREIGN KEY (hotel_id)
        REFERENCES hotel.hotels (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;
ALTER TABLE hotel.hotel_products
    ADD CONSTRAINT FK_PRODUCT_ID FOREIGN KEY (product_id)
        REFERENCES hotel.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

----------------------------------------
-- Hotel Booking Outbox Table
----------------------------------------
DROP TABLE IF EXISTS hotel.booking_outbox CASCADE;
CREATE TABLE hotel.booking_outbox
(
    id             UUID PRIMARY KEY,
    saga_action_id UUID                 NOT NULL,
    event_type     VARCHAR(20)          NOT NULL,
    event_payload  JSONB                NOT NULL,
    booking_status hotel.booking_status NOT NULL,
    outbox_status  hotel.outbox_status  NOT NULL DEFAULT 'STARTED',
    created_at     TIMESTAMP WITH TIME ZONE      DEFAULT now() NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE,
    completed_at   TIMESTAMP WITH TIME ZONE,
    version        INTEGER              NOT NULL,
    CONSTRAINT booking_outbox_saga_unique UNIQUE (event_type, saga_action_id, booking_status, outbox_status)
);
CREATE INDEX "hotel_booking_outbox_booking_status"
    ON "hotel".booking_outbox
        (event_type, booking_status);
