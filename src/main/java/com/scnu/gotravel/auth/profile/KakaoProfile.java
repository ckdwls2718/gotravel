package com.scnu.gotravel.auth.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoProfile {
     KaKaoAccount kakao_aacout;


    @Data
    public class KaKaoAccount{
        private String email;
    }
}
