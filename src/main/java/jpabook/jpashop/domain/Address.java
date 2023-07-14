package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    // 변경이 불가능하도록 setter()를 없애고 생성자로 값을 할당하도록 한다.
    private String city;
    private String street;
    private String zipcode;

    // 암묵적으로 객체 생성이 불가능함을 알린다.
    protected Address() {}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
