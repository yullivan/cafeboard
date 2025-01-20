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

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));

        if (!member.authenticate(request.password())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        return new LoginResponse("token");
    }
}
