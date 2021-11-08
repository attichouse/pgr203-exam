create table QUESTIONS
(
    question_id serial primary key,
    description varchar(100) not null,
    constraint fk_survey foreign key (survey_id) references SURVEYS(survey_id)
);