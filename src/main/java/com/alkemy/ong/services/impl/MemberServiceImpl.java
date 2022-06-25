package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.members.EditMemberDto;
import com.alkemy.ong.dto.request.members.EntryMemberDto;
import com.alkemy.ong.dto.response.members.MemberResponseDto;
import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.repositories.IMemberRepository;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.MemberService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MemberServiceImpl extends BasicServiceImpl<MemberEntity, String, IMemberRepository> implements MemberService {

    private static final String PICTURE = "https://cohorte-mayo-2820e45d.s3.amazonaws.com/439df0793dfa45648365e4beeed292f4.png";
    private final AWSS3Service awss3Service;

    public MemberServiceImpl(IMemberRepository repository, AWSS3Service awss3Service) {
        super(repository);
        this.awss3Service = awss3Service;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("No members found"));
        }
    }

    @Override
    @Transactional
    public String deleteMember(String id) {
        try {
            repository.deleteById(id);
            return "Member was successfully deleted";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("Member not found"));
        }
    }

    @Override
    @Transactional
    public MemberResponseDto createMember(EntryMemberDto entryMemberDto, MultipartFile file) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setImage(awss3Service.uploadFile(file));
        ObjectMapperUtils.map(entryMemberDto, memberEntity);
        memberEntity = this.save(memberEntity);
        return ObjectMapperUtils.map(memberEntity, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public MemberResponseDto createMember(EntryMemberDto entryMemberDto) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setImage(PICTURE);
        ObjectMapperUtils.map(entryMemberDto, memberEntity);
        memberEntity = this.save(memberEntity);
        return ObjectMapperUtils.map(memberEntity, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public MemberResponseDto updateMember(EditMemberDto editMemberDto, String id, MultipartFile file) {
        MemberEntity memberEntity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ("Member not found")));
        memberEntity.setImage(awss3Service.uploadFile(file));
        edit(ObjectMapperUtils.map(editMemberDto, memberEntity));
        return ObjectMapperUtils.map(memberEntity, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public MemberResponseDto updateMember(EditMemberDto editMemberDto, String id) {
        MemberEntity memberEntity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ("Member not found")));
        edit(ObjectMapperUtils.map(editMemberDto, memberEntity));
        return ObjectMapperUtils.map(memberEntity, MemberResponseDto.class);
    }

}
