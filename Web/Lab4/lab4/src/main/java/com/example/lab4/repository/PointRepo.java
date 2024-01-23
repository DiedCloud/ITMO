package com.example.lab4.repository;

import com.example.lab4.entity.PointEntity;
import com.example.lab4.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointRepo extends CrudRepository<PointEntity, Long> {

    List<PointEntity> findAllByUser(UserEntity entity);

    void deleteAllByUser(UserEntity entity);
}
