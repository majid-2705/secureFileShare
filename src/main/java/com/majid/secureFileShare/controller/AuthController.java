package com.majid.secureFileShare.controller;

import com.majid.secureFileShare.Exception.InvalidTokenException;
import com.majid.secureFileShare.Exception.UserNotFindException;
import com.majid.secureFileShare.dataTransferObject.LoginRequest;
import com.majid.secureFileShare.dataTransferObject.RegisterRequest;
import com.majid.secureFileShare.model.PasswordResetToken;
import com.majid.secureFileShare.model.User;
import com.majid.secureFileShare.service.PasswordResetService;
import com.majid.secureFileShare.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    public AuthController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest(null, null));
        return "register";
    }
    @PostMapping("/register")
    public String register (@ModelAttribute RegisterRequest registerRequest, RedirectAttributes redirectAtt) {
        Optional<User> user = userService.findByEmail(registerRequest.email());
        if(user.isPresent()) {
            redirectAtt.addFlashAttribute("message", "A user with the given email address already exists");
            return "redirect:/auth/register";
        }
        else {
            userService.registerUser(registerRequest.email(), registerRequest.password());
            redirectAtt.addFlashAttribute("message", "The registration action has been performed successfully");
            return "redirect:/auth/login";

        }
    }

    @GetMapping("/login")
    public String getLoginPage() {
    return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, RedirectAttributes redirectAtt) {
        boolean isAuthenticatedLogin = userService.authenticate(loginRequest.email(), loginRequest.password());
        System.out.println(loginRequest.email() + "; " + loginRequest.password());
        if(isAuthenticatedLogin) {
            return "redirect:/auth/register";
        }
        else {
            redirectAtt.addFlashAttribute("message", "Email address or password are incorrect");
            return "redirect:/auth/login";
        }

    }
    @GetMapping("/failedLogin")
    public String showLoginFailed()
    {
        return "failedLogin";
    }

    @GetMapping ("/forgotPassword")
    public String getForgotPasswordTemplate () {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String sendResetPasswordEmail(@RequestParam("email") String userEmail, RedirectAttributes redirectAtt) {
    try {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFindException("No user found with this email"));
         PasswordResetToken resetToken = passwordResetService.findByUser(user)
                .orElse(new PasswordResetToken());
        passwordResetService.createPasswordResetToken(user, resetToken);
        redirectAtt.addFlashAttribute("message", "If the email you set was used for registration, a password rest email\n" +
                "will be sent to the provided email");
        return "redirect:/auth/dispatchResetPasswordEmail";
    }

    catch (UserNotFindException e) {
        redirectAtt.addFlashAttribute("message", e.getMessage());

    }
        return "redirect:/auth/dispatchResetPasswordEmail";
    }
    @GetMapping("/dispatchResetPasswordEmail")
    public String confirmSentEmail()
    {
        return "dispatchResetPasswordEmail";
    }

    @GetMapping("/reset-password")
    public String getRestPasswordForm(@RequestParam String token, Model model)
    {


        try {
            passwordResetService.checkTokenValidity(token,
                    "Invalid link: Reset token is invalid",
                    "Invalid link: Reset token has expired");

            model.addAttribute("token", token);
            return "resetPassword";
        }
        catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
            return "invalid-link";
        }
    }

    @PostMapping("reset-password")
    public String performPasswordReset(@RequestParam ("token") String token,
                                       @RequestParam ("password") String password, Model model)
    {       try {
            passwordResetService.resetPassword(token, password);
            return "redirect:/auth/login";
            }
            catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
            return "invalid-link";
            }

    }


}
