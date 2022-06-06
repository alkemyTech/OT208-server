package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.ActivityDto;
import com.alkemy.ong.exeptions.ArgumentRequiredException;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.repositories.IActivityRepository;
import com.alkemy.ong.services.ActivityService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.stereotype.Service;

/**
 * @author nagredo
 * @project OT208-server
 * @class ActivityServiceImpl
 */
@Service
public class ActivityServiceImpl extends BasicServiceImpl<ActivityEntity, String, IActivityRepository>
        implements ActivityService {

    public static final String NO_EXIST = "El id no existe";

    public ActivityServiceImpl(IActivityRepository repository) {
        super(repository);
    }

    @Override
    public ActivityDto saveActivity(ActivityDto activityDto) {
        ActivityEntity activityEntity = ObjectMapperUtils.map(activityDto, ActivityEntity.class);

        activityEntity = this.save(activityEntity);
        return ObjectMapperUtils.map(activityEntity, ActivityDto.class);
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
