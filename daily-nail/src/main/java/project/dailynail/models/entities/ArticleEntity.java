package project.dailynail.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "articles")
public class ArticleEntity extends BaseEntity {
    private String title;
    private UserEntity author;
    private String url;
    private String imageUrl;
    private String text;
    private LocalDateTime created;
    private LocalDateTime posted;
    private boolean activated;
    private CategoryEntity category;
    private SubcategoryEntity subcategory;
    private boolean disabledComments;
    private Integer seen;
    private Set<CommentEntity> comments;
    private boolean top;

    public ArticleEntity() {
        this.activated = false;
    }

    @Column
    public String getTitle() {
        return title;
    }

    public ArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    @ManyToOne
    public UserEntity getAuthor() {
        return author;
    }

    public ArticleEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    @Column
    public String getImageUrl() {
        return imageUrl;
    }

    public ArticleEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Column(columnDefinition = "TEXT")
    public String getText() {
        return text;
    }

    public ArticleEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Column
    public LocalDateTime getCreated() {
        return created;
    }

    public ArticleEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    @Column
    public LocalDateTime getPosted() {
        return posted;
    }

    public ArticleEntity setPosted(LocalDateTime posted) {
        this.posted = posted;
        return this;
    }

    @Column
    public boolean isActivated() {
        return activated;
    }

    public ArticleEntity setActivated(boolean activated) {
        this.activated = activated;
        return this;
    }

    @ManyToOne
    public CategoryEntity getCategory() {
        return category;
    }

    public ArticleEntity setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }

    @ManyToOne
    public SubcategoryEntity getSubcategory() {
        return subcategory;
    }

    public ArticleEntity setSubcategory(SubcategoryEntity subcategory) {
        this.subcategory = subcategory;
        return this;
    }

    @Column
    public String getUrl() {
        return url;
    }

    public ArticleEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    @Column
    public boolean isDisabledComments() {
        return disabledComments;
    }

    public ArticleEntity setDisabledComments(boolean disabledComments) {
        this.disabledComments = disabledComments;
        return this;
    }

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    public Set<CommentEntity> getComments() {
        return comments;
    }

    public ArticleEntity setComments(Set<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    @Column
    public Integer getSeen() {
        return seen;
    }

    public ArticleEntity setSeen(Integer seen) {
        this.seen = seen;
        return this;
    }

    @Column
    public boolean isTop() {
        return top;
    }

    public ArticleEntity setTop(boolean top) {
        this.top = top;
        return this;
    }
}
