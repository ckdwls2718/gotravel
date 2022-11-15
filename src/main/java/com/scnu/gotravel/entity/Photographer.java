package com.scnu.gotravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member","photographerImages","photographer_hashTags"})
@Builder
@AllArgsConstructor
public class Photographer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photographer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.REMOVE)
    private List<PhotographerImage> photographerImages = new ArrayList<>();

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.REMOVE)
    private List<PhotographerHashTag> photographerHashTags = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public void changeContent( String content){
        this.content = content;
    }

}