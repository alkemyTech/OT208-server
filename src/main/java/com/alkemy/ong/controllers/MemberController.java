package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }

    @PostMapping("/create")
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestPart(name="dto") EntryMemberDto entryMemberDto, @RequestPart(name="img") Errors errors, MultipartFile file) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (!file.isEmpty()) {
            return ResponseEntity.ok(memberService.createMember(entryMemberDto, file));
        }
        return ResponseEntity.ok(memberService.createMember(entryMemberDto));
    }

}
