package cafeboard;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<BoardResponse> findAll() {
        return boardRepository.findAll()
                .stream()
                .map(board -> new BoardResponse(board.getId(), board.getTitle()))
                .toList();
    }
}
