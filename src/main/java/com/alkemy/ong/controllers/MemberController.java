package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.services.MemberService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<MemberResponseDto>> getComments() {
        List<MemberEntity> memberEntities = memberService.findAll();
        if (memberEntities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ObjectMapperUtils.mapAll(memberEntities, MemberResponseDto.class));
    }


}
