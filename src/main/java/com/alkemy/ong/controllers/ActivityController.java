package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.ActivityDto;
import com.alkemy.ong.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author nagredo
 * @project OT208-server
 * @class ActivityController
 */
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;

    @PostMapping
    public ResponseEntity<ActivityDto> create(@Valid @RequestBody ActivityDto activityDto) {
        try {
            if (!activityDto.getName().isEmpty() || !activityDto.getContent().isEmpty())
                return new ResponseEntity<>(this.service.saveActivity(activityDto), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
