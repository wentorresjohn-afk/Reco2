package com.ucr.reco.service;

import com.ucr.reco.model.User;
import com.ucr.reco.model.dto.LoginDTO;
import com.ucr.reco.model.dto.UserDTO;
import com.ucr.reco.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserJpaRepository repository;

    public List<User> findAll()
    {
        //Lista todos los objetos que hay en user
        return repository.findAll();
    }

    /*
        public User add(User user){
            if(repository.existsByEmail(user.getEmail())){
                return null;
            }
            return repository.save(user);
        }
    */
    public User add(UserDTO user) {
        // 1. Validar si el correo ya existe
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        // 2. Validar que no vengan campos nulos o vacíos
        if (user.getName() == null || user.getName().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getRole() == null || user.getRole().trim().isEmpty()) {

            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        // 3. Si todo está bien, pasamos los datos del DTO a la entidad real
        User userTemp = new User();
        userTemp.setName(user.getName());
        userTemp.setEmail(user.getEmail());
        userTemp.setPassword(user.getPassword());
        userTemp.setRole(user.getRole());

        return repository.save(userTemp); // Guarda en Neon y lo retorna
    }

    public User getById(Integer id)
    {
        Optional<User> user = repository.findById(id); // findById hereda de JpaRepository, retorna Optional<User>
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User update(User user)
    {
        User userExits = repository.getByEmail(user.getEmail());
        if (userExits != null)
        {
            if (user.getName() != null)
            {
                userExits.setName(user.getName());
            }
            if (user.getPassword() != null)
            {
                userExits.setPassword(user.getPassword());
            }
            if (user.getRole() != null)
            {
                userExits.setRole(user.getRole());
            }

        } else
        {
            return null;
        }
        return repository.save(userExits);
    }

    public User delete(Integer id)
    {
        Optional<User> userExits = repository.findById(id);//Hace que pueda tener información o pueda estar vacío

        if (userExits.isPresent()) {
            repository.deleteById(id);
            return (User) userExits.get();//
        } else {
            return null;
        }
    }

    public User changePassword(String email, String newPassword)
    {
        User userExits = repository.getByEmail(email);
        if (userExits != null) {
            userExits.setPassword(newPassword);
            return repository.save(userExits);
        } else {
            return null;
        }
    }

    public User login(LoginDTO login)
    {
        //Valida si el email y la contraseña existen
        if (repository.existsByEmail(login.getEmail()) && repository.existsByPassword(login.getPassword()))
        {
            //Si es así, crea un objeto llamado userTemp de tipo user, que guardará la info del correo y contraseña que ingrese el usuario
            User userTemp = new User(login.getEmail() , login.getPassword());
            return userTemp;

        }
        return null;
    }

    public User getUserByEmail(String email)
    {
        return repository.getByEmail(email);
    }


}
