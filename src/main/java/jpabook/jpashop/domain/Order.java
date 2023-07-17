package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order는 DB상 예약어이다.
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK가 있는 곳에서 지정
    private Member member;  // 주문 회원

    // CascadeType.ALL가 명시된 객체들은 Order가 persist될 때 모두 persist된다.
    // Delivery, OrderItem 등의 엔티티가 다른 곳에서 참조되거나 중요한 경우에는 따로 repository를 만들어 persist 해 주도록 하자.

    // 주문과 상품은 N:M 관계
    // 따라서 중간에 OrderItem 테이블을 두었다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문 상품

    // 1:1 관계에서는 자주 접근하는 테이블에서 join을 걸어준다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 시간

    // EnumType.STRING을 항상 확인하자.
    // EnumType.ORDINAL의 경우 1, 2와 같이 숫자 값이 부여된다. (추후 문제 발생 위험)
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // 연관 관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem ...orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 비즈니스 로직

    /**
     * 주문 취소
     */
    public void cancelOrder() {
        if(this.delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("배송 완료된 상품은 취소가 불가능 합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for(OrderItem orderItem : this.getOrderItems()) {
            orderItem.cancel();
        }
    }

    // 조회 로직

    /**
     * 주문 별 총 가격
     * @return
     */
    public int getTotalPrice() {
        int totalPrice = 0;

        for(OrderItem orderItem : this.getOrderItems()) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }
}
