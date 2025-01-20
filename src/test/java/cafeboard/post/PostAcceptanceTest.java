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

    final String 글제목 = "제목1";
    final String 글내용 = "내용1";
    final String 로그인아이디 = "doraemon";
    final String 비밀번호 = "dora!23";
    final String 닉네임 = "도라에몽";

    @DisplayName("게시글을 생성한다.")
    @Test
    void createPost() {
        // given
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

    @DisplayName("게시글 상세 정보를 조회한다.")
    @Test
    void findPost() {
        // given
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

        // 게시글 생성
        PostDetailResponse 생성된게시글 = RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.accessToken())
                .contentType(ContentType.JSON)
                .body(new CreatePostRequest(글제목, 글내용, 게시판.id()))
                .when()
                .post("/posts")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PostDetailResponse.class);

        // when
        // 게시글 상세 조회
        PostDetailResponse 조회한게시글 = RestAssured
                .given().log().all()
                .pathParam("postId", 생성된게시글.id())
                .when()
                .get("/posts/{postId}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PostDetailResponse.class);

        // then
        assertThat(조회한게시글.id()).isEqualTo(생성된게시글.id());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deletePost() {
        // given
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

        // 게시글 생성
        PostDetailResponse 생성된게시글 = RestAssured
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

        // when
        // 게시글 삭제
        RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.accessToken())
                .pathParam("postId", 생성된게시글.id())
                .when()
                .delete("/posts/{postId}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // then
        // 게시글 조회 시 찾을 수 없음
        RestAssured
                .given().log().all()
                .pathParam("postId", 생성된게시글.id())
                .when()
                .get("/posts/{postId}")
                .then().log().all()
                .statusCode(500);
    }
}
