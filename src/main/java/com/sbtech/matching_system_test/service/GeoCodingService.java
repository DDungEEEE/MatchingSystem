package com.sbtech.matching_system_test.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GeoCodingService {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    public Map<String, Double> getLocationByAd(String address) throws IOException {
        String encodedAd = URLEncoder.encode(address, StandardCharsets.UTF_8)
                .replace("+", "%20");

        StringBuilder sb = new StringBuilder();

        String sUrl = sb.append("https://maps.googleapis.com/maps/api/geocode/json?address=")
                .append(encodedAd)
                .append("&key=")
                .append(GOOGLE_API_KEY)
                .append("&language=ko")
                .toString();

        try{
            URL url = new URL(sUrl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder resStringBuilder = new StringBuilder();
            String inputStr;

            while((inputStr = br.readLine()) != null){
                resStringBuilder.append(inputStr);
            }

            JSONObject jsonObject = new JSONObject(resStringBuilder.toString());

            if(!jsonObject.getString("status").equals("OK")){
                log.error("GOOGLE API ERROR");
                return null;
            }

            JSONArray results = jsonObject.getJSONArray("results");
            Map<String, Double> result = new HashMap<>();

            if(!results.isEmpty()){
                JSONObject jo = results.getJSONObject(0);

                //위도
                Double lat = jo.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                // 경도
                Double lng = jo.getJSONObject("geometry").getJSONObject("location").getDouble("lng");


                result.put("lat", lat);
                result.put("lng", lng);

                return result;
            }
        }catch (IOException ex){
            throw ex;
        }
        return null;
    }
}
