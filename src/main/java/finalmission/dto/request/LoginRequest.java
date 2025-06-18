package finalmission.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "이메일은 반드시 입력해주세요.")
        @Email(message = "유효한 이메일 형식을 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호는 반드시 입력해주세요.")
        String password
) {
}
