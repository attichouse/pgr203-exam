package no.kristiania.http;

import no.kristiania.survey.Survey;
import no.kristiania.survey.SurveyDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class CreateSurveyController implements HttpController {
    private final SurveyDao surveyDao;

    public CreateSurveyController(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        response.write(socket);
    }

    private HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Survey survey = new Survey();
        survey.setSurveyName(queryMap.get("surveyName"));
        surveyDao.save(survey);

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/createSurvey.html");
        return redirect;
    }
}
