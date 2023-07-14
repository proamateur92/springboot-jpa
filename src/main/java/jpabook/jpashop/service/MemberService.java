package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final만 가지고 생성자를 만들어준다.
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional // 작은 범위의 트랜잭션이 우선시 된다.
    public Long join(Member member) {
        validateDuplicateMember(member);

        // 동시에 회원가입하는 경우 두 요청 모두 유효성을 통과한다.
        // 따라서 sql에 unique key를 설정해두도록 하자.
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 전체 조회
     * @return
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 단일 회원 조회
     * @param memberId
     * @return
     */
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> result = memberRepository.findByName(member.getName());

        if(result.size() > 0) {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }
    }
}
