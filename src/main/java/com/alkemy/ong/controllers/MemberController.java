package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.members.EditMemberDto;
import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<MemberResponseDto>> getMembers(@PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.ok(memberService.getMembers(page));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestPart(name = "dto") EntryMemberDto entryMemberDto, Errors errors, @RequestPart(name = "img") MultipartFile file) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (!file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(memberService.createMember(entryMemberDto, file));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.createMember(entryMemberDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {
        if (!memberService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
        return new ResponseEntity<>(memberService.deleteMember(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable String id, @Valid @RequestPart(name = "dto") EditMemberDto editMemberDto, Errors errorsDto, @RequestPart(name = "img") MultipartFile file) {
        if (errorsDto.hasErrors()) {
            throw new ValidationException(errorsDto.getFieldErrors());
        }
        if (!memberService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");}
        if (!file.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(memberService.updateMember(editMemberDto, id, file));}
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.updateMember(editMemberDto, id));}

}
