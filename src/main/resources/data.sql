-- 테스트 계정
-- TODO: 테스트용이지만 비밀번호가 노출된 데이터 세팅. 개선하는 것이 좋을 지 고민해 보자.
insert into user_account (user_id, user_password, nickname, email, memo, created_at, created_by, modified_at, modified_by) values
    ('test', 'test1234', 'test', 'test@test.com', 'test.', now(), 'test', now(), 'test')
;

-- 123 게시글
insert into article (title, content, hashtag, created_at, created_by, modified_at, modified_by) values ('Java', 'Java is ...', '#java', '2020-01-01 00:00:00', 'test', '2020-01-01 00:00:00', 'test');
insert into article (title, content, hashtag, created_at, created_by, modified_at, modified_by) values ('Spring Boot', 'Spring Boot is ...', '#spring', '2020-01-01 00:00:00', 'test', '2020-01-01 00:00:00', 'test');
insert into article (title, content, hashtag, created_at, created_by, modified_at, modified_by) values ('AWS', 'Aws is ...', '#aws', '2020-01-01 00:00:00', 'test', '2020-01-01 00:00:00', 'test');

--- 123 댓글
insert into article_comment (content, article_id, created_at, created_by, modified_at, modified_by) values ('Great article!', 1, '2020-01-01 00:00:00', 'test', '2020-01-01 00:00:00', 'test');
insert into article_comment (content, article_id, created_at, created_by, modified_at, modified_by) values ('Great paper!', 1, '2020-01-01 00:00:00', 'test', '2020-01-01 00:00:00', 'test');
insert into article_comment (content, article_id, created_at, created_by, modified_at, modified_by) values ('Great book!', 1, '2020-01-01 00:00:00', 'test', '2020-01-01 00:00:00', 'test');
