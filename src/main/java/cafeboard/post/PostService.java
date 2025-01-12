package cafeboard.post;

import cafeboard.board.Board;
import cafeboard.board.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    public PostService(PostRepository postRepository, BoardRepository boardRepository) {
        this.postRepository = postRepository;
        this.boardRepository = boardRepository;
    }

    public PostDetailResponse create(CreatePostRequest request) {
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 게시판 id: " + request.boardId()));
        Post post = postRepository.save(new Post(request.title(), request.content(), board));
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
