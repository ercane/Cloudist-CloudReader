package mree.cloud.music.player.rest.ok;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import mree.cloud.music.player.common.model.dropbox.DownloadInfo;
import mree.cloud.music.player.common.model.dropbox.ListFolder;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by mree on 09.01.2017.
 */

public class DropboxOkHttp extends BaseOkHttp {

    private static final Gson GSON = new Gson();

    public DropboxOkHttp(String access_token) {

        super(access_token);
    }

    public void download(String path_dropbox_file, String local_file) throws IOException {
        MediaType JSON = MediaType.parse("");

        JSONObject data = new JSONObject();
        try {
            data.put("path", path_dropbox_file);
        } catch (JSONException e) {
        }

        String json = data.toString();

        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("https://content.dropboxapi.com/2/files/download")
                .addHeader("Authorization", "Bearer " + access_token)
                .addHeader("Dropbox-API-Arg", json)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        File downloadedFile = new File(local_file);
        BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
        sink.writeAll(response.body().source());
        sink.close();

    }

    public ListFolder listFolder(String path_folder, boolean recursive) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject data = new JSONObject();
        try {
            data.put("path", path_folder);
            data.put("recursive", recursive);
        } catch (JSONException e) {
        }

        String json = data.toString();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/2/files/list_folder")
                .addHeader("Authorization", "Bearer " + access_token)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        String resultJson = response.body().string();
        ListFolder listFolder = new Gson().fromJson(resultJson, ListFolder.class);
        return listFolder;
    }

    public DownloadInfo getTempLink(String path_folder) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject data = new JSONObject();
        try {
            data.put("path", path_folder);
        } catch (JSONException e) {
        }

        String json = data.toString();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/2/files/get_temporary_link")
                .addHeader("Authorization", "Bearer " + access_token)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        String resultJson = response.body().string();
        DownloadInfo di = GSON.fromJson(resultJson, DownloadInfo.class);
        return di;
    }

    public String createFolder(String path_folder) throws IOException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject data = new JSONObject();
        try {
            data.put("path", path_folder);
        } catch (JSONException e) {
        }

        String json = data.toString();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/2/files/create_folder")
                .addHeader("Authorization", "Bearer " + access_token)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String deleteFolder(String path_dropbox_folder) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject data = new JSONObject();
        try {
            data.put("path", path_dropbox_folder);
        } catch (JSONException e) {
        }
        String json = data.toString();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/2/files/delete")
                .addHeader("Authorization", "Bearer " + access_token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String deleteFile(String path_dropbox_file) throws IOException {
        return deleteFolder(path_dropbox_file);
    }

    /**
     * @param path        String The path in the user's Dropbox to search. Should probably be a
     *                    folder.
     * @param query       String The string to search for. The search string is split on spaces
     *                    into multiple tokens. For file name searching, the last token is used
     *                    for prefix matching (i.e. "bat c" matches "bat cave" but not "batman
     *                    car").
     * @param start       int The starting index within the search results (used for paging). The
     *                    default for this field is 0.
     * @param max_results int The maximum number of search results to return. The default for
     *                    this field is 100.
     * @param mode        String The search mode (filename, filename_and_content, or
     *                    deleted_filename).
     * @return String Response of the query
     * @throws IOException
     */
    public String search(String path, String query, int start, int max_results, String mode)
            throws IOException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject data = new JSONObject();
        try {
            data.put("path", path);
            data.put("query", query);
            data.put("start", start);
            data.put("max_results", max_results);
            data.put("mode", mode);
        } catch (JSONException e) {
        }

        String json = data.toString();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://api.dropboxapi.com/2/files/search")
                .addHeader("Authorization", "Bearer " + access_token)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}