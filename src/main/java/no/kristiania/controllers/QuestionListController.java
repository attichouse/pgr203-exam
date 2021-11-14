package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class QuestionListController implements HttpController{
    private final QuestionDao questionDao;

    public QuestionListController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        response.write(socket);
    }

    private HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);

        String responseText = "";
        Long sid = Long.parseLong(queryMap.get("surveyid"));
        for (Question question : questionDao.listSurvey(sid)) {
            responseText += question.toListString();
        }

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/listQuestions.html");
        return  redirect;
    }
}

