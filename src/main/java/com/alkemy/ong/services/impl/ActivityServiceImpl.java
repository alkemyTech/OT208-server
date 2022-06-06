package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.ActivityDto;
import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IActivityRepository;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.services.ActivityService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nagredo
 * @project OT208-server
 * @class ActivityServiceImpl
 */
@Service
public class ActivityServiceImpl extends BasicServiceImpl<ActivityEntity, String, IActivityRepository>
        implements ActivityService {

    public ActivityServiceImpl(IActivityRepository repository) {
        super(repository);
    }

    @Override
    public ActivityDto saveActivity(ActivityDto activityDto) {
        ActivityEntity activityEntity = ObjectMapperUtils.map(activityDto, ActivityEntity.class);

        activityEntity = this.save(activityEntity);
        return ObjectMapperUtils.map(activityEntity, ActivityDto.class);
    }
}
