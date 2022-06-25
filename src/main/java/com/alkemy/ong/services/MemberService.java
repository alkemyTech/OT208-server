package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.members.EditMemberDto;
import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService extends BasicService<MemberEntity, String> {

    MemberResponseDto createMember(EntryMemberDto entryMemberDto, MultipartFile file);
    MemberResponseDto createMember(EntryMemberDto entryMemberDto);
    Page<MemberResponseDto> getMembers(Pageable pageable);
    String deleteMember(String id);
    MemberResponseDto updateMember(EditMemberDto editMemberDto, String id, MultipartFile file);
    MemberResponseDto updateMember(EditMemberDto editMemberDto, String id);

}
