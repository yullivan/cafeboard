package cafeboard.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // login ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    protected Member() {
    }

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
//        if (nickname == null) {
//            this.nickname = username;
//        } else {
//            this.nickname = nickname;
//        }
        this.nickname = nickname == null ? username : nickname;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}
