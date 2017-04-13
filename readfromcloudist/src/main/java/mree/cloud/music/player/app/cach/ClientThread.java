package mree.cloud.music.player.app.cach;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by eercan on 27.12.2016.
 */

public class ClientThread {
    private static final String TAG = ClientThread.class.getSimpleName();
    private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: ";
    private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";
    private Socket client;

    public ClientThread(Socket client) {
        this.client = client;
    }


    public void run() {
        try {
            InputStream inputStream = client.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String request = br.readLine();
            OutputStream outputStream = client.getOutputStream();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
            String[] strings = request.split("/");
            for (String s : strings) {
                if (s.startsWith("id=")) {
                    String[] split = s.split("=");
                    File file = new File(ProxyServer.CACHE_ROOT, split[1] + ProxyServer.EXTENSION);
                    if (file.exists()) {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] fileArray = new byte[fis.available()];
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bis.read(fileArray, 0, fileArray.length);
                        String output = OUTPUT_HEADERS + fileArray.length + OUTPUT_END_OF_HEADERS;
                        outputStream.write(output.getBytes());
                        outputStream.write(fileArray);

                    } else {
                        outputStream.write(null);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }
}
