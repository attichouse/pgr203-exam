create table QUESTIONS
(
    question_id serial primary key,
    description varchar(100) not null,
    fk_survey int references surveys(survey_id)
);