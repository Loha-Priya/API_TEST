package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class validateResponse {
    static StringBuilder response = new StringBuilder();
    int responseCode=0;
    public void validateResposnse() {

        String apiUrl = "https://eacp.energyaustralia.com.au/codingtest/api/v1/festivals";

        try {

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

             responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("API response  successful!");
            } else {
                System.out.println("Error fetching API: " + responseCode);
            }
            connection.disconnect();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void storeValues() {
        try {

            String jsonString = response.toString();
            ObjectMapper objectMapper = new ObjectMapper();

            List<Map<String, Object>> festivalList = objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });

            for (Map<String, Object> festival : festivalList) {
                System.out.println("Festival Name: " + festival.get("name"));
                List<Map<String, Object>> bands = (List<Map<String, Object>>) festival.get("bands");
                for (Map<String, Object> band : bands) {
                    System.out.println("Band Name: " + band.get("name") + ", Record Label: " + band.get("recordLabel"));
                }
                System.out.println();
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void nullValues() {
        try {

            String jsonString = response.toString();
            ObjectMapper objectMapper1 = new ObjectMapper();

            List<Map<String, Object>> festivalList1 = objectMapper1.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
            int count = 0;
            int count1 = 0;
            for (Map<String, Object> festival1 : festivalList1) {
                if (festival1.get("name") == null) {
                    // System.out.println("Festival Name is Null");
                    count++;
                }
                List<Map<String, Object>> bands = (List<Map<String, Object>>) festival1.get("bands");
                for (Map<String, Object> band : bands) {
                    if (band.get("recordLabel") == null) {
                        // System.out.println(" Record Label is Null");
                        count1++;
                    }

                }
                System.out.println();

            }
            System.out.println("Festival name null count is " + count);
            System.out.println("Record Label null count is " + count1);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void verifybadRequest(){

        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("Not received expected response " + responseCode);
        }

    }

}
