package cafeboard.post;

import java.time.LocalDateTime;

public record PostDetailResponse(
        long id,
        String title,
        String content,
        long writerId,
        String writerNickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
