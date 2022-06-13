package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.repositories.IMemberRepository;
import com.alkemy.ong.services.MemberService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MemberServiceImpl extends BasicServiceImpl<MemberEntity, String, IMemberRepository> implements MemberService {

    public MemberServiceImpl(IMemberRepository repository) {
        super(repository);
    }

    @Override
    public List<MemberResponseDto> getMembers() {
        List<MemberEntity> memberEntities = repository.findAll();
        if (!memberEntities.isEmpty()) {
            return ObjectMapperUtils.mapAll(memberEntities, MemberResponseDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("There's no members"));
        }
    }

}
