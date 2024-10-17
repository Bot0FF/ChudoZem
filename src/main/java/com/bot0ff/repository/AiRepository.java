package com.bot0ff.repository;

import com.bot0ff.entity.Ai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository extends JpaRepository<Ai, Long> {

}
