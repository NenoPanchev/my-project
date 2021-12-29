package project.dailynail.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.dailynail.models.entities.ArticleEntity;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query(value = "SELECT a.id " +
            "FROM articles AS a " +
            "LEFT JOIN categories AS c ON a.category_id = c.id " +
            "LEFT JOIN subcategories AS s ON a.subcategory_id = s.id " +
            "LEFT JOIN users AS u ON a.author_id = u.id " +
            "WHERE CONCAT(a.title, a.text) LIKE %:keyWord% " +
            "AND CONCAT(COALESCE(c.category_name, ''), COALESCE(s.subcategory_name, '')) LIKE %:category% " +
            "AND u.full_name LIKE %:author% " +
            "AND IF(a.activated, 'true', 'false') LIKE %:activated% " +
            "AND DATEDIFF(NOW(), a.created) <= :days", nativeQuery = true)
    Page<String> findAllArticleIdBySearchFilter(@Param("keyWord") String keyword, @Param("category") String category,
                                                @Param("author") String author, @Param("activated") String activated,
                                                @Param("days") int days, Pageable pageable);

//    @Query("SELECT CONCAT(a.author.fullName, COALESCE(a.category.categoryName, 'asd') , COALESCE(a.subcategory.subcategoryName, 'asd') , a.title, a.text) " +
//            "FROM ArticleEntity a " +
//            "LEFT JOIN CategoryEntity c ON a.category.id = c.id " +
//            "LEFT JOIN SubcategoryEntity s ON a.subcategory.id = s.id " +
//            "LEFT JOIN UserEntity u ON a.author.id = u.id")

    @Query(value = "SELECT CONCAT(u.full_name, IF(a.activated, 'true', 'false'), COALESCE(c.category_name, '---'), COALESCE(s.subcategory_name, '---'), a.title, a.text) " +
            "FROM articles AS a " +
            "LEFT JOIN categories AS c ON a.category_id = c.id " +
            "LEFT JOIN subcategories AS s ON a.subcategory_id = s.id " +
            "LEFT JOIN users AS u ON a.author_id = u.id ", nativeQuery = true)
    List<String> test();
}
