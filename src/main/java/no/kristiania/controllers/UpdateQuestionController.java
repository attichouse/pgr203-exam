package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class UpdateQuestionController implements HttpController {

        private final QuestionDao questionDao;

        public UpdateQuestionController(QuestionDao questionDao){
            this.questionDao = questionDao;
        }

        @Override
        public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
            HttpMessage response = handle(request);
            response.write(socket);
        }

        private HttpMessage handle(HttpMessage request) throws SQLException {
            HttpMessage requestParameter = new HttpMessage(request.getContentLength());

            //Get data
            String questionDescription = requestParameter.getParameter("question");
            String questionAlternatives = requestParameter.getParameter("questionAlternatives");
            long questionId = Integer.parseInt(requestParameter.getParameter("questionid"));
            long questionIdFk = Integer.parseInt(requestParameter.getParameter("survey"));

            //So we can read æøå
            String decodeQuestionDescription = URLDecoder.decode(questionDescription, StandardCharsets.UTF_8);
            String decodeQuestionAlternatives = URLDecoder.decode(questionAlternatives, StandardCharsets.UTF_8);

            //Insert data
            Question question = questionDao.retrieve(questionId);
            question.setQuestionDescription(decodeQuestionDescription);
            question.setQuestionAlternatives(decodeQuestionAlternatives);
            question.setQuestionId(questionId);
            question.setQuestionIdFk(questionIdFk);

            questionDao.update(question);

            HttpMessage redirect = new HttpMessage();
            redirect.setStartLine("HTTP/1.1 302 Redirect");
            redirect.getHeader().put("Location", "/changeQuestion.html");
            return redirect;
        }
    }

