package com.traveladvisor.memberserver.service.domain.dto.command;

import com.traveladvisor.common.domain.vo.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * 회원가입 요청 커맨드
 */
public record CreateMemberCommand(
        @Schema(description = "이메일", example = "sunhyeokchoe@gmail.com")
        @NotNull @Email @Size(min = 5, max = 30, message = "이메일은 5~30자 사이어야 합니다.")
        String email,

        @Schema(description = "닉네임", example = "SunhyeokChoe")
        @NotBlank @Size(min = 5, max = 30, message = "닉네임은 5~30자 사이어야 합니다.")
        String nickname,

        @Schema(description = "이름", example = "Sunhyeok")
        @NotBlank @Size(max = 30, message = "이름은 최대 30자까지 허용합니다.")
        String firstName,

        @Schema(description = "성", example = "Choe")
        @NotBlank @Size(max = 30, message = "성은 최대 30자까지 허용합니다.")
        String lastName,

        @Schema(description = "전화번호", example = "+82 10 0000 0000")
        @NotBlank @Pattern(regexp = "^\\+?[0-9. ()-]{7,20}$", message = "잘못된 전화번호 형식입니다.")
        String contactNumber,

        @NotNull
        Gender gender

) {

}
