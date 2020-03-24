package udacity.pokemon;

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeHttpRequest(String url) throws IOException {
        URL _url = createUrl(url);
        if (_url != null) return makeHttpRequest(_url);
        return null;
    }

    public String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection httpURLCon = null;
        InputStream is = null;
        String jsonResponse = "";

        try {
            httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestMethod("GET");
            httpURLCon.setReadTimeout(10000);
            httpURLCon.setConnectTimeout(15000);
            httpURLCon.connect();

            is = httpURLCon.getInputStream();
            jsonResponse = convertStreamToString(is);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        } finally {
            if (httpURLCon != null) httpURLCon.disconnect();
            if (is != null) is.close();
        }

        return jsonResponse;
    }

    private URL createUrl(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) sb.append(line).append('\n');

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}