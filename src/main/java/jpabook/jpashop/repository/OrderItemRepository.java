package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final EntityManager em;

    public void save(OrderItem orderItem) {
        em.persist(orderItem);
    }

    public OrderItem findOne(Long id) {
        return em.find(OrderItem.class, id);
    }

//    public List<OrderItem> findList(OrderSearch orderSearch) {}
}
