package com.techelevator.green.repository;

import com.techelevator.green.model.log.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
