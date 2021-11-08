package no.kristiania.survey;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class SurveyDaoTest {

    private SurveyDao dao = new SurveyDao(TestData.testDataSource());

    @Test
    void shouldListSurveys() throws SQLException {
        String question1 = "question-" + UUID.randomUUID();
        String question2 = "question-" + UUID.randomUUID();

        dao.save(question1);
        dao.save(question2);

        assertThat(dao.listAll())
                .contains(question1, question2);
    }
}
