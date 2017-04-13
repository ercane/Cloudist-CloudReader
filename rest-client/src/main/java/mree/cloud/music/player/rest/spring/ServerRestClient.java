package mree.cloud.music.player.rest.spring;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

import mree.cloud.music.player.common.model.auth.AccountInfo;
import mree.cloud.music.player.common.ws.AuthServiceUri;
import mree.cloud.music.player.common.ws.RequestHeader;

/**
 * Created by eercan on 01.12.2016.
 */

public class ServerRestClient extends BaseRestClient {


    public ServerRestClient(String SERVICE_URI, String xToken) {
        super(SERVICE_URI, RequestHeader.HEADER_TOKEN, xToken, new
                MappingJackson2HttpMessageConverter());
    }

    public AccountInfo createAccount(AccountInfo accountInfo) {
        return post(AuthServiceUri.ACCOUNT_ADD, accountInfo, AccountInfo.class, Collections
                .EMPTY_MAP);
    }

}
