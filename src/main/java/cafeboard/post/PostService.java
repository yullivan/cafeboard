package cafeboard.post;

import cafeboard.board.Board;
import cafeboard.board.BoardRepository;
import cafeboard.member.Member;
import cafeboard.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, BoardRepository boardRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    public PostDetailResponse create(CreatePostRequest request, String username) {
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 게시판 id: " + request.boardId()));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. Username:" + username));
        Post post = postRepository.save(new Post(request.title(), request.content(), board, member));
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter().getId(),
                post.getWriter().getNickname(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
