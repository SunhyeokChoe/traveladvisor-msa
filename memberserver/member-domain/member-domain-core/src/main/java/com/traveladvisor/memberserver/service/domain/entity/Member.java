package com.traveladvisor.memberserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.Gender;
import com.traveladvisor.common.domain.vo.MemberId;

public class Member extends AggregateRoot<MemberId> {

    private String email;
    private String nickname;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private Gender gender;

    public Member(Builder builder) {
        super.setId(builder.memberId);
        this.email = builder.email;
        this.nickname = builder.nickname;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.contactNumber = builder.contactNumber;
        this.gender = builder.gender;
    }

    // BEGIN: Getter
    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public Gender getGender() {
        return gender;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private MemberId memberId;
        private String email;
        private String nickname;
        private String firstName;
        private String lastName;
        private String contactNumber;
        private Gender gender;

        private Builder() {
        }

        public Builder memberId(MemberId memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder contactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
    // END: Builder

}
