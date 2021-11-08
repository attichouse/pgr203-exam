create table USER_ANSWER
(
    user_answer varchar(100) not null,
    fk_questions int references questions(question_id)
);