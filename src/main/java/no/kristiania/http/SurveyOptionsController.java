package no.kristiania.http;

import no.kristiania.survey.Survey;
import no.kristiania.survey.SurveyDao;
import java.sql.SQLException;

public class SurveyOptionsController implements HttpController {
    private final SurveyDao surveyDao;

    public SurveyOptionsController(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;

    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";

        int value = 1;
        for (Survey survey : surveyDao.listAll()) {
            responseText += "<option value=" + (value++) + ">" + survey + "</option>";

        }
            return new HttpMessage("HTTP/1.1 200 ok", responseText);
    }
}
