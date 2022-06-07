package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.data.*;
import org.launchcode.liftoffproject.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
                    tempArr = line.split(delimiter, 19);
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
                Tag tag = new Tag(tags[i], null);
                tagRepository.save(tag);
            }
        }
    }

    public void saveInterventions() throws FileNotFoundException {
        String delimiter = ";";
        List repo = (List) interventionRepository.findAll();

        if (repo.isEmpty()) {
            try {
                File file = new File("src/main/resources/assets/ParentingPlaybookData - Book4.csv");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line = " ";
                String[] tempArr;
                User user = null;
                while ((line = br.readLine()) != null) {
                    tempArr = line.split(delimiter);
                    Intervention newIntervention = new Intervention(tempArr[0], tempArr[1], tempArr[2], tempArr[3], tempArr[4], user);
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

    public String clickableURL(String reference) {
        String[] parts = reference.split("\\s+");
        String start = "<p>";
        String end = "</p>";

        for (int i = 0; i < parts.length; i++) try {
            URL url = new URL(parts[i]);
            parts[i] = "<a target=\"_blank\" href=\"" + url + "\">"+ url + "</a>";
        } catch (MalformedURLException e) {
            System.out.print( parts[i] + " " );
            if (parts[i].contains(".com")) {
                parts[i] = "<a target=\"_blank\" href=\"https://" + parts[i] + "\">"+ parts[i] + "</a>";
            }
        }

        String joined = "";

        for (String part : parts) {
            joined += part + " ";
        }

        String output = start + joined + end;

        return output;
    }

    public Boolean detectURL(String reference) {
        String[] parts = reference.split("\\s+");
        Boolean output = false;

        for (String part : parts) try {
            URL url = new URL(part);
            if (url != null) {
                output = true;
                break;
            }
        } catch (MalformedURLException e) {
            System.out.print( part + " " );
            if (part.contains(".com")) {
                output = true;
                break;
            }
        }

        return output;
    }

    @RequestMapping("")
    public String index(Model model, HttpServletRequest request) throws FileNotFoundException {
        createDomains();
        createTags();
        saveInterventions();

        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));
        model.addAttribute("title", "All Domains");
        model.addAttribute("domains", domainRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddInterventionForm(Model model, HttpServletRequest request) {
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));

        if (authenticationController.isUserLoggedIn(request)) {
            model.addAttribute("title", "Add Intervention");
            model.addAttribute(new Intervention());
            model.addAttribute("domains", domainRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            User user = new User();
            model.addAttribute("username", user.getUsername());
            model.addAttribute(user);

            return "add";
        }
        return "redirect:";
    }

    @PostMapping("add")
    public String processAddInterventionForm(@ModelAttribute @Valid Intervention newIntervention, Errors errors, Model model, @RequestParam(required = false) List<Integer> domains, @RequestParam(required = false) List<Integer> tag, HttpServletRequest request) {
        User user = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));

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
            model.addAttribute("user", userRepository.findAll());
            return "add";
        }

        List<Domain> domainObjs = (List<Domain>) domainRepository.findAllById(domains);
        List<Tag> tagObjs = (List<Tag>) tagRepository.findAllById(tag);

        newIntervention.setUser(user);
        newIntervention.setDomains(domainObjs);
        newIntervention.setTags(tagObjs);

        interventionRepository.save(newIntervention);

        return "redirect:/profile";
    }

    @GetMapping("view/{interventionId}")
    public String displayViewIntervention(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            model.addAttribute("intervention", intervention);
            model.addAttribute("detectURL", detectURL(intervention.getReference()));
            if (detectURL(intervention.getReference())) {
                String clickableURL = clickableURL(intervention.getReference());
                model.addAttribute("clickableURL", clickableURL);
            }

            User user = authenticationController.getUserFromSession(request.getSession());
            model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));
            Comment comment = new Comment();

            if (user != null) {
                model.addAttribute("comments", commentRepository.findCommentByInterventionIdAndUserId(interventionId, user.getId()));
                if (user == intervention.getUser()) {
                    model.addAttribute("initialUser", true);
                }
            }

            model.addAttribute("comment", comment);
            model.addAttribute("user", user);

            return "view";
        } else {
            return "redirect:../";
        }
    }

    @PostMapping("view/{interventionId}")
    public String processAddComment(@ModelAttribute @Valid Comment newComment, Errors errors, Model model,
                                    @PathVariable int interventionId, HttpServletRequest request) {
        User user = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));

        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (errors.hasErrors()) {
            model.addAttribute("intervention", intervention);
            return "view";
        }

        newComment.setUser(user);
        newComment.setIntervention(intervention);
        commentRepository.save(newComment);
        return "redirect:{interventionId}";
    }


    @GetMapping("about")
    public String displayAbout(Model model, HttpServletRequest request) {
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));
        return "about";
    }

    @GetMapping("faq")
    public String displayFaq(Model model, HttpServletRequest request) {
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));
        return "faq";
    }

    List<String> quizResults = new ArrayList<>();

    @GetMapping("quiz")
    public String displayAllQuestions(Model model, HttpServletRequest request) {
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));
        List<String> questionnaire = new ArrayList<>();
        model.addAttribute("questionnaire", questionnaire);
        return "quiz";
    }

    @GetMapping("results")
    public String getResults(Model model, Quiz quiz, HttpServletRequest request) {
        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));
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
                                                List<String> organization, @RequestParam(required = false) List<String> none, HttpServletRequest request) {

        model.addAttribute("loggedIn", authenticationController.isUserLoggedIn(request));

        if (impulseControl == null || impulseControl.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (impulseControl.size() >= 2) {
            model.addAttribute("impulseControl", domainRepository.findById(1));
        }

        if (emotionalControl == null || emotionalControl.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (emotionalControl.size() >= 2) {
            model.addAttribute("emotionalControl", domainRepository.findById(2));
        }

        if (flexibleThinking == null || flexibleThinking.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (flexibleThinking.size() >= 2) {
            model.addAttribute("flexibleThinking", domainRepository.findById(3));
        }

        if (workingMemory == null || workingMemory.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (workingMemory.size() >= 2) {
            model.addAttribute("workingMemory", domainRepository.findById(4));
        }

        if (selfMonitoring == null || selfMonitoring.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (selfMonitoring.size() >= 2) {
            model.addAttribute("selfMonitoring", domainRepository.findById(5));
        }

        if (planningAndPrioritizing == null || planningAndPrioritizing.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (planningAndPrioritizing.size() >= 2) {
            model.addAttribute("planningAndPrioritizing", domainRepository.findById(6));
        }

        if (taskInitiation == null || taskInitiation.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (taskInitiation.size() >= 2) {
            model.addAttribute("taskInitiation", domainRepository.findById(7));
        }

        if (organization == null || organization.size() < 2) {
            model.addAttribute("title", quiz);
        } else if (organization.size() >= 2) {
            model.addAttribute("organization", domainRepository.findById(8));
        }

        if ((impulseControl == null || impulseControl.size() < 2) && (emotionalControl == null || emotionalControl.size() < 2)
                && (flexibleThinking == null || flexibleThinking.size() < 2) && (workingMemory == null || workingMemory.size() < 2)
                && (selfMonitoring == null || selfMonitoring.size() < 2) && (planningAndPrioritizing == null || planningAndPrioritizing.size() < 2)
                && (taskInitiation == null || taskInitiation.size() < 2) && (organization == null || organization.size() < 2)) {
            model.addAttribute("title", quiz);
            model.addAttribute("none", domainRepository.findAll());

        }
        return "results";
    }


}