package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order는 DB상 예약어이다.
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK가 있는 곳에서 지정
    private Member member;  // 주문 회원

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
}
