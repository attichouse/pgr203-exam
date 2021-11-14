package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Answer;
import no.kristiania.survey.AnswerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class UserAnswersController implements HttpController {

    private final AnswerDao answerDao;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public UserAnswersController(AnswerDao answerDao){
        this.answerDao = answerDao;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        response.write(socket);
    }

    private HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Answer answer = new Answer();
        answer.setAnswerText(queryMap.get("alternativ"));
        answer.setQuestionId(Long.parseLong(queryMap.get("questionId")));
        answerDao.save(answer);
        logger.info("Created new answer options " + answer.getAnswerText() + " in the database");

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/listQuestions.html");
        return redirect;
    }
}
