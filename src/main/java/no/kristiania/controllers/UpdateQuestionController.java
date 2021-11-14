package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;

public class UpdateQuestionController implements HttpController {

    private final QuestionDao questionDao;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);


    public UpdateQuestionController(QuestionDao questionDao){
            this.questionDao = questionDao;
    }


    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage response = handle(request);
        response.write(socket);
    }


    private HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Question question = new Question();
        question.setQuestionDescription(queryMap.get("question"));
        question.setQuestionAlternatives(queryMap.get("questionAlternatives"));
        question.setQuestionIdFk(Long.parseLong(queryMap.get("survey")));
        question.setQuestionId(Long.parseLong(queryMap.get("questionid")));
        questionDao.update(question);
        logger.info("Updated" + question.getQuestionDescription() + " in the database");

        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "/changeQuestion.html");
        return redirect;
    }
}

