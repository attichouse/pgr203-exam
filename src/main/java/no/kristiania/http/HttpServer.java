package no.kristiania.http;

import no.kristiania.survey.SurveyDao;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class HttpServer {

    private ServerSocket serverSocket;
    private SurveyDao surveyDao;
    //ENDRE CATEGORIES OG PRODUCT TIL DET SOM PASSER!!!!!
    private List<String> survey = new ArrayList<>();
    private List<Survey> surveyList = new ArrayList<>();
    private Map<String, HttpController> controllers = new HashMap<>();

    PGSimpleDataSource dataSource = new PGSimpleDataSource();


    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);
        new Thread(this::handleClients).start();
    }

    public void createDataSource() {
        dataSource.setURL("jdbc:postgresql://localhost:5433/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("v;6G.}h:s8uVyf*+");
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
        String responseText = "";


        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        if (questionPos != -1){
            fileTarget = requestTarget.substring(0, questionPos);
        } else {
            fileTarget = requestTarget;
        }


        if(controllers.containsKey(fileTarget)){
            HttpMessage response = controllers.get(fileTarget).handle(httpMessage);
                    response.write(clientSocket);
                    return;
        }

    if (fileTarget.equals("/api/newQuestion")) {
            Map<String, String> queryMap = parseRequestParameters(httpMessage.messageBody);
            Survey product = new Survey();
            product.setSurveyName(queryMap.get("productName"));
            surveyList.add(product);
            writeOkResponse(clientSocket, "it is done", "text/html");
        } else if (fileTarget.equals("/api/categoryOptions")){


            int value = 1;
        for (String survey : surveyDao.listAll()) {
                responseText += "<option value=" + (value++) + ">" + survey + "</option>";
            }

            writeOkResponse(clientSocket, responseText, "text/html");
        } else if (fileTarget.equals("/api/products")){
            for (int i = 0; i < surveyList.size(); i++) {
                responseText += "<p>" + surveyList.get(i).geSurveyName() + "</p>";
            }

            writeOkResponse(clientSocket, responseText, "text/html");
        } else {
            InputStream fileResource = getClass().getResourceAsStream(fileTarget);
            if(fileResource != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                fileResource.transferTo(buffer);
                responseText = buffer.toString();

                String contentType = "text/plain";
                if (requestTarget.endsWith(".html")){
                    contentType = "text/html";
                }
                writeOkResponse(clientSocket, responseText, contentType);
                return;
            }

            responseText = "File not found: " + requestTarget;

            String response = "HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: " + responseText.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseText;
            clientSocket.getOutputStream().write(response.getBytes());
        }
    }


    private Map<String, String> parseRequestParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalPos);
            String parameterValue = queryParameter.substring(equalPos + 1);
            queryMap.put(parameterName, URLDecoder.decode(parameterValue, StandardCharsets.UTF_8));
        }
        return queryMap;
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

    public List<Survey> getProducts() {
        return surveyList;
    }

    public void addController(String path, HttpController controller) {
        this.controllers.put(path, controller);
    }

}
