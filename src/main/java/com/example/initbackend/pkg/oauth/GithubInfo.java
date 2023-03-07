package com.example.initbackend.pkg.oauth;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class GithubInfo {
    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private static String clientId;

    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private static String clientSecret;
    public static String getGithubAccessToken(Map<String, Object> requestMap) throws IOException, ParseException {

        URL url = new URL("https://github.com/login/oauth/access_token");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");

        String requestBody = getJsonStringFromMap(requestMap);
        System.out.println("requestBody:" + requestBody);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(requestBody);
        bw.flush();
        bw.close();

        System.out.println("getContentType():" + conn.getContentType()); // 응답 콘텐츠 유형 구하기
        System.out.println("getResponseCode():"    + conn.getResponseCode()); // 응답 코드 구하기
        System.out.println("getResponseMessage():" + conn.getResponseMessage()); // 응답 메시지 구하기

        Charset charset = Charset.forName("UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

        String inputLine;
        StringBuffer sb = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        br.close();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( sb.toString() );
        JSONObject jsonObj = (JSONObject) obj;

        String accessToken = (String) jsonObj.get("access_token");

        return accessToken;
    }
    public static Map<String, Object> getGithubUserInfo(String access_token) throws IOException, ParseException {

        URL url = new URL("https://api.github.com/user");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        conn.setRequestProperty("Authorization", "token " + access_token);


        System.out.println("getContentType():" + conn.getContentType()); // 응답 콘텐츠 유형 구하기
        System.out.println("getResponseCode():"    + conn.getResponseCode()); // 응답 코드 구하기
        System.out.println("getResponseMessage():" + conn.getResponseMessage()); // 응답 메시지 구하기

        Charset charset = Charset.forName("UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

        String inputLine;
        StringBuffer sb = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        br.close();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( sb.toString() );
        JSONObject jsonObj = (JSONObject) obj;

        String email = (String) jsonObj.get("email");
        String nickname = (String) jsonObj.get("login");

        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("email", email);
        userInfo.put("nickname", nickname);

        return userInfo;
    }

    public static String getJsonStringFromMap(Map<String, Object> map) {

        JSONObject json = new JSONObject();

        for(Map.Entry<String, Object> entry : map.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            json.put(key, value);
        }

        return json.toJSONString();
    }
}
