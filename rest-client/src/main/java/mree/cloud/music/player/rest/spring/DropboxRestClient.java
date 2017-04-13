package mree.cloud.music.player.rest.spring;

import org.springframework.http.converter.StringHttpMessageConverter;

import mree.cloud.music.player.common.ws.dropbox.Credentials;
import mree.cloud.music.player.common.ws.dropbox.DropboxServiceUri;
import mree.cloud.music.player.rest.utils.GsonConverter;

/**
 * Created by mree on 09.01.2017.
 */

public class DropboxRestClient extends BaseRestClient{


    private static String SERVICE_URI = "https://api.dropboxapi.com/2";
    private static String PREFIX = "Authorization";

    public DropboxRestClient(String token){
        super(SERVICE_URI, PREFIX, "bearer " + token, GsonConverter.getGsonConverterInstance());
        addConverter(new StringHttpMessageConverter());
    }

    @Override
    public void setxToken(String xToken){
        super.setxToken("bearer " + xToken);
    }

    public String getListFolderFilesByPath(String path){
        Credentials credentials = Credentials.getDefault();
        credentials.path = path;
        return post(DropboxServiceUri.LIST_FOLDER_FILES, credentials, String.class);
    }
}