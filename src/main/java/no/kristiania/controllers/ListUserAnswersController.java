package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Answer;
import no.kristiania.survey.AnswerDao;



import java.sql.SQLException;
import java.util.Map;

public class ListUserAnswersController implements HttpController{
    private final AnswerDao answerDao;

    public ListUserAnswersController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    public HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.parameterLine());
        Long qid = Long.parseLong(queryMap.get("questionid"));
        for (Answer answer : answerDao.listSurvey(qid)) {
            responseText += answer;
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
}
}
