package cafeboard;

import java.time.LocalDateTime;

public record PostDetailResponse(
        long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
