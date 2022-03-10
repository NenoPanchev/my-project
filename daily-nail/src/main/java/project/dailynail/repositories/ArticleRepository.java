package project.dailynail.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.dailynail.models.entities.ArticleEntity;
import project.dailynail.models.entities.enums.CategoryNameEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            "AND DATEDIFF(NOW(), a.created) <= :days " +
            "ORDER BY a.created DESC ", nativeQuery = true)
    Page<String> findAllArticleIdBySearchFilter(@Param("keyWord") String keyword, @Param("category") String category,
                                                @Param("author") String author, @Param("activated") String activated,
                                                @Param("days") int days, Pageable pageable);

    Page<ArticleEntity> findAllByOrderByCreatedDesc(Pageable pageable);

    @Query(value = "SELECT a.id FROM articles AS a " +
            "LEFT JOIN categories AS c ON a.category_id = c.id " +
            "WHERE c.category_name LIKE %:categoryNameEnum% " +
            "AND a.posted <= :now " +
            "ORDER BY a.posted DESC " +
            "LIMIT 1", nativeQuery = true)
    String findFirstByCategoryNameOrderByPostedDesc(@Param("categoryNameEnum") CategoryNameEnum categoryNameEnum, @Param("now")LocalDateTime now);

    @Query(value = "SELECT a.id FROM articles AS a " +
            "LEFT JOIN categories AS c ON a.category_id = c.id " +
            "WHERE c.category_name LIKE %:categoryNameEnum% " +
            "AND a.posted <= :now " +
            "ORDER BY a.posted DESC " +
            "LIMIT 1, 4", nativeQuery = true)
    List<String> findFourByCategoryNameOrderByPostedDesc(@Param("categoryNameEnum") CategoryNameEnum categoryNameEnum, @Param("now") LocalDateTime now);

    @Query(value = "SELECT a.id FROM articles AS a " +
            "WHERE a.posted <= :now " +
            "ORDER BY a.posted DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<String> findLatestArticles(@Param("limit") Integer limit, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE ArticleEntity a " +
            "SET a.top = :condition " +
            "WHERE a.id = :id")
    void updateTop(@Param("id") String id, @Param("condition") boolean condition);

    @Query(value = "SELECT a.id FROM ArticleEntity a " +
            "WHERE a.top = true " +
            "AND a.posted <= :now " +
            "ORDER BY a.posted DESC ")
    List<String> findAllByTopIsTrue(@Param("now") LocalDateTime now);

    @Query(value = "SELECT a.id FROM articles a " +
            "ORDER BY a.created DESC " +
            "LIMIT 1", nativeQuery = true)
    String getIdOfLastCreatedArticle();
}
