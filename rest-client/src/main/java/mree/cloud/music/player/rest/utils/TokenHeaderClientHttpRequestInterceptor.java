package mree.cloud.music.player.rest.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created by mree on 23.01.2016.
 */
public class TokenHeaderClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private String HEADER_TOKEN;
    private String token;

    public TokenHeaderClientHttpRequestInterceptor(String headerToken, String token) {
        this.HEADER_TOKEN = headerToken;
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(HEADER_TOKEN, token);
        return execution.execute(request, body);
    }
}