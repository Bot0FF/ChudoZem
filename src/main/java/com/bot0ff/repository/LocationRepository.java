package com.bot0ff.repository;

import com.bot0ff.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT * FROM location " +
            "WHERE x = :x AND y = :y" , nativeQuery = true)
    Optional<Location> findLocation(@Param("x") int x,
                                    @Param("y") int y);
}