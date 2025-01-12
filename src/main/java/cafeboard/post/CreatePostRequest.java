package cafeboard.post;

public record CreatePostRequest(
        String title,
        String content,
        Long boardId
) {
}
