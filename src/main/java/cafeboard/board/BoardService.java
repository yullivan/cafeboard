package cafeboard.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public BoardResponse update(long boardId, CreateBoardRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시판을 찾을 수 없습니다 id: " + boardId));
        board.changeTitle(request.title());
        return new BoardResponse(board.getId(), board.getTitle());
    }

    public void deleteById(long boardId) {
        boardRepository.deleteById(boardId);
    }
}
