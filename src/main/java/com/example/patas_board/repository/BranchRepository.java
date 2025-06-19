package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findAll();
}
