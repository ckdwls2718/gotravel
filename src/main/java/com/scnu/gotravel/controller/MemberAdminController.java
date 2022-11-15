package com.scnu.gotravel.controller;

import com.scnu.gotravel.dto.MemberDTO;
import com.scnu.gotravel.dto.MemberSearchCondition;
import com.scnu.gotravel.service.MemberAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    @GetMapping("/list")
    public ResponseEntity<Page<MemberDTO>> findAll(MemberSearchCondition memberSearchCondition, Pageable pageable){
        log.info("memberSearch : " + memberSearchCondition);
        Page<MemberDTO> result = memberAdminService.findAll(memberSearchCondition, pageable);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> findOne(@PathVariable("id")Long id){
        log.info("id : " + id);
        MemberDTO result = memberAdminService.findOne(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id")Long id){
        log.info("id : " + id);
        memberAdminService.delete(id);
        return HttpStatus.OK;
    }
}
