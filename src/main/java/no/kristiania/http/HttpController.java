package no.kristiania.http;

import java.sql.SQLException;

public interface HttpController {
    public HttpMessage handle(HttpMessage request) throws SQLException;
}
