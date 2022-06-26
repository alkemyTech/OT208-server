package com.alkemy.ong.seeder;

import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrganizationSeeder implements CommandLineRunner {

    private final IOrganizationRepository organizationRepository;

    @Override
    public void run(String... args) throws Exception {
        loadOrganizationData();
    }

    private void loadOrganizationData() {
        if (organizationRepository.count() == 0) {
            OrganizationEntity organization = new OrganizationEntity(null,
                    "Somos Más", "https://cohorte-mayo-2820e45d.s3.amazonaws.com/1654056131209.jpg", null, 1160112988,
                    "somosfundacionmas@gmail.com", "Mejorar la calidad de vida de niños y familias en situación de " +
                    "vulnerabilidad en el barrio La Cava, otorgando un cambio de rumbo en cada individuo a través de " +
                    "la educación, salud, trabajo, deporte, responsabilidad y compromiso", "Desde 1997 en Somos Más " +
                    "trabajamos con los chicos y chicas, nmamás y papás, abuelos y vecinos del barrio La Cava " +
                    "generando procesos de crecimiento y de inserción social. Uniendo las manos de ntodas las familias, " +
                    "las que viven en el barrio y las que viven fuera de él, es que podemos pensar, crear y garantizar " +
                    "estos procesos. Somos una asociación civil sin fines de lucro que se creó en 1997 con la " +
                    "intención de dar alimento a las familias del barrio. Con el tiempo fuimos involucrándonos con " +
                    "la comunidad y agrandando y mejorando nuestra capacidad de trabajo. Hoy somos un centro " +
                    "comunitario que acompaña a más de 700 personas a través de las áreas de: Educación, deportes, " +
                    "primera infancia, salud, alimentación y trabajo social.", "Somos_Más", null, "SomosMás",
                    LocalDateTime.now(), false);
            organizationRepository.save(organization);
        }
    }

}