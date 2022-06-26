package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.models.ActivityEntity;
import org.springframework.web.multipart.MultipartFile;


public interface ActivityService extends BasicService<ActivityEntity, String> {

    BasicActivityDto saveActivity(EntryActivityDto entryActivityDto, String image);

    BasicActivityDto updateActivity(EntryActivityDto entryActivityDto, MultipartFile image, String id);

}
