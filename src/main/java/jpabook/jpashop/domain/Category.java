package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    // 여러 하위 카테고리가 하나의 상위 카테고리를 갖는다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 하나의 상위 카테고리가 여러 하위 카테고리를 갖는다.
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 연관 관계 메서드
    public void addChild(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

    public void addCategory(CategoryItem categoryItem) {
        categoryItems.add(categoryItem);
        categoryItem.setCategory(this);
    }
}
