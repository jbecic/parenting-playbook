package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.data.DomainRepository;
import org.launchcode.liftoffproject.data.InterventionRepository;
import org.launchcode.liftoffproject.data.TagRepository;
import org.launchcode.liftoffproject.models.Domain;
import org.launchcode.liftoffproject.models.Intervention;
import org.launchcode.liftoffproject.models.Tag;
import org.launchcode.liftoffproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("edit")
public class EditController {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AuthenticationController authenticationController;

    @GetMapping("/name/{interventionId}")
    public String displayNameEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                model.addAttribute("intervention", intervention);
                return "edit/name";
            }
        }

        return "redirect:../";

    }

    @PostMapping("/name/{interventionId}")
    public String processNameEdit(Model model, @PathVariable int interventionId, @RequestParam String name) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (name.length() < 5 || name.length() > 255) {
            model.addAttribute("intervention", intervention);
            String str = "Name must be longer than 5 characters and not exceed 255 characters.";
            model.addAttribute("nameError", str);
            return "edit/name";
        }

        intervention.setName(name);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("/action/{interventionId}")
    public String displayActionEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                model.addAttribute("intervention", intervention);
                return "edit/action";
            }

        }

        return "redirect:../";

    }

    @PostMapping("/action/{interventionId}")
    public String processActionEdit(Model model, @PathVariable int interventionId, @RequestParam String action) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (action.length() < 20 || action.length() > 2000) {
            model.addAttribute("intervention", intervention);
            String str = "Action must be longer than 20 characters and not exceed 2000 characters.";
            model.addAttribute("actionError", str);
            return "edit/action";
        }

        intervention.setAction(action);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("/expectedResponse/{interventionId}")
    public String displayExpectedResponseEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                model.addAttribute("intervention", intervention);
                return "edit/expectedResponse";
            }

        }

        return "redirect:../";

    }

    @PostMapping("/expectedResponse/{interventionId}")
    public String processExpectedResponseEdit(Model model, @PathVariable int interventionId, @RequestParam(required = false) String expectedResponse) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (expectedResponse.length() < 20 || expectedResponse.length() > 2000) {
            model.addAttribute("intervention", intervention);
            String str = "Expected Response must be longer than 20 characters and not exceed 2000 characters.";
            model.addAttribute("expectedResponseError", str);
            return "edit/expectedResponse";
        }

        intervention.setExpectedResponse(expectedResponse);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("/reference/{interventionId}")
    public String displayReferenceEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                model.addAttribute("intervention", intervention);
                return "edit/reference";
            }
        }

        return "redirect:../";

    }

    @PostMapping("/reference/{interventionId}")
    public String processReferenceEdit(Model model, @PathVariable int interventionId, @RequestParam String reference) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();

        intervention.setReference(reference);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("/ifItFails/{interventionId}")
    public String displayIfItFailsEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                model.addAttribute("intervention", intervention);
                return "edit/ifItFails";
            }

        }

        return "redirect:../";

    }

    @PostMapping("/ifItFails/{interventionId}")
    public String processIfItFailsEdit(Model model, @PathVariable int interventionId, @RequestParam String ifItFails) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();

        intervention.setIfItFails(ifItFails);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("/domains/{interventionId}")
    public String displayDomainsEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                List<Domain> domains = (List<Domain>) domainRepository.findAll();
                for (int i = 0; i < domains.size(); i++) {
                    for (int j = 0; j < intervention.getDomains().size(); j++) {
                        if (domains.get(i).getId() == intervention.getDomains().get(j).getId()) {
                            domains.get(i).setChecked(true);
                        }
                    }
                }
                model.addAttribute("intervention", intervention);
                model.addAttribute("domains", domains);
                return "edit/domains";
            }

        }

        return "redirect:../";

    }

    @PostMapping("/domains/{interventionId}")
    public String processDomainsEdit(Model model, @PathVariable int interventionId, @RequestParam(required = false) List<Integer> domains) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (domains == null) {
            model.addAttribute("intervention", intervention);
            model.addAttribute("domains", domainRepository.findAll());
            String str = "A Domain must be selected.";
            model.addAttribute("checkBoxError", str);
            return "edit/domains";
        }

        List<Domain> domainObjs = (List<Domain>) domainRepository.findAllById(domains);
        intervention.setDomains(domainObjs);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("/tags/{interventionId}")
    public String displayTagsEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                List<Tag> tags = (List<Tag>) tagRepository.findAll();
                for (int i = 0; i < tags.size(); i++) {
                    for (int j = 0; j < intervention.getTags().size(); j++) {
                        if (tags.get(i).getId() == intervention.getTags().get(j).getId()) {
                            tags.get(i).setChecked(true);
                        }
                    }
                }
                model.addAttribute("intervention", intervention);
                model.addAttribute("tags", tags);
                return "edit/tags";
            }

        }

        return "redirect:../";

    }

    @PostMapping("/tags/{interventionId}")
    public String processTagsEdit(Model model, @PathVariable int interventionId, @RequestParam(required = false) List<Integer> tag) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (tag == null) {
            model.addAttribute("intervention", intervention);
            model.addAttribute("tags", tagRepository.findAll());
            String str = "A Tag must be selected.";
            model.addAttribute("checkBoxErrorTag", str);
            return "edit/tags";
        }

        List<Tag> tagObjs = (List<Tag>) tagRepository.findAllById(tag);
        intervention.setTags(tagObjs);
        interventionRepository.save(intervention);

        return "redirect:/view/{interventionId}";
    }

    @GetMapping("delete/{interventionId}")
    public String displayDeleteEdit(Model model, @PathVariable int interventionId, HttpServletRequest request) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        User user = authenticationController.getUserFromSession(request.getSession());
        if (optIntervention.isPresent()) {
            Intervention intervention = (Intervention) optIntervention.get();
            if (intervention.getUser() == null || !intervention.getUser().getUsername().equals(user.getUsername())) {
                return "redirect:/view/{interventionId}/";
            }
            else if (intervention.getUser().getUsername().equals(user.getUsername())) {
                model.addAttribute("intervention", intervention);
                return "edit/delete";
            }
        }

        return "redirect:../";

    }

    @PostMapping("/delete/{interventionId}")
    public String processDeleteEdit(Model model, @PathVariable int interventionId, @RequestParam int delete) {
        Optional optIntervention = interventionRepository.findById(interventionId);
        Intervention intervention = (Intervention) optIntervention.get();
        if (delete == 0) {
            model.addAttribute("intervention", intervention);
            return "redirect:/view/{interventionId}";
        }

        if (delete == 1) {
            interventionRepository.deleteById(interventionId);
        }

        return "redirect:/view/{interventionId}";
    }
}
