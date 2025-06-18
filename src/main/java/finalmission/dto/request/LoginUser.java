package finalmission.dto.request;

import finalmission.domain.Role;

public record LoginUser(
        String email,
        String name,
        Role role
) {
}
