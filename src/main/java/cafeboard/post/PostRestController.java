package cafeboard.post;

import cafeboard.TokenUtils;
import cafeboard.TokenValidator;
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
    private final TokenValidator tokenValidator;

    public PostRestController(PostService postService, JwtProvider jwtProvider, TokenValidator tokenValidator) {
        this.postService = postService;
        this.jwtProvider = jwtProvider;
        this.tokenValidator = tokenValidator;
    }

    @PostMapping("/posts")
    public PostDetailResponse create(
            @RequestBody CreatePostRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {

        String token = TokenUtils.extractToken(bearerToken);
        String username = tokenValidator.validateAndGetUsername(token);

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

        String token = TokenUtils.extractToken(bearerToken);
        String username = tokenValidator.validateAndGetUsername(token);

        postService.deleteById(postId, username);
    }
}
