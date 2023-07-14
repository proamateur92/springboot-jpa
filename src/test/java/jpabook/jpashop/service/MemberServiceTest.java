package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    void 회원가입() {
        // 트랙잭션 내의 같은 pk값이면 같은 영속성 컨테이너에서 동일하게 처리된다.
        Member member = new Member();
        member.setName("manfuku");
        Long memberId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(memberId));
    }

    @Test
    void 중복회원체크() throws Exception{
        Member member1 = new Member();
        member1.setName("manfuku");

        Member member2 = new Member();
        member2.setName("manfuku");

        memberService.join(member1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        Assertions.assertEquals("이미 존재하는 이름입니다.", exception.getMessage());
    }
}