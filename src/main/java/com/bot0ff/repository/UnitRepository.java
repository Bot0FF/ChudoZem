package com.bot0ff.repository;

import com.bot0ff.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String userName);

    @Modifying
    @Query(value = "UPDATE unit SET " +
            "status = :status " +
            "WHERE id = :id", nativeQuery = true)
    void setStatus(@Param("status") String status,
                   @Param("id") Long id);
}
