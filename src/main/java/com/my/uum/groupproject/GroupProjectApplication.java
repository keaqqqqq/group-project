package com.my.uum.groupproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.my.uum.groupproject.service.PopulationStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableJpaRepositories("com.my.uum.groupproject.*")
@ComponentScan( "com.my.uum.groupproject.*")
@EntityScan("com.my.uum.groupproject.*")
public class GroupProjectApplication implements CommandLineRunner {

    private final PopulationStateService populationStateService;

    @Autowired
    public GroupProjectApplication(PopulationStateService populationStateService) {
        this.populationStateService = populationStateService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GroupProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Specify the CSV file path here
        String csvFilePath = "C:\\Users\\User\\Downloads\\population_state.csv";
        populationStateService.importCSVData(csvFilePath);
    }
}
