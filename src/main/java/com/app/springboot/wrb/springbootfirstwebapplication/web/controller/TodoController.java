package com.app.springboot.wrb.springbootfirstwebapplication.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.app.springboot.wrb.springbootfirstwebapplication.model.Todo;
import com.app.springboot.wrb.springbootfirstwebapplication.web.service.TodoService;

import java.util.Date;

@Controller
@SessionAttributes("name")
public class TodoController {
	
	@Autowired
	TodoService service;
		
	@RequestMapping(value="/todolist", method= RequestMethod.GET)
	public String showLoginPage(ModelMap model) {
		String name = (String) model.get("name");
		model.put("todos", service.retrieveTodos(name));
		return "todolist";
	}

	@RequestMapping(value="/add-todo", method= RequestMethod.GET)
	public String addTodo(ModelMap model) {	
		model.addAttribute("todo", new Todo(0,(String) model.get("name"), "", new Date(), false));
		return "add-todo";
	}
	
	@RequestMapping(value="/delete-todo", method= RequestMethod.GET)
	public String deleteTodo(@RequestParam int id) {
		service.deleteTodo(id);
		return "redirect:/todolist";
	}

	@RequestMapping(value="/add-todo", method= RequestMethod.POST)
	public String addTodo(ModelMap model, Todo todo) {	
		service.addTodo((String) model.get("name"), todo.getDesc(), new Date(), false);
		return "redirect:/todolist";
	}
}
