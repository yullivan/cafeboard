package cafeboard;

import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResponse create(CreateBoardRequest request) {
        Board board = boardRepository.save(new Board(request.title()));
        return new BoardResponse(board.getId(), board.getTitle());
    }
}
