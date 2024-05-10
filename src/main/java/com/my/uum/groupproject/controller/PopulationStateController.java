package com.my.uum.groupproject.controller;

import com.my.uum.groupproject.entity.PopulationState;
import com.my.uum.groupproject.service.PopulationStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/population-states")
public class PopulationStateController {

    private final PopulationStateService populationStateService;

    @Autowired
    public PopulationStateController(PopulationStateService populationStateService) {
        this.populationStateService = populationStateService;
    }

    @PostMapping
    public ResponseEntity<PopulationState> addPopulationState(@RequestBody PopulationState populationState) {
        PopulationState savedPopulationState = populationStateService.addPopulationState(populationState);
        return new ResponseEntity<>(savedPopulationState, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopulationState> updatePopulationState(
            @PathVariable Long id,
            @RequestBody PopulationState populationState
    ) {
        PopulationState updatedPopulationState = populationStateService.updatePopulationState(id, populationState);
        return new ResponseEntity<>(updatedPopulationState, HttpStatus.OK);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<PopulationState>> getPopulationDataByState(@PathVariable String state) {
        List<PopulationState> populationData = populationStateService.getPopulationDataByState(state);
        return new ResponseEntity<>(populationData, HttpStatus.OK);
    }
}
