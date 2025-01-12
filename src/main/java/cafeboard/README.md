# 카페 게시판 API
## 게시판 생성
POST /boards

### Request Body 파라미터
- title (필수 · string): 게시판 이름

#### 요청 예시
POST http://localhost:8080/boards
```json
{
  "title": "공지사항"
}
```

#### 응답 예시
```json
{
  "id": 1,
  "title": "공지사항"
}
```

