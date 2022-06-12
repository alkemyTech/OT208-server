package com.alkemy.ong.services.impl;

import com.alkemy.ong.models.MemberEntity;
import com.alkemy.ong.repositories.IMemberRepository;
import com.alkemy.ong.services.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends BasicServiceImpl<MemberEntity, String, IMemberRepository> implements MemberService {

    public MemberServiceImpl(IMemberRepository repository) {
        super(repository);
    }

}
