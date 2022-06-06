package com.alkemy.ong.services.impl;

import com.alkemy.ong.models.ContactEntity;
import com.alkemy.ong.repositories.IContactRepository;
import com.alkemy.ong.services.ContactService;
import org.springframework.stereotype.Service;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactServiceImple
 */
@Service
public class ContactServiceImple extends BasicServiceImpl<ContactEntity, String, IContactRepository> implements ContactService {

    public ContactServiceImple(IContactRepository repository) {
        super(repository);
    }
}
