package cafeboard.member;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입 API
    // POST /members
    // { "username": "", "password": "", "nickname": "" }
    @PostMapping("/members")
    public void create(@Valid @RequestBody CreateMemberRequest request) {
        memberService.create(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return memberService.login(request);
    }
}
