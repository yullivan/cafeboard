package cafeboard;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardRestController {

    @PostMapping("/boards")
    public BoardResponse create(@RequestBody CreateBoardRequest request) {
        return new BoardResponse(0L, "");
    }
}
