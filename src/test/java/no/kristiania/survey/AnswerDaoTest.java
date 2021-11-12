package no.kristiania.survey;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class AnswerDaoTest {

    private QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
    private AnswerDao answerDao = new AnswerDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedAnswers() throws SQLException {
        Question exampleQuestion = new Question();
        exampleQuestion.setQuestionDescription("What is your favourite colour?");
        exampleQuestion.setQuestionIdFk(1);
        questionDao.save(exampleQuestion);

        Answer exampleAnswer = new Answer();
        exampleAnswer.setQuestion_id(1);
        exampleAnswer.setUser_answer("Pink");
        answerDao.save(exampleAnswer);

        assertThat(answerDao.retrieve(exampleAnswer.getAnswer_id()))
                .usingRecursiveComparison()
                .isEqualTo(exampleAnswer);
    }
}
