package cafeboard.post;

import cafeboard.board.Board;
import cafeboard.board.BoardRepository;
import cafeboard.member.Member;
import cafeboard.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

    public PostDetailResponse create(CreatePostRequest request, Member member) {
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 게시판 id: " + request.boardId()));
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

    public PostDetailResponse findById(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + postId));

        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter().getId(),
                post.getWriter().getNickname(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public void deleteById(long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + postId));

        if (!post.getWriter().getUsername().equals(member.getUsername())) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }

        postRepository.deleteById(postId);
    }
}
