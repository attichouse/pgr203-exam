package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HttpServer {

    private final ServerSocket serverSocket;
    private Path rootDirectory;
    //ENDRE CATEGORIES OG PRODUCT TIL DET SOM PASSER!!!!!
    private List<String> categories = new ArrayList<>();
    private List<Question> productList = new ArrayList<>();


    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        new Thread(this::handleClients).start();
    }


    //MÅ HA BEDRE FEILHÅNDTERING HER
    private void handleClients() {
        try {
            while (true) {
                handleClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleClient() throws IOException {
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


    if (fileTarget.equals("/api/newQuestion")) {
            Map<String, String> queryMap = parseRequestParameters(httpMessage.messageBody);
            Question product = new Question();
            product.setQuestion(queryMap.get("productName"));
            productList.add(product);
            writeOkResponse(clientSocket, "it is done", "text/html");
        } else if (fileTarget.equals("/api/categoryOptions")){

            int value = 1;
            for (String category : categories) {
                responseText += "<option value=" + (value++) + ">" + category + "</option>";
            }

            writeOkResponse(clientSocket, responseText, "text/html");
        } else if (fileTarget.equals("/api/products")){
            for (int i = 0; i < productList.size(); i++) {
                responseText += "<p>" + productList.get(i).getQuestionName() + "</p>";
            }


            writeOkResponse(clientSocket, responseText, "text/html");
        } else {
            if(rootDirectory != null && Files.exists(rootDirectory.resolve(fileTarget.substring(1)))){
                responseText = Files.readString(rootDirectory.resolve(fileTarget.substring(1)));

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
        httpServer.setCategories(List.of("Mat", "Drikke", "Frukt"));
        httpServer.setRoot(Paths.get("src/main/resources/."));
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }


    public void setRoot(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Question> getProducts() {
        return productList;
    }
}
