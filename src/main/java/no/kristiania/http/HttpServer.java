package no.kristiania.http;

import no.kristiania.controllers.HttpController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

public class HttpServer {

    private ServerSocket serverSocket;
    private HashMap<String, HttpController> controllers = new HashMap<>();


    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);
        new Thread(this::handleClients).start();
    }


    //MÅ HA BEDRE FEILHÅNDTERING HER
    private void handleClients() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleClient(Socket clientSocket) throws IOException, SQLException {
        HttpMessage request = new HttpMessage(clientSocket);
        String[] requestLine = request.getStartLine().split(" ");
        String requestTarget = requestLine[1];
        String requestMethod = requestLine[0];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        String query = null;
        if (questionPos != -1){
            fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos + 1);
        } else {
            fileTarget = requestTarget;
        }

        if (requestMethod.equals("POST")) {
            getController(fileTarget).handle(request, clientSocket);
        } else {
            if (fileTarget.equals("/echo")) {
                //something
            } else {
                HttpController controller = controllers.get(fileTarget);
                if (controller != null) {
                    controller.handle(request, clientSocket);
                } else {
                    writeResponse(clientSocket, fileTarget);
                }
            }
        }
    }


    private HttpController getController(String requestPath) {
        return controllers.get(requestPath);
    }

    private void writeResponse(Socket clientSocket, String requestTarget) throws IOException {
        try (InputStream fileResource = getClass().getResourceAsStream(requestTarget)) {
            if (fileResource == null) {
               String body = "File not found: " + requestTarget;
               HttpMessage response = new HttpMessage(body);
               response.setStartLine("HTTP/1.1 404 Not found");
               response.write(clientSocket);

            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileResource.transferTo(buffer);

            String contentType = "text/plain";
            if (requestTarget.endsWith(".html")) {
                contentType = "text/html; charset=UTF-8";
            } else if (requestTarget.endsWith("css")) {
                contentType = "text/css";
            }


            String response = "HTTP/1.1 200 OK\r\n" +
                    //getBytes i stedet for length
                    "Content-Length: " + buffer.toByteArray().length + "\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";
            clientSocket.getOutputStream().write(response.getBytes());
            clientSocket.getOutputStream().write(buffer.toByteArray());
        } catch (NullPointerException err) {

        }
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void addController(String path, HttpController controller) {
        this.controllers.put(path, controller);
    }

}
