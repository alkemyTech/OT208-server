package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<Page<MemberResponseDto>> getMembers(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMembers(pageable));
    }

}
