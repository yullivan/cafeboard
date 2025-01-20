package cafeboard.post;

import cafeboard.board.Board;
import cafeboard.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    private Board board;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member writer;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    protected Post() {
    }

    public Post(String title, String content, Board board, Member writer) {
        this.title = title;
        this.content = content;
        this.board = board;
        this.writer = writer;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Board getBoard() {
        return board;
    }

    public Member getWriter() {
        return writer;
    }
}
