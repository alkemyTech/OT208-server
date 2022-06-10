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
            ActivityEntity activity1 = new ActivityEntity("bbcaab80-2982-43a3-b3a1-509cb35d162c","Utiles escolares para escuelas municipales", "Utiles escolares para escuelas municipales (...)", "image.org/image_1.png", null, false);
            ActivityEntity activity2 = new ActivityEntity("c71b2a6b-6f74-4f4c-8ddd-3dafbb636b93","Juguetes por Navidad", "Juguetes por Navidad (...)", "image.org/image_2.png", null, false);
            activityRepository.save(activity1);
            activityRepository.save(activity2);
        }
    }
}
