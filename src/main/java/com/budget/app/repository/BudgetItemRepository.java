package com.budget.app.repository;

import com.budget.app.entity.BudgetItem;
import com.budget.app.model.budgetItem.BudgetItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Integer> {

    // method to get the list of budget items
    @Query(
    value = "select new com.budget.app.model.budgetItem.BudgetItemResponse(b.id, b.amount, b.description, b.dateOfTransaction, b.category.name)"
            + " from BudgetItem b"
            + " where b.user.id = ?1"
            + " and b.dateOfTransaction between ?2 and ?3"
            + " and b.budgetType.type = ?4")
    List<BudgetItemResponse> getBudgetItems(int userId, LocalDate startDate, LocalDate endDate, String type, Pageable pageable);

    // method to get the total count of budget items
    @Query(
    value = "select count(b)"
            + " from BudgetItem b"
            + " where b.user.id = ?1"
            + " and b.dateOfTransaction between ?2 and ?3"
            + " and b.budgetType.type = ?4")
    int findBudgetItemsCount(int userId, LocalDate fromDate, LocalDate toDate, String type);

    // method to get total amount of income and expense in the current month
    @Query(
    value = "select sum(b.amount)" +
            " from BudgetItem b" +
            " where b.user.id = ?1" +
            " and b.dateOfTransaction between ?2 and ?3" +
            " and b.budgetType.id = ?4")
    int totalAmount(int userId, LocalDate fromDate, LocalDate toDate, int typeId);

    // method for deleting list of items
    void deleteByIdIn(List<Integer> ids);
}
