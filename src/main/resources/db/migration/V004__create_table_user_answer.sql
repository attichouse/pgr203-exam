create table USER_ANSWER
(
    user_answer varchar(100) not null,
    question_id int not null,
    constraint fk_questions foreign key (question_id) references questions(question_id)
);