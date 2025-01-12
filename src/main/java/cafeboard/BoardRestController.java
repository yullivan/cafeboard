package cafeboard;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/boards")
    public BoardResponse create(@RequestBody CreateBoardRequest request) {
        return boardService.create(request);
    }

    @GetMapping("/boards")
    public List<BoardResponse> findAll() {
        return boardService.findAll();
    }

    @PutMapping("/boards/{boardId}")
    public BoardResponse update(@PathVariable long boardId, @RequestBody CreateBoardRequest request) {
        return boardService.update(boardId, request);
    }

    @DeleteMapping("/boards/{boardId}")
    public void deleteById(@PathVariable long boardId) {
        boardService.deleteById(boardId);
    }
}
