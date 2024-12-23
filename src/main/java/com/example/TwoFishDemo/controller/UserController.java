package com.example.TwoFishDemo.controller;

import com.example.TwoFishDemo.model.User;
import com.example.TwoFishDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/signup")
    public String showSignUpForm(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        // Clear the session or authentication context (depends on your session management)
        return "redirect:/login"; // Redirect back to the login page
    }

    // Admin dashboard endpoint


    // User dashboard endpoint
    @GetMapping("/user-dashboard")
    public String userDashboard(Model model) {
        User loggedInUser = userService.getUserByUsername("currentUserUsername"); // Replace with actual logic
        model.addAttribute("user", loggedInUser);
        return "user-dashboard"; // Returns the user dashboard view
    }

    @PostMapping("/signup")
    public String processSignupForm(@ModelAttribute("user") User user, Model model){
        try {
            //user.setPassword(userService.encryptPassword(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("successMessage","User has been regestered");
            return "login";
        }catch (Exception e){
            model.addAttribute("errorMessage", "Error during signup: " + e.getMessage());
            return "signup";
        }
    }
    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute("user") User user, Model model) {
        try {
            User existingUser = userService.getUserByUsername(user.getUsername());
            // Check if the user exists and if the decrypted password matches
            if (existingUser != null && userService.decryptPassword(existingUser.getPassword()).equals(user.getPassword())) {
                // Successful login, check if user is an admin or regular user
                if ("ADMIN".equals(existingUser.getRole())) {
                    // Redirect admin to the admin dashboard
                    model.addAttribute("successMessage", "Admin login successful!");
                    return "redirect:/admin/dashboard"; // Admin dashboard URL
                } else {
                    // Redirect regular user to their dashboard
                    model.addAttribute("successMessage", "User login successful!");
                    return "redirect:/user-dashboard"; // User dashboard URL
                }
            } else {
                // Invalid credentials
                model.addAttribute("errorMessage", "Invalid username or password");
                return "login"; // Return to login page with error message
            }
        } catch (Exception e) {
            // Handle any exceptions during the login process
            model.addAttribute("errorMessage", "Error during login: " + e.getMessage());
            return "login"; // Return to login page with error message
        }
    }



    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) throws Exception {
        // Fetch all users from the service layer
        Iterable<User> users = userService.getAllUsers();

        // Decrypt passwords for each user
        users.forEach(user -> {
            try {
                String decryptedPassword = userService.decryptPassword(user.getPassword());
                user.setPassword(decryptedPassword);
            } catch (Exception e) {
                // Handle any decryption errors gracefully
                e.printStackTrace();
            }
        });

        // Add the user list to the model
        model.addAttribute("users", users);

        // Return the Thymeleaf template name
        return "admin-dashboard";
    }
}
