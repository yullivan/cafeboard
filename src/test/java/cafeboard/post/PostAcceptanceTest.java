package cafeboard.post;

import cafeboard.AcceptanceTest;
import cafeboard.board.BoardResponse;
import cafeboard.board.CreateBoardRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class PostAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글을 생성한다.")
    @Test
    void createPost() {
        BoardResponse 게시판 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateBoardRequest("공지사항"))
                .when()
                .post("/boards")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(BoardResponse.class);

        PostDetailResponse 게시글 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreatePostRequest("글 제목", "글 내용", 게시판.id()))
                .when()
                .post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PostDetailResponse.class);

        assertThat(게시글.id()).isGreaterThan(0);
    }
}
