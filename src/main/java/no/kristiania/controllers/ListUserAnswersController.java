package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Answer;
import no.kristiania.survey.AnswerDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class ListUserAnswersController implements HttpController{
    private final AnswerDao answerDao;

    public ListUserAnswersController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        response.write(socket);
    }

    private HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.parameterLine());
        Long qid = Long.parseLong(queryMap.get("questionid"));
        for (Answer answer : answerDao.listSurvey(qid)) {
            responseText += answer;
        }

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/listQuestions.html");
        return  redirect;
    }
}
