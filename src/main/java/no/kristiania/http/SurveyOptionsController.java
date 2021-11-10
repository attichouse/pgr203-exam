package no.kristiania.http;

import no.kristiania.survey.Survey;
import no.kristiania.survey.SurveyDao;
import java.sql.SQLException;
import java.util.Map;

public class SurveyOptionsController implements HttpController {
    private final SurveyDao surveyDao;

    public SurveyOptionsController(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;

    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Survey survey = new Survey();
        survey.setSurveyName(queryMap.get("surveyName"));
        surveyDao.save(survey);

        return new HttpMessage("HTTP/1.1 200 ok", "It is done");
    }
}
