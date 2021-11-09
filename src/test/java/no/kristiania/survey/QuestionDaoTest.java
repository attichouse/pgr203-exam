package no.kristiania.survey;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    private SurveyDao surveyDao = new SurveyDao(TestData.testDataSource());
    private QuestionDao dao = new QuestionDao(TestData.testDataSource());


    @Test
    void shouldRetrieveSavedQuestions() throws SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);
        Survey survey2 = exampleSurvey();
        surveyDao.save(survey2);

        Question question = exampleQuestion();
        dao.save(question);


        assertThat(dao.retrieve(question.getQuestionId()))
                .usingRecursiveComparison()
                .isEqualTo(question);
    }


    private Question exampleQuestion() {
        Question question = new Question();
        question.setQuestionDescription(TestData.pickOne("Who won the football championship in 2022?", "What kind of animal lives in the forest?"));
        question.setQuestionIdFk(TestData.pickOneInt(1,2));
        return question;
    }


    private Survey exampleSurvey() {
        Survey survey = new Survey();
        survey.setSurveyName(TestData.pickOne("Fotball", "Er du smartere enn en 5. klassing?"));
        return survey;
    }
}
