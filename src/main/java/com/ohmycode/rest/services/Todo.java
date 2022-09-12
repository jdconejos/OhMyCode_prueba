package com.ohmycode.rest.services;

import com.ohmycode.domain.controllers.TodoController;
import com.ohmycode.rest.DTOs.DTOTodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class Todo {

    private final int DEFAULT_PAGE_LENGTH = 10;
    @Autowired
    private TodoController todoController;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DTOTodo> getTodos (@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "pageLength", required = false) Integer pageLength) {

        //Si no hay parametros o no se indica la pagina (pageNumber), se devuelven todos los TODOs.
        //Si no se indica la longitud de la pagina (pageLength), pero si la pagina (pageNumber), se devuelve la pagina
        //  con un pageLength por defecto
        //
        //pageNumber empieza en 1

        try {
            if (pageNumber == null) return todoController.getAllTodos();

            // pageNumber != null
            if (pageLength == null) return todoController.getTodosWithPage(pageNumber - 1, DEFAULT_PAGE_LENGTH);

            // pageNumber != null && pageLength != null
            return todoController.getTodosWithPage(pageNumber - 1, pageLength);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTodo(){

    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void editTodos(){

    }

}
