package cafeboard.member;

import cafeboard.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void createMember() {

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateMemberRequest("doraemon1", "doradora123", null))
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void login() {
        // given
        final String username = "doraemon";
        final String password = "dora!23";

        // 회원 가입
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateMemberRequest(username, password, "도라에몽"))
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(200);

        // when
        // 로그인
        LoginResponse loginResponse = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(username, password))
                .when()
                .post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        // then
        assertThat(loginResponse.accessToken()).isEqualTo("token");
    }
}
