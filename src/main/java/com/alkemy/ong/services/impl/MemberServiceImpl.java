package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.repositories.IMemberRepository;
import com.alkemy.ong.services.MemberService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl extends BasicServiceImpl<MemberEntity, String, IMemberRepository> implements MemberService {

    private final MemberService memberService;

    public MemberServiceImpl(IMemberRepository repository, MemberService memberService) {
        super(repository);
        this.memberService = memberService;
    }

    public List<MemberResponseDto> getMembers() {
        List<MemberEntity> memberEntities = memberService.findAll();
        return ObjectMapperUtils.mapAll(memberEntities, MemberResponseDto.class);
    }

}
