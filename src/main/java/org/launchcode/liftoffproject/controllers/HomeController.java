package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.data.*;
import org.launchcode.liftoffproject.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizController quizController;


    public void createDomains() throws FileNotFoundException {
        String delimiter = ",";
        List repo = (List) domainRepository.findAll();

        if (repo.isEmpty()) {
            try {
                File file = new File("src/main/resources/assets/domains.csv");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line = " ";
                String[] tempArr;
                while ((line = br.readLine()) != null) {
                    tempArr = line.split(delimiter, 2);
                    Domain domain = new Domain(tempArr[0], tempArr[1]);
                    domainRepository.save(domain);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void createTags() {
        String[] tags = {"Aggression", "Anger", "Mindfulness", "Resentment", "Kids", "Adults", "Openness", "Working"};
        List repo = (List) tagRepository.findAll();

        if (repo.isEmpty()) {
            for (int i = 0; i < tags.length; i++) {
                Tag tag = new Tag(tags[i]);
                tagRepository.save(tag);
            }
        }
    }

    public void saveInterventions() throws FileNotFoundException {
        String delimiter = ",";
        List repo = (List) interventionRepository.findAll();

        if (repo.isEmpty()) {
            try {
                File file = new File("src/main/resources/assets/ParentingPlaybookData - Book4.csv");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line = " ";
                String[] tempArr;
                while ((line = br.readLine()) != null) {
                    tempArr = line.split(delimiter);
                    Intervention newIntervention = new Intervention(tempArr[0], tempArr[1], tempArr[2], tempArr[3], tempArr[4]);
                    List<Integer> domains = new ArrayList<Integer>();
                    List<Integer> tags = new ArrayList<Integer>();
                    for (int i = 0; i < tempArr[5].length(); i++) {
                        domains.add(Integer.parseInt(String.valueOf(tempArr[5].charAt(i))));
                        tags.add(Integer.parseInt(String.valueOf(tempArr[5].charAt(i))) + 8);
                    }
                    List<Domain> domainObjs = (List<Domain>) domainRepository.findAllById(domains);
                    List<Tag> tagObjs = (List<Tag>) tagRepository.findAllById(tags);
                    newIntervention.setDomains(domainObjs);
                    newIntervention.setTags(tagObjs);
                    interventionRepository.save(newIntervention);
                }
                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @RequestMapping("")
    public String index(Model model) throws FileNotFoundException {
        createDomains();
        createTags();
        saveInterventions();

        model.addAttribute("title", "All Domains");
        model.addAttribute("domains", domainRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddInterventionForm(Model model) {
        model.addAttribute("title", "Add Intervention");
        model.addAttribute(new Intervention());
        model.addAttribute("domains", domainRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());

        return "add";
    }

    @PostMapping("add")
    public String processAddInterventionForm(@ModelAttribute @Valid Intervention newIntervention, Errors errors, Model model, @RequestParam(required = false) List<Integer> domains, @RequestParam(required = false) List<Integer> tag) {
        if (domains == null || domains.size() == 0 || domains.isEmpty()) {
            model.addAttribute("title", "Add Intervention");
            model.addAttribute("domains", domainRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            String str = "A Domain must be selected.";
            model.addAttribute("checkBoxError", str);
            return "add";
        }

        if (tag == null || tag.size() == 0 || tag.isEmpty()) {
            model.addAttribute("title", "Add Intervention");
            model.addAttribute("domains", domainRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            String str = "A Tag must be selected.";
            model.addAttribute("checkBoxErrorTag", str);
            return "add";
        }

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Intervention");
            model.addAttribute("domains", domainRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            return "add";
        }

        List<Domain> domainObjs = (List<Domain>) domainRepository.findAllById(domains);
        List<Tag> tagObjs = (List<Tag>) tagRepository.findAllById(tag);

        newIntervention.setDomains(domainObjs);
        newIntervention.setTags(tagObjs);

        interventionRepository.save(newIntervention);

        return "redirect:";
    }

    // HttpServletRequest request
    @GetMapping("view/{interventionId}")
    public String displayViewIntervention(Model model, @PathVariable int interventionId) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            model.addAttribute("intervention", intervention);

            User user = new User();
//            User user = authenticationController.getUserFromSession(request.getSession());

            model.addAttribute("comment", new Comment());

            model.addAttribute("username", user.getUsername());
            model.addAttribute(user);

            model.addAttribute("comments", commentRepository.findCommentByInterventionId(interventionId));

            return "view";
        } else {
            return "redirect:../";
        }
    }

    @PostMapping("view/{interventionId}")
    public String processAddComment(@ModelAttribute @Valid Comment newComment, Errors errors, Model model,
                                    @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (errors.hasErrors()) {
            model.addAttribute("intervention", intervention);
            return "view";
        }

        User user = authenticationController.getUserFromSession(request.getSession());

        newComment.setUser(user);
        newComment.setIntervention(intervention);
        commentRepository.save(newComment);
        return "redirect:{interventionId}";
    }

    @GetMapping("about")
    public String displayAbout() {
        return "about";
    }

    @GetMapping("faq")
    public String displayFaq() {
        return "faq";
    }

    List<String> quizResults = new ArrayList<>();

    @GetMapping("quiz")
    public String displayAllQuestions(Model model) {
        List<String> questionnaire = new ArrayList<>();
        model.addAttribute("questionnaire", questionnaire);
        return "quiz";
    }

    @GetMapping("results")
    public String getResults(Model model, Quiz quiz) {
        model.addAttribute("Quiz", quiz);
        return "results";
    }

    @PostMapping("results")
    public String processFormMethodQuiz(@ModelAttribute("quiz") @Valid Quiz quiz, Model model, @RequestParam(required = false)
            List<String> impulseControl, @RequestParam(required = false) List<String> emotionalControl, @RequestParam(required = false)
                                                List<String> flexibleThinking, @RequestParam(required = false)
                                                List<String> workingMemory, @RequestParam(required = false)
                                                List<String> selfMonitoring, @RequestParam(required = false)
                                                List<String> planningAndPrioritizing, @RequestParam(required = false)
                                                List<String> taskInitiation, @RequestParam(required = false)
                                                List<String> organization) {


        if (impulseControl == null) {
            model.addAttribute("title", quiz);
            System.out.println("impulse null");
        } else if (impulseControl.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            model.addAttribute("impulseControl", domainRepository.findById(1));
            System.out.println("impulse found!!");
//            model.addAttribute("quiz", quizResults);
        }

        if (emotionalControl == null) {
            model.addAttribute("title", quiz);
            System.out.println("emocontrol null");
        } else if (emotionalControl.size() >= 2) {
//            model.addAttribute("title", quizResults);
            System.out.println("emo control found!!");
            model.addAttribute("impulseControl", domainRepository.findById(2));
        }

        if (flexibleThinking == null) {
            model.addAttribute("title", quiz);
            System.out.println("flexible null");
        } else if (flexibleThinking.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            System.out.println("flexible found!!");
            model.addAttribute("flexibleThinking", domainRepository.findById(3));
//            model.addAttribute("quiz", quizResults);
        }

        if (workingMemory == null) {
            model.addAttribute("title", quiz);
            System.out.println("working mem null");
        } else if (workingMemory.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            System.out.println("working mem found!!");
            model.addAttribute("workingMemory", domainRepository.findById(4));
//            model.addAttribute("quiz", quizResults);
        }

        if (selfMonitoring == null) {
            model.addAttribute("title", quiz);
            System.out.println("self monitoring null");
        } else if (selfMonitoring.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            System.out.println("self monitoring found!!");
            model.addAttribute("selfMonitoring", domainRepository.findById(5));
//            model.addAttribute("quiz", quizResults);
        }

        if (planningAndPrioritizing == null) {
            model.addAttribute("title", quiz);
            System.out.println("planning null");
        } else if (planningAndPrioritizing.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            System.out.println("planning found!!");
            model.addAttribute("planningAndPrioritizing", domainRepository.findById(6));
//            model.addAttribute("quiz", quizResults);
        }

        if (taskInitiation == null) {
            model.addAttribute("title", quiz);
            System.out.println("task init null");
        } else if (taskInitiation.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            System.out.println("task init found!!");
            model.addAttribute("taskInitiation", domainRepository.findById(7));
//            model.addAttribute("quiz", quizResults);
        }

        if (organization == null) {
            model.addAttribute("title", quiz);
            System.out.println("org null");
        } else if (organization.size() >= 2) {
//            model.addAttribute("title", "Display Domain");
            System.out.println("org found!!");
            model.addAttribute("organization", domainRepository.findById(8));
//            model.addAttribute("quiz", quizResults);
        }

        model.addAttribute("Quiz", quiz);

        return "results";

    }

}

