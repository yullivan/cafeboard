package cafeboard;

public record CreatePostRequest(
        String title,
        String content,
        Long boardId
) {
}
