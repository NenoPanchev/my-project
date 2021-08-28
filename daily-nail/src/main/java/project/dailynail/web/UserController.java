package project.dailynail.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dailynail.models.binding.UserChangingNameAndEmailBindingModel;
import project.dailynail.models.binding.UserRegistrationBindingModel;
import project.dailynail.models.dtos.UserFullNameAndEmailDto;
import project.dailynail.models.service.UserServiceModel;
import project.dailynail.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("userRegistrationBindingModel")
    public UserRegistrationBindingModel createBindingModel() {
        return new UserRegistrationBindingModel();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String failedLogin(@ModelAttribute("email") String email,
                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("bad_credentials", true);
        redirectAttributes.addFlashAttribute("email", email);

        return "redirect:login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerAndLoginUser(@Valid UserRegistrationBindingModel userRegistrationBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationBindingModel", userRegistrationBindingModel);
            redirectAttributes.addFlashAttribute("acceptedTerms", true);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userRegistrationBindingModel", bindingResult);

            return "redirect:register";
        }

        if (this.userService.existsByEmail(userRegistrationBindingModel.getEmail())) {
            redirectAttributes.addFlashAttribute("userExistsError", true);
            redirectAttributes.addFlashAttribute("userRegistrationBindingModel", userRegistrationBindingModel);
            return "redirect:register";
        }

        UserServiceModel userServiceModel = modelMapper
                .map(userRegistrationBindingModel, UserServiceModel.class);
        userService.registerAndLoginUser(userServiceModel);
        return "redirect:/";
    }

    @GetMapping("/terms-and-conditions")
    public String terms() {
        return "terms-and-conditions";
    }

    @GetMapping("/profile/settings")
    public String profile(Principal principal, Model model) {

        model.addAttribute("principalName", userService.getUserNameByEmail(principal.getName()));
        model.addAttribute("principalEmail", principal.getName());
        return "profile-settings";
    }

    @PostMapping("/profile/settings")
    public String updateNameAndEmail(@Valid @ModelAttribute("userChangingNameAndEmailBindingModel") UserChangingNameAndEmailBindingModel userChangingNameAndEmailBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes, Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userChangingNameAndEmailBindingModel", userChangingNameAndEmailBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userChangingNameAndEmailBindingModel", bindingResult);

            return "redirect:settings";
        }

        if (this.userService.existsByEmail(userChangingNameAndEmailBindingModel.getEmail())
        && !userChangingNameAndEmailBindingModel.getEmail().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("userExistsError", true);
            redirectAttributes.addFlashAttribute("userChangingNameAndEmailBindingModel", userChangingNameAndEmailBindingModel);
            return "redirect:settings";
        }


        userService.checkIfInputIsDifferentAndUpdateUserNameAndEmail(
                modelMapper.map(userChangingNameAndEmailBindingModel, UserFullNameAndEmailDto.class),
                principal.getName());
        return "redirect:/";
    }
}