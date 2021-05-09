package com.example.quizapp.controllers;

import com.example.quizapp.service.QuizServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class QuizController {

    private Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizServiceImpl quizService;


    @RequestMapping(value = "coding/exercise/quiz",method=RequestMethod.GET )
    public ResponseEntity<Object> getWeather()
            throws IOException {
        logger.info("API invoked : coding/exercise/quiz");
        return quizService.getQuizResponse();
    }
}