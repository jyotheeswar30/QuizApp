package com.example.quizapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
public class QuizServiceImpl {

    @Value("${film.url}")
    private String filmUrl;
    @Value("${book.url}")
    private String bookUrl;

    @Autowired
    RestTemplate restTemp;

    public ResponseEntity<Object> getQuizResponse() {
        ResponseEntity<String> respMusic= restTemp.exchange(filmUrl, HttpMethod.GET, null, String.class);
       String musicResponse= parseResponse(respMusic);
       ResponseEntity<String> respBook= restTemp.exchange(bookUrl, HttpMethod.GET, null, String.class);
        String bookResponse= parseResponse(respBook);
       return ResponseEntity.ok(buildReponse(musicResponse,bookResponse));
    }

    private Object buildReponse(String musicResponse, String bookResponse) {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonArray.put(musicResponse);
        jsonArray.put(bookResponse);
        jsonObject.put("quiz",jsonArray);
        return jsonObject;
    }

    private String parseResponse(ResponseEntity<String> resp) {
        if(resp.getStatusCode()== HttpStatus.ACCEPTED) {
            JSONObject jsonObject = new JSONObject(resp.getBody());
            Integer responseCode= jsonObject.getInt("response_code");
            if(responseCode==0){
               return getResponse(jsonObject);
            }else if (responseCode==1){
                return jsonObject.toString();
            }
        }
        return null;
    }

    private String getResponse(JSONObject jsonObjectServiceResponse) {


        JSONArray actualJsonArrayResponse=new JSONArray();

        JSONArray jsonArrayResult=jsonObjectServiceResponse.getJSONArray("results");
        String category=jsonArrayResult.getJSONObject(0).getString("category");
        for (int i=0;i<jsonArrayResult.length();i++){
            JSONObject singleQuizJSON=jsonArrayResult.getJSONObject(i);
            singleQuizJSON.remove("category");
           JSONArray quizAnswersArray= singleQuizJSON.getJSONArray("incorrect_answers");
            quizAnswersArray.put(singleQuizJSON.get("correct_answer"));
            singleQuizJSON.remove("incorrect_answers");
            singleQuizJSON.put("all_answers",quizAnswersArray);
            actualJsonArrayResponse.put(singleQuizJSON);
        }
        JSONObject response=new JSONObject();
        response.put("category",category);
        response.put("results",actualJsonArrayResponse);

        return response.toString();

    }
}
