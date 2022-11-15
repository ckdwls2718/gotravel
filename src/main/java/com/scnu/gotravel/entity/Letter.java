package com.scnu.gotravel.entity;

import com.scnu.gotravel.dto.LetterDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"sendMember","receiveMember"})
@Builder
@AllArgsConstructor
public class Letter extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sendMember", nullable = false)
    private Member sendMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverMember", nullable = false)
    private Member receiveMember;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public void changeTitleAndContent(LetterDto letterDto) {
        this.title = letterDto.getTitle();
        this.content = letterDto.getContent();
    }

}
