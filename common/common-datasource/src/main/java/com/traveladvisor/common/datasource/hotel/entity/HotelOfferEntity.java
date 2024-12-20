package com.traveladvisor.common.datasource.hotel.entity;

import com.traveladvisor.common.datasource.common.entity.BaseZonedDateTimeEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "hotel_offers", schema = "hotel")
@Entity
public class HotelOfferEntity extends BaseZonedDateTimeEntity {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "rate_code")
    private String rateCode;

    @Column(name = "rate_family_code")
    private String rateFamilyCode;

    @Column(name = "rate_family_type")
    private String rateFamilyType;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "room_category")
    private String roomCategory;

    @Column(name = "room_beds")
    private Integer roomBeds;

    @Column(name = "room_bed_type")
    private String roomBedType;

    @Column(name = "room_description_text")
    private String roomDescriptionText;

    @Column(name = "room_description_lang")
    private String roomDescriptionLang;

    @Column(name = "guests_adults")
    private Integer guestsAdults;

    @Column(name = "price_currency")
    private String priceCurrency;

    @Column(name = "price_base")
    private BigDecimal priceBase;

    @Column(name = "price_total")
    private BigDecimal priceTotal;

    @Column(name = "price_variations")
    private String priceVariations;

    @Column(name = "cancellation_policies")
    private String cancellationPolicies;

    @Column(name = "payment_type")
    private String paymentType;

}
