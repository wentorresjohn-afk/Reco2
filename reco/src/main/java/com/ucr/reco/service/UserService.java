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
    public User add(UserDTO user)
    {
        //Valida si el correo existe
        if (repository.existsByEmail(user.getEmail()))
        {
            return null;//Si el correo existe, no permite guardar un usuario nuevo con el mismo correo
        } else //Si no existe
        {
            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null)
            {
                //Valida si todos los requerimientos se cumplen, si no, retorna null
                return null;
            }
        }
        User userTemp = new User();//Define una referencia nueva de tipo user
        //La informacion que trae el usuario dto se los damos al userTemp
        userTemp.setName(user.getName());
        userTemp.setEmail(user.getEmail());
        userTemp.setPassword(user.getPassword());
        userTemp.setRole(user.getRole());
        return repository.save(userTemp);//Guarda el nuevo usuario y lo retorna
    }

    public User getById(Integer id)
    {
        User user = repository.findById(id.intValue());//intValue obtiene el valor del integer
        if (user != null) {
            return user;
        }
        /*if (repository.existsById(id)) {
            return repository.findById(id).get();
        }*/
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
