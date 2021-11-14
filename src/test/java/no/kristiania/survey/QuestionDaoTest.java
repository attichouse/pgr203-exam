package no.kristiania.survey;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    private SurveyDao surveyDao = new SurveyDao(TestData.testDataSource());
    private QuestionDao questionDao = new QuestionDao(TestData.testDataSource());


    @Test
    void shouldRetrieveSavedQuestion() throws SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);

        Question question = exampleQuestion();
        questionDao.save(question);


        assertThat(questionDao.retrieve(question.getQuestionId()))
                .usingRecursiveComparison()
                .isEqualTo(question);
    }


    @Test
    void shouldUpdateQuestions() throws SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);

        Question question = exampleQuestion();
        questionDao.save(question);
        Question question2 = exampleQuestion2();
        questionDao.save(question2);

        questionDao.update2(question2, question.getQuestionId());
        assertThat(questionDao.retrieve(question2.getQuestionId()))
                .extracting(Question::getQuestionDescription)
                .isEqualTo(question2.getQuestionDescription());
    }


    @Test
    void shouldListAllSavedQuestions() throws SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);

        Question question = exampleQuestion();
        Question question2 = exampleQuestion2();
        questionDao.save(question);
        questionDao.save(question2);

        assertThat(questionDao.listAll())
                .extracting(Question::getQuestionId)
                .contains(question.getQuestionId(), question2.getQuestionId());
    }


    @Test
    void shouldRetrieveQuestionsOnSurvey() throws SQLException {
        Survey survey = exampleSurvey();
        surveyDao.save(survey);

        Question question = exampleQuestion();
        Question question2 = exampleQuestion2();
        questionDao.save(question);
        questionDao.save(question2);
    }

    //Example objects
    private Question exampleQuestion() {
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionIdFk(1);
        question.setQuestionDescription("What is 2+2?");
        question.setQuestionAlternatives("7");
        return question;
    }

    private Question exampleQuestion2() {
        Question question = new Question();
        question.setQuestionId(2);
        question.setQuestionIdFk(1);
        question.setQuestionDescription("What is the capital of Norway?");
        question.setQuestionAlternatives("Oslo");
        return question;
    }

    private Survey exampleSurvey() {
        Survey survey = new Survey();
        survey.setSurveyName("Are you smarter than a 5th grader?");
        return survey;
    }
}
