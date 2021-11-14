package no.kristiania.http;

import no.kristiania.controllers.*;
import no.kristiania.survey.AnswerDao;
import no.kristiania.survey.QuestionDao;
import no.kristiania.survey.SurveyDao;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Program {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);

        DataSource dataSource = createDataSource();
        SurveyDao surveyDao = new SurveyDao(dataSource);
        QuestionDao questionDao = new QuestionDao(dataSource);
        AnswerDao answerDao = new AnswerDao(dataSource);

        httpServer.addController("/", new RedirectController("index.html"));
        httpServer.addController("/api/newSurvey", new CreateSurveyController(surveyDao));
        httpServer.addController("/api/newQuestion", new CreateQuestionController(questionDao));
        httpServer.addController("/api/surveyOptions", new SurveyOptionsController(surveyDao));
        httpServer.addController("/api/newAnswer", new UserAnswersController(answerDao));
        httpServer.addController("/api/questionOptions", new QuestionOptionsController(questionDao));
        httpServer.addController("/api/listQuestions", new QuestionListController(questionDao));
        httpServer.addController("/api/listAnswers", new ListUserAnswersController(answerDao));
        httpServer.addController("/api/updateQuestion", new UpdateQuestionController(questionDao));

        logger.info("Started http://localhost:{}/index.html", httpServer.getPort());
    }


    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(properties.getProperty( "dataSource.url",
                "jdbc:postgresql://localhost:5432/postgres"
        ));
        dataSource.setUser(properties.getProperty("dataSource.user", "postgres"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        /*Flyway.configure().dataSource(dataSource).load().migrate();*/
        return dataSource;
    }
}
