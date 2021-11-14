package no.kristiania.survey;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDaoTest {

    private SurveyDao surveyDao = new SurveyDao(TestData.testDataSource());
    private QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
    private AnswerDao answerDao = new AnswerDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedAnswers() throws SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);

        Question question = exampleQuestion();
        questionDao.save(question);

        Answer answer = exampleAnswer();
        answerDao.save(answer);

        assertThat(answerDao.retrieve(answer.getAnswerId()))
                .usingRecursiveComparison()
                .isEqualTo(answer);
    }

    //Example objects
    private Answer exampleAnswer() {
        Answer answer = new Answer();
        answer.setAnswerId(1);
        answer.setAnswerText("Nei");
        answer.setQuestionId(1);
        return answer;
    }

    private Question exampleQuestion() {
        Question question = new Question();
        question.setQuestionDescription("Is the sky blue?");
        question.setQuestionAlternatives("Nei; Ja");
        question.setQuestionIdFk(1);
        return question;
    }

    private Survey exampleSurvey() {
        Survey survey = new Survey();
        survey.setSurveyName("Er du klar?");
        return survey;
    }
}
