package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.data.DomainRepository;
import org.launchcode.liftoffproject.models.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("quiz")
public class QuizController {}
//
//    @Autowired
//    private DomainRepository domainRepository;
//
//    List<String> quizResults = new ArrayList<>();
//
//    @GetMapping("quiz")
//    public String displayAllQuestions(Model model) {
//        List<String> questionnaire = new ArrayList<>();
//        model.addAttribute("questionnaire", questionnaire);
//        return "quiz";
//    }
//
//    @GetMapping("results")
//    public String displayResults(Model model) {
//        List<String> questionnaire = new ArrayList<>();
//        model.addAttribute("questionnaire", questionnaire);
//        model.addAttribute("quizResults", quizResults);
//        return "results";
//    }
//
//    @PostMapping("quiz")
//    public String processFormMethodQuiz(@ModelAttribute @Valid Quiz quiz, Model model, @RequestParam(required = false)
//            List<String> impulseControl, @RequestParam(required = false) List<String> emotionalControl, @RequestParam(required = false)
//                                                    List<String> flexibleThinking, @RequestParam(required = false)
//                                                    List<String> workingMemory, @RequestParam(required = false)
//                                                    List<String> selfMonitoring, @RequestParam(required = false)
//                                                    List<String> planningAndPrioritizing, @RequestParam(required = false)
//                                                    List<String> taskInitiation, @RequestParam(required = false)
//                                                    List<String> organization) {
//
//        model.addAttribute("quiz", quiz);
//
//        if (impulseControl.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domain", domainRepository.findById(1));
//            model.addAttribute("quiz", quizResults);
//            System.out.println("FOUND IMPULSE CONTROL");
//        } else if (impulseControl == null){
//            model.addAttribute("title", "Display Domain");
//            System.out.println("DIDN'T FIND IMPULSE CONTROL");
//        }
//
//        if (emotionalControl.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(2));
//            model.addAttribute("quiz", quizResults);
//        } else if (emotionalControl == null){
//            model.addAttribute("title", "Quiz");
//        }
//
//        if (flexibleThinking.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(3));
//            model.addAttribute("quiz", quizResults);
//        } else if (flexibleThinking == null){
//            model.addAttribute("title", "Quiz");
//        }
//
//        if (workingMemory.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(4));
//            model.addAttribute("quiz", quizResults);
//        } else if (workingMemory == null) {
//            model.addAttribute("title", "Quiz");
//        }
//
//        if (selfMonitoring.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(5));
//            model.addAttribute("quiz", quizResults);
//
//        } else if (selfMonitoring == null){
//            model.addAttribute("title", "Quiz");
//        }
//
//        if (planningAndPrioritizing.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(6));
//            model.addAttribute("quiz", quizResults);
//
//        } else if (planningAndPrioritizing == null){
//            model.addAttribute("title", "Quiz");
//        }
//
//        if (taskInitiation.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(7));
//            model.addAttribute("quiz", quizResults);
//
//        } else if (taskInitiation == null){
//            model.addAttribute("title", "Quiz");
//        }
//
//        if (organization.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
//            model.addAttribute("domains", domainRepository.findById(8));
//            model.addAttribute("quiz", quizResults);
//        } else if (organization == null){
//            model.addAttribute("title", "Display Domain");
//        }
//
//        model.addAttribute("title", "Quiz");
//        return "redirect:results";
//    }
//    }
//
//
//
//
//
//