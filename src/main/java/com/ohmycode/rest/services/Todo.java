package com.ohmycode.rest.services;

import com.ohmycode.domain.controllers.TodoController;
import com.ohmycode.rest.DTOs.DTOTodo;
import com.ohmycode.rest.DTOs.TodoApiBody;
import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/todos")
public class Todo {

    private final int DEFAULT_PAGE_LENGTH = 10;
    @Autowired
    private TodoController todoController;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DTOTodo> getTodos (@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "pageLength", required = false) Integer pageLength,
                                   @RequestParam(value = "username", required = false) String username) {

        //Si no hay parametros o no se indica la pagina (pageNumber), se devuelven todos los TODOs.
        //Si no se indica la longitud de la pagina (pageLength), pero si la pagina (pageNumber), se devuelve la pagina
        //  con un pageLength por defecto
        //
        //pageNumber empieza en 1

        try {
            if (pageNumber == null) {
                if(username == null) return todoController.getAllTodos();
                else return todoController.getTodosByUsername(username);
            }
            // pageNumber != null
            if (pageLength == null) return todoController.getTodosWithPage(pageNumber - 1, DEFAULT_PAGE_LENGTH, username);

            // pageNumber != null && pageLength != null
            return todoController.getTodosWithPage(pageNumber - 1, pageLength, username);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DTOTodo createTodo(@RequestBody TodoApiBody body){
        //Revisar que userId > 0, que el titulo no esté vacío o con longitud >= 200, y que todos los parametros existen
        if(body == null || body.getUserId() == null || body.getUserId() <= 0
                || body.getTitle() == null || body.getTitle().length() <= 0 || body.getTitle().length() >= 200
                || body.getCompleted() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return todoController.createTodo(body.getTitle(), body.getCompleted(), body.getUserId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DTOTodo editTodo(@RequestBody TodoApiBody body){
        //Revisar que userId > 0; y que el titulo no esté vacío o con longitud >= 200, y que todos los parametros existen
        if(body == null || body.getUserId() == null || body.getUserId() <= 0
                || body.getTitle() == null || body.getTitle().length() <= 0 || body.getTitle().length() >= 200
                || body.getCompleted() == null || body.getId() == null || body.getId() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return todoController.editTodo(body.getId(), body.getTitle(), body.getCompleted(), body.getUserId());

    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteTodo(@RequestParam(value="id") Long id){
        todoController.deleteTodo(id);
    }

}
