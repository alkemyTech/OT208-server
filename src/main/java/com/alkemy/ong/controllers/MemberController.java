package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.services.impl.MemberServiceImpl;
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

    private final MemberServiceImpl memberServiceImpl;

    @GetMapping("/list")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        if (memberServiceImpl.getMembers().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memberServiceImpl.getMembers());
    }


}
