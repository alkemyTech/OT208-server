package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<Page<MemberResponseDto>> getMembers(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMembers(pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestPart(name="dto") EntryMemberDto entryMemberDto, Errors errors, @RequestPart(name="img") MultipartFile file) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (!file.isEmpty()) {
            return ResponseEntity.ok(memberService.createMember(entryMemberDto, file));
        }
        return ResponseEntity.ok(memberService.createMember(entryMemberDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>("Member was successfully deleted", HttpStatus.OK);
    }

}
