package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    // 주문 저장

    /**
     *
     * @param memberId
     * @param itemId
     * @param count
     * @return
     */
    @Transactional
    public Long saveOrder(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // new 생성자 함수 호출로 객체를 생성하면 다른 코드에서 무분별하게 set인스턴스 멤버 변수를 남용할 수 있다.
        // 생성자 함수를 따로 만들어 리팩토링하여 제약을 걸어주는 것이 유지보수와 리팩토링 측면에서 옳다.
        // 도메인에 @NoArgsConstructor(access = AccessLevel.PROTECTED)를 걸어주면 new 생성자 함수 호출을 방지할 수 있다.

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소 - entity의 변경이 일어나면 JPA가 update 처리를 해준다.
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        
        // 주문 취소 함수 호출
        order.cancelOrder();
    }

    // 검색
//    public List<Order> findOrder(OrderSearch orderSearch) {
//        return orderRepository.findList(orderSearch);
//    }
}
