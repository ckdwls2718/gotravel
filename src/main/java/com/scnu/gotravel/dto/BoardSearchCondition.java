package com.scnu.gotravel.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class BoardSearchCondition {
    String boardTitle;
    List<String> hashTags = new ArrayList<>();
    String memberNickname;
    String location;
}
