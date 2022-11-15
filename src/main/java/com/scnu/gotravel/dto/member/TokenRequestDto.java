package com.scnu.gotravel.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TokenRequestDto {
    String accessToken;
    String refreshToken;
}