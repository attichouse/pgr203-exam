package no.kristiania.http;

import no.kristiania.controllers.RedirectController;
import no.kristiania.survey.AnswerDao;
import no.kristiania.survey.QuestionDao;
import no.kristiania.survey.SurveyDao;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Program {
    public static void main(String[] args) throws IOException {
        DataSource dataSource = createDataSource();
        SurveyDao surveyDao = new SurveyDao(dataSource);
        QuestionDao questionDao = new QuestionDao(dataSource);
        AnswerDao answerDao = new AnswerDao(dataSource);
        HttpServer httpServer = new HttpServer(1962);
        httpServer.addController("/", new RedirectController("index.html"));
        httpServer.addController("/api/newSurvey", new CreateSurveyController(surveyDao));
        httpServer.addController("/api/newQuestion", new CreateQuestionController(questionDao));
        httpServer.addController("/api/surveyOptions", new SurveyOptionsController(surveyDao));
        httpServer.addController("/api/newAnswer", new UserAnswersController(answerDao));
        httpServer.addController("/api/questionOptions", new QuestionOptionsController(questionDao));

        /*httpServer.setCategories(List.of("Mat", "Drikke", "Frukt"));*/
        System.out.println("http://localhost:" + httpServer.getPort() + "/index.html");
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
