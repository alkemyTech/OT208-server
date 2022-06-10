package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.ContactDto;
import com.alkemy.ong.models.ContactEntity;
import com.alkemy.ong.repositories.IContactRepository;
import com.alkemy.ong.services.ContactService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.stereotype.Service;


@Service
public class ContactServiceImple extends BasicServiceImpl<ContactEntity, String, IContactRepository> implements ContactService {

    public ContactServiceImple(IContactRepository repository) {
        super(repository);
    }

    @Override
    public ContactDto saveContact(ContactDto contactDto) {
        ContactEntity contactEntity = ObjectMapperUtils.map(contactDto, ContactEntity.class);

        contactEntity = this.save(contactEntity);
        return ObjectMapperUtils.map(contactEntity, ContactDto.class);
    }
}
