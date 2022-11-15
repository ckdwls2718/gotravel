package com.scnu.gotravel.entity;

import com.scnu.gotravel.dto.MemberDTO;
import com.sun.istack.NotNull;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"boardList","recommendList"})
@Builder
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 사용자 이름

    @Column(unique = true)
    @NotNull
    private String email; // 사용자 이메일

    @Column(unique = true)
    @NotNull
    private String nickName; // 닉네임

    @NotNull
    private String password; // 비밀번호

    private String provider;

    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Board> boardList = new ArrayList<>(); // 사용자 게시물

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Recommend> recommendList = new ArrayList<>(); // 추천 리스트

    public void changeNickNameAndPassword(MemberDTO memberDTO){
        this.nickName = memberDTO.getNickname();
        this.password = memberDTO.getPassword();
    }

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Role> role = new ArrayList<>();

    public void addRole(Role role) {
        this.role.add(role);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Member(String name, String email, String nickName, String password, String provider, String refreshToken) {
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.provider = provider;
    }
}
