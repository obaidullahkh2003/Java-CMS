package com.obaidullah.cms.controller;

import com.obaidullah.cms.dto.RoleDto;
import com.obaidullah.cms.dto.UserDto;
import com.obaidullah.cms.dto.UserResponse;
import com.obaidullah.cms.model.Roles;
import com.obaidullah.cms.service.RoleService;
import com.obaidullah.cms.service.UserService;
import com.obaidullah.cms.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // Get list of users with pagination and sorting
    @GetMapping("/users")
    public String getUsersList(
            Model model,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        // Fetch paginated and sorted user data
        UserResponse userResponse = userService.getAllUsers(pageNo, pageSize, sortBy, sortDir);

        // Get the currently logged-in user's name
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() + " " +
                userService.getUserByEmail(authentication.getName()).getLastName();

        // Populate model attributes for the user list view
        model.addAttribute("users", userResponse.getContent());
        model.addAttribute("contentTemplate", "userslist");
        model.addAttribute("pageTitle", "User List");
        model.addAttribute("username", username);

        model.addAttribute("currentPage", userResponse.getPageNo());
        model.addAttribute("totalPages", userResponse.getTotalPages());
        model.addAttribute("totalUsers", userResponse.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", pageSize);

        return "templet";
    }

    @GetMapping("/users/view/{id}")
    public String viewUser(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.getUserById(id);
        List<RoleDto> roles = roleService.getAllRoles();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() +
                " " + userService.getUserByEmail(authentication.getName()).getLastName();

        List<Long> userRoleIds = userDto.getRoles().stream()
                .map(role -> role.getId())
                .collect(Collectors.toList());

        model.addAttribute("userDto", userDto);
        model.addAttribute("roles", roles);
        model.addAttribute("userRoleIds", userRoleIds);
        model.addAttribute("contentTemplate", "userview");
        model.addAttribute("pageTitle", "Create User");
        model.addAttribute("username", username);

        return "templet";
    }



    @GetMapping("/users/create")
    public String CreateUserScreen(@ModelAttribute("user") @Validated UserDto userDto, BindingResult result, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=userService.getUserByEmail(authentication.getName()).getFirstName() +" "+userService.getUserByEmail(authentication.getName()).getLastName();
        List<RoleDto> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("userDto", userDto);
        model.addAttribute("contentTemplate", "createuser");
        model.addAttribute("pageTitle", "Create User");
        model.addAttribute("username", username);
        return "templet";
    }

    // Handle user creation
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.createUser(userDto);
        return "redirect:/users";
    }

    // Handle user editing page
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() + " " + userService.getUserByEmail(authentication.getName()).getLastName();
        UserDto userDto = userService.getUserById(id);
        List<RoleDto> roles = roleService.getAllRoles();
        List<Long> userRoleIds = userDto.getRoles().stream()
                .map(Roles::getId)
                .collect(Collectors.toList());
        // Add model attributes
        model.addAttribute("roles", roles);
        model.addAttribute("userDto", userDto);
        model.addAttribute("userRoleIds", userRoleIds);
        model.addAttribute("contentTemplate", "updateuser");
        model.addAttribute("pageTitle", "Update User");
        model.addAttribute("username", username);

        return "templet";
    }


    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("userDto") @Validated UserDto userDto,
                             BindingResult result,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "confirmPassword", required = false) String confirmPassword) {

        if (result.hasErrors()) {
            return "templet";
        }

        if (password != null && !password.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                result.rejectValue("password", "password.mismatch", "Passwords do not match.");
                return "templet";
            }
            userDto.setPassword(password);
        }

        userService.updateUser(id, userDto);

        return "redirect:/users";
    }




    // Handle user deletion
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

}
