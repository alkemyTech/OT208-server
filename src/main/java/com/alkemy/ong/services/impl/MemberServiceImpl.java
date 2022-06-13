package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.response.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.repositories.IMemberRepository;
import com.alkemy.ong.services.MemberService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<MemberResponseDto> getMembers(Pageable pageable) {
        List<MemberEntity> memberEntities = repository.findAll();
        List<MemberResponseDto> response;

        if (!memberEntities.isEmpty()) {
            response = ObjectMapperUtils.mapAll(memberEntities, MemberResponseDto.class);

            final int start = (int) pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), response.size());

            return new PageImpl<>(response.subList(start, end), pageable, response.size());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("There's no members"));
        }
    }

}
