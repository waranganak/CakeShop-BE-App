package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("SELECT MAX(c.id) FROM Category c")
    Long findMaxId();

}