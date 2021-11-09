package no.kristiania.http;

import no.kristiania.survey.SurveyDao;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class HttpServer {

    private ServerSocket serverSocket;
    private SurveyDao surveyDao;
    //ENDRE CATEGORIES OG PRODUCT TIL DET SOM PASSER!!!!!
    private List<String> survey = new ArrayList<>();
    private Map<String, HttpController> controllers = new HashMap<>();

    PGSimpleDataSource dataSource = new PGSimpleDataSource();


    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);
        new Thread(this::handleClients).start();
    }

    public void createDataSource() {
        dataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("6RLBqvkKpFXptjwnKm");
        surveyDao = new SurveyDao(dataSource);
    }


    //MÅ HA BEDRE FEILHÅNDTERING HER
    private void handleClients() {
        try {
            while (true) {
                handleClient();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleClient() throws IOException, SQLException {
        Socket clientSocket = serverSocket.accept();

        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = httpMessage.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        String query = null;
        if (questionPos != -1){
            fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos + 1);
        } else {
            fileTarget = requestTarget;
        }

        if(controllers.containsKey(fileTarget)){
            HttpMessage response = controllers.get(fileTarget).handle(httpMessage);
            response.write(clientSocket);
            return;
        }

        InputStream fileResource = getClass().getResourceAsStream(fileTarget);
        if (fileResource != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileResource.transferTo(buffer);
            String responseText = buffer.toString();

            String contentType = "text/plain";
            if (requestTarget.endsWith(".html")) {
                contentType = "text/html";
            } else if (requestTarget.endsWith("css")) {
                contentType = "text/css";
            }
            writeOkResponse(clientSocket, responseText, contentType);
            return;
        }

        String responseText = "File not found: " + requestTarget;

        String response = "HTTP/1.1 404 Not found\r\n" +
                "Content-Length: " + responseText.length() + "\r\n" +
                "Connection: close \r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }


    private void writeOkResponse(Socket clientSocket, String responseText, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                //getBytes i stedet for length
                "Content-Length: " + responseText.length() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }


    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(1962);
        httpServer.createDataSource();
        httpServer.setCategories(List.of("Mat", "Drikke", "Frukt"));
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setCategories(List<String> categories) {
        this.survey = categories;
    }


    public void addController(String path, HttpController controller) {
        this.controllers.put(path, controller);
    }

}
