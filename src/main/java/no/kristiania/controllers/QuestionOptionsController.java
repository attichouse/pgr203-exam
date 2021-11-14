package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class QuestionOptionsController implements HttpController{

    private final QuestionDao questionDao;

    public QuestionOptionsController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }


    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = new HttpMessage(getBody(request));
        response.write(socket);
    }

    private String getBody(HttpMessage request) throws SQLException {
        String responseText = "";
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.parameterLine());
        Long sid = Long.parseLong(queryMap.get("surveyid"));
        for (Question question : questionDao.listSurvey(sid)) {
            responseText += question;
        }
        return responseText;
    }
}
