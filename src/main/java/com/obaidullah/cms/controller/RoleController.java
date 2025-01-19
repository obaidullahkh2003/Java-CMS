package com.obaidullah.cms.controller;

import com.obaidullah.cms.dto.RoleDto;
import com.obaidullah.cms.dto.RoleResponse;
import com.obaidullah.cms.service.RoleService;
import com.obaidullah.cms.service.UserService;
import com.obaidullah.cms.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;




@Controller
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String CreateRoleScreen(@ModelAttribute("roleDto") @Validated RoleDto roleDto, BindingResult result, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() + " " + userService.getUserByEmail(authentication.getName()).getLastName();

        model.addAttribute("roleDto", roleDto);
        model.addAttribute("contentTemplate", "createrole");
        model.addAttribute("pageTitle", "Create Role");
        model.addAttribute("username", username);

        return "templet";
    }

    // Create a new role
    @PostMapping("/save")
    public String createRole(@ModelAttribute("roleDto") @Validated RoleDto roleDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("contentTemplate", "createrole");
            model.addAttribute("pageTitle", "Create Role");
            return "templet";
        }

        roleService.createRole(roleDto);
        return "redirect:/roles";
    }



    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable long id) {
        RoleDto roleDto = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDto);
    }

    @GetMapping
    public String getRolesList(
            Model model,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        // Fetch paginated and sorted user data
        RoleResponse roleResponse = roleService.getAllRolesList(pageNo, pageSize, sortBy, sortDir);

        // Log the data for debugging
        System.out.println("Roles data: " + roleResponse.getContent());

        // Get the currently logged-in user's name
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() + " " +
                userService.getUserByEmail(authentication.getName()).getLastName();

        // Populate model attributes for the user list view
        model.addAttribute("roles", roleResponse.getContent());
        model.addAttribute("contentTemplate", "rolelist");
        model.addAttribute("pageTitle", "Roles List");
        model.addAttribute("username", username);

        model.addAttribute("currentPage", roleResponse.getPageNo());
        model.addAttribute("totalPages", roleResponse.getTotalPages());
        model.addAttribute("totalUsers", roleResponse.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", pageSize);

        return "templet";
    }



    @GetMapping("/edit/{id}")
    public String editRoleScreen(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() + " " + userService.getUserByEmail(authentication.getName()).getLastName();

        model.addAttribute("roleDto", roleService.getRoleById(id));
        model.addAttribute("contentTemplate", "updaterole");
        model.addAttribute("pageTitle", "Update Role");
        model.addAttribute("username", username);

        return "templet";
    }


    @PostMapping("/{id}")
    public String updateRole(@PathVariable("id") long id, @ModelAttribute("roleDto") @Validated RoleDto roleDto, BindingResult result) {
        if (result.hasErrors()) {
            return "templet";
        }
        roleService.updateRole(id, roleDto);
        return "redirect:/roles";
    }

    @GetMapping("/view/{id}")
    public String viewRole(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.getUserByEmail(authentication.getName()).getFirstName() + " " + userService.getUserByEmail(authentication.getName()).getLastName();

        model.addAttribute("roleDto", roleService.getRoleById(id));
        model.addAttribute("contentTemplate", "viewrole");
        model.addAttribute("pageTitle", "View Role");
        model.addAttribute("username", username);

        return "templet";
    }


    // Delete a role
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable long id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
