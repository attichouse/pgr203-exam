package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Answer;
import no.kristiania.survey.AnswerDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class ListUserAnswersController implements HttpController {

    private final AnswerDao answerDao;

    public ListUserAnswersController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = new HttpMessage(getBody(request));
        response.write(socket);
    }

    private String getBody(HttpMessage request) throws SQLException {
        String responseText = "";
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.parameterLine());
        Long qid = Long.parseLong(queryMap.get("questionid"));
        for (Answer answer : answerDao.listByQuestion(qid)) {
            responseText += answer.toString();
        }
        return responseText;
    }
}
