package cafeboard.post;

import cafeboard.LoginMemberResolver;
import cafeboard.member.JwtProvider;
import cafeboard.member.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostRestController {

    private final PostService postService;
    private final JwtProvider jwtProvider;
    private final LoginMemberResolver loginMemberResolver;

    public PostRestController(PostService postService, JwtProvider jwtProvider, LoginMemberResolver loginMemberResolver) {
        this.postService = postService;
        this.jwtProvider = jwtProvider;
        this.loginMemberResolver = loginMemberResolver;
    }

    @PostMapping("/posts")
    public PostDetailResponse create(
            @RequestBody CreatePostRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {

        Member member = loginMemberResolver.resolveMemberFromToken(bearerToken);

        return postService.create(request, member);
    }

    @GetMapping("/posts/{postId}")
    public PostDetailResponse findById(@PathVariable long postId) {
        return postService.findById(postId);
    }

    @DeleteMapping("/posts/{postId}")
    public void deleteById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @PathVariable long postId) {

        Member member = loginMemberResolver.resolveMemberFromToken(bearerToken);

        postService.deleteById(postId, member);
    }
}
