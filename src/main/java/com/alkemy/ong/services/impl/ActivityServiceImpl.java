package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.repositories.IActivityRepository;
import com.alkemy.ong.services.ActivityService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
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
    public ActivityDto updateActivity(ActivityDto activityDto) {
        if (this.existById(activityDto.getId())) {
            ActivityEntity activityEntity = ObjectMapperUtils.map(activityDto, ActivityEntity.class);

            activityEntity = this.save(activityEntity);
            return ObjectMapperUtils.map(activityEntity, ActivityDto.class);
        } else {
            throw new ArgumentRequiredException(NO_EXIST);
        }
    }
}
