package com.my.uum.groupproject.service;

import com.my.uum.groupproject.entity.PopulationState;
import com.my.uum.groupproject.repository.PopulationStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class PopulationStateService {

    private final PopulationStateRepository populationStateRepository;

    @Autowired
    public PopulationStateService(PopulationStateRepository populationStateRepository) {
        this.populationStateRepository = populationStateRepository;
    }

    public PopulationState addPopulationState(PopulationState populationState) {
        return populationStateRepository.save(populationState);
    }

    public PopulationState updatePopulationState(Long id, PopulationState populationState) {
        populationState.setId(id);
        return populationStateRepository.save(populationState);
    }

    public List<PopulationState> getPopulationDataByState(String state) {
        return populationStateRepository.findByState(state);
    }

    public void importCSVData(String csvFilePath) {
        String dbUrl = "jdbc:mysql://localhost:3306/population?serverTimezone=UTC";
        String username = "root";
        String password = "!Kqmysql2024";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            String insertQuery = "INSERT INTO population_state (state, date, sex, age, ethnicity, population) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                 BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

                // Skip the header
                String header = reader.readLine();
                String[] columns = header.split(",");

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    // Skip the row if the data length doesn't match the number of columns
                    if (data.length != columns.length) {
                        System.out.println("Skipping row due to inconsistent data length: " + line);
                        continue;
                    }

                    for (int i = 0; i < data.length; i++) {
                        switch (columns[i].trim()) {
                            case "state":
                            case "sex":
                            case "ethnicity":
                                preparedStatement.setString(i + 1, data[i]);
                                break;
                            case "date":
                                preparedStatement.setDate(i + 1, java.sql.Date.valueOf(data[i]));
                                break;
                            case "age":
                                try {
                                    int ageValue;
                                    String ageString = data[i].toLowerCase(); // Convert to lowercase for case-insensitive comparison

                                    if (ageString.equals("overall")) {
                                        // If "overall", you might want to set a default age value or handle it in some other way
                                        ageValue = -1; // For example, -1 could represent "overall" age
                                    } else if (ageString.contains("+")) {
                                        // If the value contains "+", treat it as a special case
                                        ageValue = Integer.parseInt(ageString.substring(0, ageString.indexOf("+")));
                                    } else if (ageString.contains("-")) {
                                        // If the value is a range, you might want to convert it to a representative age value
                                        String[] range = ageString.split("-");
                                        int lowerBound = Integer.parseInt(range[0]);
                                        int upperBound = Integer.parseInt(range[1]);
                                        ageValue = (lowerBound + upperBound) / 2; // Taking the average of the range
                                    } else {
                                        // Otherwise, parse the value as an integer
                                        ageValue = Integer.parseInt(ageString);
                                    }

                                    preparedStatement.setInt(i + 1, ageValue);
                                } catch (NumberFormatException e) {
                                    // Handle non-numeric values here
                                    System.out.println("Skipping non-numeric value for column " + columns[i] + ": " + data[i]);
                                    // You might also choose to set a default value or handle it in some other way
                                }
                                break;

                            case "population":
                                try {
                                    if (data[i] != null && !data[i].isEmpty()) {
                                        // Parse floating-point numbers as doubles instead of integers
                                        preparedStatement.setDouble(i + 1, Double.parseDouble(data[i]));
                                    } else {
                                        // If population is missing in the CSV, you might want to set a default value or handle it in some other way
                                        preparedStatement.setNull(i + 1, java.sql.Types.DOUBLE);
                                    }
                                } catch (NumberFormatException e) {
                                    // Handle non-numeric values here
                                    System.out.println("Skipping non-numeric value for column " + columns[i] + ": " + data[i]);
                                    // You might also choose to set a default value or handle it in some other way
                                }
                                break;
                        }
                    }

                    // Execute the query
                    preparedStatement.executeUpdate();
                }

                System.out.println("Data imported successfully.");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
