package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService extends BasicService<MemberEntity, String> {

    List<MemberResponseDto> getMembers();
    MemberResponseDto createMember(EntryMemberDto entryMemberDto, MultipartFile file);
    MemberResponseDto createMember(EntryMemberDto entryMemberDto);

}
