package no.kristiania.http;

import no.kristiania.survey.Question;
import no.kristiania.survey.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class UpdateQuestionController implements HttpController{
        private final QuestionDao questionDao;

        public UpdateQuestionController(QuestionDao questionDao){
            this.questionDao = questionDao;
        }

        @Override
        public HttpMessage handle(HttpMessage request) throws SQLException {
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
            Question question = new Question();
            question.setQuestionDescription(queryMap.get("question"));
            question.setQuestionAlternatives(queryMap.get("questionAlternatives"));
            question.setQuestionIdFk(Long.parseLong(queryMap.get("survey")));
            question.setQuestionId(Long.parseLong(queryMap.get("questionid")));
            questionDao.update(question);

            return new HttpMessage("HTTP/1.1 200 ok", "It is done");
        }
    }

