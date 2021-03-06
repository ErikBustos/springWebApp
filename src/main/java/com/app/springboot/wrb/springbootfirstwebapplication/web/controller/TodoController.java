package com.app.springboot.wrb.springbootfirstwebapplication.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.app.springboot.wrb.springbootfirstwebapplication.model.Todo;
import com.app.springboot.wrb.springbootfirstwebapplication.web.service.TodoService;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

@Controller
public class TodoController {
	
	@Autowired
	TodoService service;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date - dd/MM/yyyy
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}
		
	@RequestMapping(value="/todolist", method= RequestMethod.GET)
	public String showLoginPage(ModelMap model) {
		String name = getLoggedinUserName();
		model.put("todos", service.retrieveTodos(name));
		return "todolist";
	}

	private String getLoggedinUserName() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if(principal instanceof UserDetails) {
			return ((UserDetails)principal).getUsername();
		}
		return principal.toString();
	}	

	@RequestMapping(value="/add-todo", method= RequestMethod.GET)
	public String addTodo(ModelMap model) {	
		model.addAttribute("todo", new Todo(0,getLoggedinUserName(), "Default Description", new Date(), false));
		return "add-todo";
	}
	
	@RequestMapping(value="/delete-todo", method= RequestMethod.GET)
	public String deleteTodo(@RequestParam int id) {
		service.deleteTodo(id);
		return "redirect:/todolist";
	}

	@RequestMapping(value="/update-todo", method= RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {	
		Todo todo = service.retrieveTodo(id);
		model.put("todo", todo);
		return "add-todo";
	}
	
	@RequestMapping(value="/update-todo", method= RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult bindingResult) {	
		
		if(bindingResult.hasErrors()) {
			return "add-todo";
		}
		todo.setUser(getLoggedinUserName());
		
		service.updateTodo(todo);
		
		return "redirect:/todolist";
	}
	
	@RequestMapping(value="/add-todo", method= RequestMethod.POST)
	public String addTodo(ModelMap model, @Valid Todo todo, BindingResult bindingResult) {	
		if(bindingResult.hasErrors()) {
			return "add-todo";
		}
		service.addTodo(getLoggedinUserName(), todo.getDesc(), todo.getTargetDate(), false);
		return "redirect:/todolist";
	}
}
