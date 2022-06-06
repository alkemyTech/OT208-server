package com.alkemy.ong.services;

import com.alkemy.ong.dto.ActivityDto;
import com.alkemy.ong.models.ActivityEntity;

/**
 * @author nagredo
 * @project OT208-server
 * @class ActivityService
 */
public interface ActivityService extends BasicService<ActivityEntity, String> {
    ActivityDto saveActivity(ActivityDto activityDto);

    ActivityDto updateActivity(ActivityDto activityDto);
}
