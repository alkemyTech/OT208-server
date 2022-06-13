package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;

import java.util.List;

public interface MemberService extends BasicService<MemberEntity, String> {

    List<MemberResponseDto> getMembers();

}
