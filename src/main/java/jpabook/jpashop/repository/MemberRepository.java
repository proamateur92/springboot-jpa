package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class MemberRepository {
//    기술 설명
//    @Repository : 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
//    @PersistenceContext : 엔티티 메니저( EntityManager ) 주입
//    @PersistenceUnit : 엔티티 메니터 팩토리(EntityManagerFactory) 주입

//    기능 설명
//    save()
//    findOne()
//    findAll()
//    findByName()

    @PersistenceContext
    EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // jpql에서는 엔티티를 table로 대신해서 받는다.
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
