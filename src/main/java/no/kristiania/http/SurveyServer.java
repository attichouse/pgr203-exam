package no.kristiania.http;

import no.kristiania.survey.QuestionDao;
import no.kristiania.survey.SurveyDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SurveyServer {
    public static void main(String[] args) throws IOException {
        DataSource dataSource = createDataSource();
        SurveyDao surveyDao = new SurveyDao(dataSource);
        QuestionDao questionDao = new QuestionDao(dataSource);
        HttpServer httpServer = new HttpServer(1962);
        httpServer.addController("/api/newQuestion", new CreateQuestionController(questionDao));
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
