package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // id 존재 여부에 따른 DB 작업 분기 처리
        if(item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public List<Item> findAll() {
        // 여러 건에 대해서는 createQuery 메서드 실행
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
}
