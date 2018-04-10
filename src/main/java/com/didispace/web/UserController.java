package com.didispace.web;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.didispace.domain.User;
import com.didispace.domain.UserRepository;

@Controller
@ComponentScan

public class UserController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String userListPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("users", (ArrayList<User>)userRepository.findAll());
		return "index";
	}
	
	@RequestMapping("user/add")
	public String addUser(User user) {
		return "addUser";
	}
	
    @RequestMapping("user/save")
    public String saveUser(@ModelAttribute("user") User user,
    		final RedirectAttributes redirectAttributes) {
    	if(userRepository.save(user)!=null) {
    		redirectAttributes.addFlashAttribute("save", "success");
    	}else {
    		redirectAttributes.addFlashAttribute("save", "unsuccess");
    	}
    	return "redirect:/";
    }
    @RequestMapping(value="/user/{operation}/{id}", method=RequestMethod.GET)
    public String editRemoveUser(@PathVariable("operation") String operation,
    		@PathVariable("id") String id, User user, final RedirectAttributes redirectAttributes,
    		Model model) {
    	if(operation.equals("delete")) {
        	try {
            	userRepository.delete(id);
            	}
            	catch (Exception ex) {
            		redirectAttributes.addFlashAttribute("delete", "unsuccess");
            		}
            		redirectAttributes.addFlashAttribute("delete", "success");
    	}else if (operation.equals("edit")) {
    		User eUser = userRepository.findById(id);
    		if (eUser!=null) {
    			model.addAttribute("editUser", eUser);
    			return "editUser";
    		} else {
    			redirectAttributes.addFlashAttribute("status", "notfound");
    		}
    	}
    	return "redirect:/";
    }
    
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("editUser") User eUser,
    		final RedirectAttributes redirectAttributes) {
    	try {
    		User user = userRepository.findById(eUser.getId());
    		user.setName(eUser.getName());
    		user.setAge(eUser.getAge());    		
    	}
    	catch (Exception ex) {
    		redirectAttributes.addFlashAttribute("edit", "unsuccess");
    		
    	}
    	redirectAttributes.addFlashAttribute("edit", "success");
    	return "redirect:/";
    }
}
   