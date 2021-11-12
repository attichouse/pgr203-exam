package no.kristiania.survey;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

public class AnswerDaoTest {

    private SurveyDao surveyDao = new SurveyDao(TestData.testDataSource());
    private QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
    private AnswerDao answerDao = new AnswerDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedAnswers() throws SQLException {
        Survey exampleSurvey = new Survey();
        exampleSurvey.setSurveyName("Wut");
        surveyDao.save(exampleSurvey);

        Question exampleQuestion = new Question();
        exampleQuestion.setQuestionDescription("What is your favourite colour?");
        exampleQuestion.setQuestionIdFk(1);
        questionDao.save(exampleQuestion);

        Answer exampleAnswer = new Answer();
        exampleAnswer.setQuestion_id(1);
        exampleAnswer.setAnswer_text("Pink");
        answerDao.save(exampleAnswer);

        assertThat(answerDao.retrieve(exampleAnswer.getAnswer_id()))
                .usingRecursiveComparison()
                .isEqualTo(exampleAnswer);
    }
}
