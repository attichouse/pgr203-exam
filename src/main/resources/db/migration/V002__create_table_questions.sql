create table QUESTIONS
(
    question_id serial primary key,
    description varchar(100) not null,
    survey_id int not null,
    constraint fk_survey foreign key (survey_id) references surveys(survey_id)
);