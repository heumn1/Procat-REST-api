package ru.heumn.Procat.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.heumn.Procat.storage.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByLogin(String login);
}
