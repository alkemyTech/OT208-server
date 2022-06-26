package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.repositories.IActivityRepository;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.ActivityService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;


@Service
public class ActivityServiceImpl extends BasicServiceImpl<ActivityEntity, String, IActivityRepository> implements ActivityService {

    public static final String NO_EXIST = "El id no existe";
    private AWSS3Service awss3Service;

    public ActivityServiceImpl(IActivityRepository repository, AWSS3Service awss3Service) {
        super(repository);
        this.awss3Service = awss3Service;
    }

    @Override
    @Transactional
    public BasicActivityDto saveActivity(EntryActivityDto entryActivityDto, String image) {

        ActivityEntity activityEntity = ObjectMapperUtils.map(entryActivityDto, ActivityEntity.class);

        if (StringUtils.hasText(image)) {
            activityEntity.setImage(image);
        }

        activityEntity = this.save(activityEntity);

        return ObjectMapperUtils.map(activityEntity, BasicActivityDto.class);
    }

    @Override
    @Transactional
    public BasicActivityDto updateActivity(EntryActivityDto dto, MultipartFile image, String id) {

        if (this.existById(id)) {
            ActivityEntity activityEntity = findById(id).get();
            activityEntity = ObjectMapperUtils.map(dto, activityEntity);
            if (!image.isEmpty()) {
                activityEntity.setImage(awss3Service.uploadFile(image));
            }
            activityEntity = this.edit(activityEntity);
            return ObjectMapperUtils.map(activityEntity, BasicActivityDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}
