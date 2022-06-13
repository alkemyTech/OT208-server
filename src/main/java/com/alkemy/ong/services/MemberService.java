package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService extends BasicService<MemberEntity, String> {

    Page<MemberResponseDto> getMembers(Pageable pageable);

}
