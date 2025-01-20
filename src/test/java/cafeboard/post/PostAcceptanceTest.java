package cafeboard.post;

import cafeboard.AcceptanceTest;
import cafeboard.board.BoardResponse;
import cafeboard.board.CreateBoardRequest;
import cafeboard.member.CreateMemberRequest;
import cafeboard.member.LoginRequest;
import cafeboard.member.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class PostAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시글을 생성한다.")
    @Test
    void createPost() {
        // given
        final String 로그인아이디 = "doraemon";
        final String 비밀번호 = "dora!23";
        final String 닉네임 = "도라에몽";

        // 게시판 생성
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

        // 회원 가입
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateMemberRequest(로그인아이디, 비밀번호, 닉네임))
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(200);

        // 로그인
        LoginResponse loginResponse = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(로그인아이디, 비밀번호))
                .when()
                .post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        // when
        // 게시글 생성
        PostDetailResponse 게시글 = RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.accessToken())
                .contentType(ContentType.JSON)
                .body(new CreatePostRequest("글 제목", "글 내용", 게시판.id()))
                .when()
                .post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PostDetailResponse.class);

        // then
        assertThat(게시글.id()).isGreaterThan(0);
        assertThat(게시글.writerNickname()).isEqualTo(닉네임);
    }
}
