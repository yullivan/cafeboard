package cafeboard.board;

import cafeboard.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시판을 생성한다.")
    @Test
    void createBoard() {
        BoardResponse board = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest("공지사항"))
                .when()
                .post("/boards")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(BoardResponse.class);

        assertThat(board.id()).isGreaterThan(0);
    }

    @DisplayName("게시판 목록을 조회한다.")
    @Test
    void findBoards() {
        // given
        // 게시판 생성
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest("공지사항"))
                .when()
                .post("/boards")
                .then()
                .statusCode(HttpStatus.OK.value());

        // when
        // 게시판 목록 조회
        List<BoardResponse> boards = RestAssured
                .given().log().all()
                .when()
                .get("/boards")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", BoardResponse.class);

        // then
        assertThat(boards).hasSize(1);
    }

    @DisplayName("게시판을 수정한다.")
    @Test
    void updateBoard() {
        // given
        // 게시판 생성
        final String 변경_전_제목 = "공지사항";
        final String 변경_후_제목 = "공지";

        BoardResponse 생성된_게시판 = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest(변경_전_제목))
                .when()
                .post("/boards")
                .then()
                .extract()
                .as(BoardResponse.class);

        // when
        // 게시판 title 수정
        BoardResponse 수정된_게시판 = RestAssured
                .given().log().all()
                .pathParam("boardId", 생성된_게시판.id())
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest(변경_후_제목))
                .when()
                .put("/boards/{boardId}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(BoardResponse.class);

        // then
        // 게시판 목록 조회
        List<BoardResponse> boards = RestAssured
                .given().log().all()
                .when()
                .get("/boards")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", BoardResponse.class);

        // allMatch() 사용
        assertThat(boards).allMatch(board -> !board.title().equals(변경_전_제목));
        assertThat(boards).allMatch(board -> board.title().equals(변경_후_제목));

        // allSatisfy() 사용
        assertThat(boards).allSatisfy(board -> {
            assertThat(board.title()).isNotEqualTo(변경_전_제목);
            assertThat(board.title()).isEqualTo(변경_후_제목);
        });
    }

    @DisplayName("게시판을 삭제한다.")
    @Test
    void deleteBoard() {
        // given
        // 게시판 생성 1
        BoardResponse 생성된_게시판 = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest("공지사항"))
                .when()
                .post("/boards")
                .then()
                .extract()
                .as(BoardResponse.class);

        // 게시판 생성 2
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest("자유게시판"))
                .when()
                .post("/boards")
                .then()
                .extract()
                .as(BoardResponse.class);

        // when
        // 게시판 삭제
        RestAssured
                .given().log().all()
                .pathParam("boardId", 생성된_게시판.id())
                .when()
                .delete("/boards/{boardId}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // then
        // 게시판 목록 조회
        List<BoardResponse> boards = RestAssured
                .given().log().all()
                .when()
                .get("/boards")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", BoardResponse.class);

        assertThat(boards).allMatch(board -> !board.title().equals(생성된_게시판.title()));
    }
}
