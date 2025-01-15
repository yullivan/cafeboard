package cafeboard.member;

import cafeboard.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
}
