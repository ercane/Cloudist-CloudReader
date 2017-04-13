/*

 *
 * Project: mcys-client-rest
 * Date Created: 7 May 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.rest.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import mree.cloud.music.player.common.ws.RequestHeader;

/**
 * @author ykurt
 */

public class AuthHeaderClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private String password;
    private String username;

    public AuthHeaderClientHttpRequestInterceptor(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add(RequestHeader.HEADER_USERNAME, username);
        headers.add(RequestHeader.HEADER_PASSWORD, password);
        return execution.execute(request, body);
    }
}
