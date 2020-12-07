package com.budget.app.repository;

import com.budget.app.entity.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetTypeRepository extends JpaRepository<BudgetType, Integer> {

}
