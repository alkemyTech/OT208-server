package com.alkemy.ong.seeder;

import com.alkemy.ong.models.ActivityEntity;
import com.alkemy.ong.repositories.IActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivitySeeder implements CommandLineRunner {

    private final IActivityRepository activityRepository;

    @Override
    public void run(String... args) throws Exception {
        loadActivityData();
    }

    private void loadActivityData() {
        if (activityRepository.count() == 0) {
            ActivityEntity activity1 = new ActivityEntity(null, "Utiles Escolares", "Utiles Escolares para escuelas Municipales (...)", "image.org/image_1.png", null, false);
            ActivityEntity activity2 = new ActivityEntity(null, "Juguetes para Navidad", "Juguetes para Navidad (...)", "image.org/image_2.png", null, false);
            activityRepository.save(activity1);
            activityRepository.save(activity2);
        }
    }
}
