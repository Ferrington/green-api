package com.techelevator.green.repository;

import com.techelevator.green.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findByStudentIsNotNull();
  Optional<User> findByUsername(String username);

    Optional<User> findBySetPasswordUUID(String uuid);
  Boolean existsByUsername(String username);
}
