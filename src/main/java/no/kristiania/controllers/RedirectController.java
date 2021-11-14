package no.kristiania.controllers;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class RedirectController implements HttpController {

    private final String path;

    public RedirectController(String path) {
        this.path = path;
    }

    @Override
    public void handle(HttpMessage request, Socket socket) throws SQLException, IOException {
        HttpMessage redirect = new HttpMessage();
        redirect.setStartLine("HTTP/1.1 302 Redirect");
        redirect.getHeader().put("Location", "http://localhost:1962/" + path);
        redirect.write(socket);
    }
}
