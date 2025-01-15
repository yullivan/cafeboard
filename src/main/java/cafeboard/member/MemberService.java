package cafeboard.member;

import cafeboard.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 생성
    public void create(CreateMemberRequest request) {
        memberRepository.save(new Member(
                request.username(),
                SecurityUtils.sha256Encrypt(request.password()),
                request.nickname()));
    }
}
