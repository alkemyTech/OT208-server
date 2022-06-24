package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.repositories.IActivityRepository;
import com.alkemy.ong.services.ActivityService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class ActivityServiceImpl extends BasicServiceImpl<ActivityEntity, String, IActivityRepository> implements ActivityService {

    public static final String NO_EXIST = "El id no existe";

    public ActivityServiceImpl(IActivityRepository repository) {
        super(repository);
    }

    @Override
    public BasicActivityDto saveActivity(EntryActivityDto entryActivityDto, String image) {

        ActivityEntity activityEntity = ObjectMapperUtils.map(entryActivityDto, ActivityEntity.class);

        if (StringUtils.hasText(image)) {
            activityEntity.setImage(image);
        }

        activityEntity = this.save(activityEntity);

        return ObjectMapperUtils.map(activityEntity, BasicActivityDto.class);
    }

    @Override
    public BasicActivityDto updateActivity(EntryActivityDto dto, String image, String id) {

        if (this.existById(id)) {
            ActivityEntity activityEntity = findById(id).get();
            activityEntity = ObjectMapperUtils.map(dto, activityEntity);
            if (StringUtils.hasText(image)) {
                activityEntity.setImage(image);
            }
            activityEntity = this.edit(activityEntity);
            return ObjectMapperUtils.map(activityEntity, BasicActivityDto.class);
        } else {
            throw new RuntimeException(NO_EXIST);
        }

    }
}
