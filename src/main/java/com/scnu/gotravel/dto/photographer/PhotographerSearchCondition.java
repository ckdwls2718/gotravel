package com.scnu.gotravel.dto.photographer;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class PhotographerSearchCondition {

    String location;
    List<String> hashtag = new ArrayList<>();
    String memberNickname;

}
