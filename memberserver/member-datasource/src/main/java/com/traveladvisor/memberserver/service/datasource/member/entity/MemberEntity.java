package com.traveladvisor.memberserver.service.datasource.member.entity;

import com.traveladvisor.common.datasource.common.entity.BaseAuditEntity;
import com.traveladvisor.common.domain.vo.Gender;
import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "members", schema = "member")
@Entity
public class MemberEntity extends BaseAuditEntity {

    @Id @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "gender") @Enumerated(EnumType.STRING)
    private Gender gender;

}
