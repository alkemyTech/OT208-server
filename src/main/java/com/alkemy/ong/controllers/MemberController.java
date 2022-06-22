package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.members.EditMemberDto;
import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "Members", description = "Endpoint to List, Create, Update or Delete Members")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok Get Page Members"),
            @ApiResponse(responseCode = "403", description = "Forbidden Access Denied"),
            @ApiResponse(responseCode = "404", description = "Not Found")})
    @Operation(summary = "Get all members", description = "Get all members only if logged in as a User or as Administrator")
    public ResponseEntity<Page<MemberResponseDto>> getMembers(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMembers(pageable));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member was successfully created"),
            @ApiResponse(responseCode = "400", description = "There are validation errors or Required request parts are missing"),
            @ApiResponse(responseCode = "403", description = "Forbidden Access Denied")})
    @Operation(summary = "Create a new member", description = "Create a new member only if logged in as a User or as Administrator")
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestPart(name = "dto") EntryMemberDto entryMemberDto, Errors errors,
                                                          @RequestPart(name = "img") MultipartFile file) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (!file.isEmpty()) {
            return ResponseEntity.ok(memberService.createMember(entryMemberDto, file));
        }
        return ResponseEntity.ok(memberService.createMember(entryMemberDto));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member was successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden Access Denied"),
            @ApiResponse(responseCode = "404", description = "Id is empty, or does not exist")})
    @Operation(summary = "Delete member", description = "Delete a member by id, only if logged in as Administrator")
    public ResponseEntity<String> deleteMember(@PathVariable @Parameter (name = "ID", description = "Member ID", example = "602685aa-cf03-4f2e-9350-811208cd92b2") String id) {

                memberService.deleteMember(id);
        return new ResponseEntity<>("Member was successfully deleted", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member was successfully updated"),
            @ApiResponse(responseCode = "400", description = "There are validation errors or Required request parts are missing"),
            @ApiResponse(responseCode = "403", description = "Forbidden Access Denied")})
    @Operation(summary = "Update member", description = "Update a member by id, only if logged in as Administrator")
    public ResponseEntity<MemberResponseDto> updateMember(@Valid @RequestPart(name = "dto") EditMemberDto editMemberDto, Errors errorsDto,
                                                          @PathVariable String id,
                                                          @Valid @RequestPart(name = "img") MultipartFile file, Errors errorsFile) {
        if (errorsDto.hasErrors()) {
            throw new ValidationException(errorsDto.getFieldErrors());
        }

        if (errorsFile.hasErrors()) {
            throw new ValidationException(errorsFile.getFieldErrors());
        }

        Optional<MemberEntity> memberOptional = memberService.findById(id);
        if (memberOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!file.isEmpty()) {
            return ResponseEntity.ok(memberService.updateMember(editMemberDto, memberOptional.get(), file));
        }
        return ResponseEntity.ok(memberService.updateMember(editMemberDto, memberOptional.get()));
    }

}
