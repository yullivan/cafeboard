package cafeboard;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }


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
}
