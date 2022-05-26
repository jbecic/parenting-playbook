package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.data.UserRepository;
import org.launchcode.liftoffproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private DomainRepository domainRepository;
//
//    @Autowired
//    private InterventionRepository interventionRepository;
//
//    @Autowired
//    private TagRepository tagRepository;
////
//    @Autowired
//    private CommentRepository commentRepository;
//
//
    @GetMapping
    public String profile(Model model, HttpSession session, RedirectAttributes redirAttrs) {
        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            redirAttrs.addFlashAttribute("mustlogin", "Please log into access this feature.");

            return "redirect:login";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        model.addAttribute("email", user.getEmail());
        //model.addAttribute("comments", user.getComments());
        //model.addAttribute("addedInterventions", user.getAddedInterventions());
        //model.addAttribute("savedInterventions", user.getSavedInterventions());
        //model.addAttribute("notes", user.getNotes());
        //model.addAttribute("sharedLinks", user.getSharedLinks());
        model.addAttribute(user);

        return "profile";
    }

}
