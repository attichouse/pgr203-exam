package no.kristiania.survey;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class SurveyDaoTest {

    private SurveyDao dao = new SurveyDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedSurveys() throws SQLException {
        Survey survey = exampleSurvey();
        dao.save(survey);
        assertThat(dao.retrieve(survey.getSurveyId()))
                .usingRecursiveComparison()
                .isEqualTo(survey);
    }


    @Test
    void shouldListAllSurveys() throws SQLException {
        Survey survey = exampleSurvey();
        dao.save(survey);
        Survey anotherSurvey = exampleSurvey();
        dao.save(anotherSurvey);

        assertThat(dao.listAll())
                .extracting(Survey::getSurveyId)
                .contains(survey.getSurveyId(), anotherSurvey.getSurveyId());
    }


    private Survey exampleSurvey() {
        Survey survey = new Survey();
        survey.setSurveyName("Are you smarter than a 5th grader?");
        return survey;
    }
}
