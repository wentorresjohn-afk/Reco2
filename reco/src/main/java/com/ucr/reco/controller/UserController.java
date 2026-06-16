package com.ucr.reco.controller;

import com.ucr.reco.model.User;
import com.ucr.reco.model.dto.LoginDTO;
import com.ucr.reco.model.dto.UserDTO;
import com.ucr.reco.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/all")
    public ResponseEntity<List<?>> getAll()
    {
        List<User> users = service.findAll();//Obtiene todos los datos de la base de datos
        if (users.isEmpty())//Si la lista está vacía
        {
            return ResponseEntity.noContent().build();//Responde con "error"
        }
        return ResponseEntity.ok(users);//Si no se cumple ese condicional, imprime la lista
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        User user = service.getById(id);
        if (user == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody UserDTO user, BindingResult result) // Valida si se cumple, BindingResult valida los errores
    {
        // 1. Mantiene tu validación nativa de anotaciones de Spring (@NotNull, @Email, etc.)
        if (result.hasErrors())
        {
            List<String> errors = new ArrayList<>();
            for (ObjectError error: result.getAllErrors())
            {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // 2. Intenta registrar el usuario atrapando el mensaje exacto del Service
        try
        {
            service.add(user);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        }
        catch (IllegalArgumentException e)
        {
            // Atrapa el texto "El correo electrónico ya está registrado." o "Todos los campos son obligatorios."
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody User user) {
        if (service.update(user) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        if (service.delete(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

    @PatchMapping("/change/{email}")
    public ResponseEntity<?> changePassword(@PathVariable String email, @RequestBody User user) {
        User updatedUser = service.changePassword(email, user.getPassword());
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }

    @GetMapping("/login")
    //BindingResult result sirve para guardar los errores que se encontraron con el valid
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO login, BindingResult result)
    {
        if (result.hasErrors()) //Si resultado tiene errores
        {
            List<String> errors = new ArrayList<>();//Crea una lista vacía de errores
            for (ObjectError error : result.getAllErrors())//Cada que valid detecta un error, crea un objeto de tipo objectError
            {
                errors.add(error.getDefaultMessage());//Saca el mensaje de cada error y lo almacena en la lista
            }
            //Devuelve un 400 Bad Request con la lista de mensajes de error en el body.
            return ResponseEntity.badRequest().body(errors);
        }
        //Llama al servicio con los datos del login. Si retorna null significa que el correo no existe o la contraseña es incorrect
        if (service.login(login) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo no está registrado o la contraseña es incorrecta");
        }
        //Si todo sale bien
        return ResponseEntity.ok("Inició sesión correctamente");
    }

}
