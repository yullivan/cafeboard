package cafeboard.post;

import cafeboard.member.JwtProvider;
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

    public PostRestController(PostService postService, JwtProvider jwtProvider) {
        this.postService = postService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/posts")
    public PostDetailResponse create(
            @RequestBody CreatePostRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {

        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        if (token == null) {
            throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다");
        }

        // 유효한 JWT 토큰인지 검증
        if (!jwtProvider.isValidToken(token)) {
            throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다");
        }

        // 토큰 데이터 읽기 - 요청 보낸 사람이 누구인지 확인
        String username = jwtProvider.getSubject(token);

        return postService.create(request, username);
    }

    @GetMapping("/posts/{postId}")
    public PostDetailResponse findById(@PathVariable long postId) {
        return postService.findById(postId);
    }

    @DeleteMapping("/posts/{postId}")
    public void deleteById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @PathVariable long postId) {

        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        if (token == null) {
            throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다");
        }

        // 유효한 JWT 토큰인지 검증
        if (!jwtProvider.isValidToken(token)) {
            throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다");
        }

        // 토큰 데이터 읽기 - 요청 보낸 사람이 누구인지 확인
        String username = jwtProvider.getSubject(token);

        postService.deleteById(postId, username);
    }
}
