package finalmission.dto.request;

import finalmission.domain.Role;

public record LoginUser(
        String userId,
        String name,
        Role role
) {
}
