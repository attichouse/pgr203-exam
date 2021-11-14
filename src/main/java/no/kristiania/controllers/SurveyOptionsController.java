package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Survey;
import no.kristiania.survey.SurveyDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class SurveyOptionsController implements HttpController{

    private final SurveyDao surveyDao;

    public SurveyOptionsController(SurveyDao surveyDao){
        this.surveyDao = surveyDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = new HttpMessage(getBody());
        response.write(socket);
    }

    private String getBody() throws SQLException {
        String responseText = "";
        int value = 1;
        for (Survey survey : surveyDao.listAll()) {
            responseText += "<option value=" + survey.getSurveyId() + ">" + survey.getSurveyName() + "</option>";
        }

        return responseText;
    }


}
