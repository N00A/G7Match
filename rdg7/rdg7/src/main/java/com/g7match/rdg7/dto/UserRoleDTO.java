package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleDTO {
    private Long userId;
    private Long roleId;
}
