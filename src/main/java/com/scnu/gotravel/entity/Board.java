package com.scnu.gotravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "imageList", "location", "recommendList"})
@Builder
@AllArgsConstructor
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int recommendCount;

    @OneToMany(mappedBy = "board")
    private List<Image> imageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "board")
    private List<Recommend> recommendList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Board_HashTag> board_hashTags = new ArrayList<>();

    public void changeTitleAndContent(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void plusRecommend() {
        this.recommendCount++;
    }

    public void minusRecommend() {
        this.recommendCount--;
    }
}
