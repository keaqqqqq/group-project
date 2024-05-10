package com.my.uum.groupproject.repository;

import com.my.uum.groupproject.entity.PopulationState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PopulationStateRepository extends JpaRepository<PopulationState, Long> {
    List<PopulationState> findByState(String state);
}
