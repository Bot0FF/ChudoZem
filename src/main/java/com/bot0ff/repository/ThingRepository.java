package com.bot0ff.repository;

import com.bot0ff.entity.Thing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThingRepository extends JpaRepository<Thing, Long> {
    List<Thing> findByOwnerId(Long ownerId);
}
