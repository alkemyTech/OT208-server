package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.contact.EntryContactDto;
import com.alkemy.ong.dto.response.contact.BasicContactDto;
import com.alkemy.ong.models.ContactEntity;
import com.alkemy.ong.repositories.IContactRepository;
import com.alkemy.ong.services.ContactService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class ContactServiceImpl extends BasicServiceImpl<ContactEntity, String, IContactRepository> implements ContactService {

    public ContactServiceImpl(IContactRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public BasicContactDto saveContact(EntryContactDto dto) {
        ContactEntity contactEntity = ObjectMapperUtils.map(dto, ContactEntity.class);
        contactEntity = this.save(contactEntity);
        return ObjectMapperUtils.map(contactEntity, BasicContactDto.class);
    }

    @Override
    @Transactional
    public List<BasicContactDto> getAllContacts() {
        return ObjectMapperUtils.mapAll(this.findAll(), BasicContactDto.class);
    }
}
