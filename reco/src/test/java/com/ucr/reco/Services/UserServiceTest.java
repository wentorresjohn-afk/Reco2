package com.ucr.reco.Services;

import com.ucr.reco.model.User;
import com.ucr.reco.model.dto.UserDTO;
import com.ucr.reco.repository.UserJpaRepository;
import com.ucr.reco.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)//Indica que usara Mockito durante la prueba
public class UserServiceTest
{
    @Mock//Crea un repositorio simulado para no consultar la base de datos real
    private UserJpaRepository repository;

    @InjectMocks//Instancia a User e inyecta el repositorio simulado
    private UserService service;

    @Test // FIN ALL
    void findAll_deberiaRetornarListaDeUsuarios()
    {
        // Preparamos los datos falsos que "devolverá" el repositorio simulado
        User user1 = new User(1, "Juan", "juan@mail.com", "1234", "ADMIN");
        User user2 = new User(2, "Maria", "maria@mail.com", "5678", "USER");
        List<User> usuariosFalsos = Arrays.asList(user1, user2);

        // Aqui le dice al mock que si alguien llama al findAll, que no use la base de datos real, si no que devuelva la lista que se preparó aca1
        when(repository.findAll()).thenReturn(usuariosFalsos);

        // Ejecutamos el método real del service
        List<User> resultado = service.findAll();

        // Comprobamos que el resultado es el esperado
        assertEquals(2, resultado.size());
        // Aqui le dice que acceda a la info que está en la posición 0
        assertEquals("Juan", resultado.get(0).getName());

        // Confirmamos que el repositorio fue llamado exactamente 1 vez, y que cumpla el camino correcto
        verify(repository, times(1)).findAll();
    }// Fin de la prueba

    @Test // GET BY ID
    void getById_deberiaRetornarUsuario_cuandoExiste()
    {
        // Crea un usuario falso
        User userFalso = new User(1, "Juan", "juan@mail.com", "1234", "ADMIN");

        // Le dice a mockito que no se vaya la base de datos, solo entrega optional que contiene el user falso
        when(repository.findById(1)).thenReturn(Optional.of(userFalso));

        User resultado = service.getById(1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getName());

        // Confirma que llamo a repository.findbyid solo una vez
        verify(repository, times(1)).findById(1);
    }// Fin de la prueba

    @Test
    void getById_deberiaRetornarNull_cuandoNoExiste()
    {
        // EL 99 es solo un ejemplo de un id que no existe
        when(repository.findById(99)).thenReturn(Optional.empty());

        User resultado = service.getById(99);
        // Ejecuta el metodo

        // Comprueba que sea null
        assertNull(resultado);


        verify(repository, times(1)).findById(99);
    }// Fin de la prueba

    @Test //ADD
    void add_deberiaGuardarUsuario_cuandoDatosSonValidos()
    {
        // Se crea un dto falso con datos validos
        UserDTO dto = new UserDTO();
        dto.setName("Pedro");
        dto.setEmail("pedro@mail.com");
        dto.setPassword("clave123");
        dto.setRole("USER");

        // Simula que verifica si el correo existe, y la respuesta sea falso
        when(repository.existsByEmail("pedro@mail.com")).thenReturn(false);
        // El save real recibe un User (no el DTO), por eso usamos any(User.class)
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecuta el metodo
        User resultado = service.add(dto);

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getName());

        // Verifica que el metodo solo se haya llamado una vez
        verify(repository, times(1)).existsByEmail("pedro@mail.com");
        // Verifica si se guardó
        verify(repository, times(1)).save(any(User.class));
    }// Fin de la prueba

    @Test
    void add_deberiaLanzarExcepcion_cuandoEmailYaExiste()
    {
        // Se crea un dto falso con datos validos nuevamente
        UserDTO dto = new UserDTO();
        dto.setName("Pedro");
        dto.setEmail("pedro@mail.com");
        dto.setPassword("clave123");
        dto.setRole("USER");

        // Llama al metodo existsByEmail y verifica si el correo ya existe, y le contestan que si
        when(repository.existsByEmail("pedro@mail.com")).thenReturn(true);

        // Comprobamos que se lance la excepción esperada
        // Normalmente si un metodo lanza una excepcion, y no se atrapa, explota el programa
        // assetThrows hace que en casos donde se espera que algo explore, comprueba si lo hizo, sin afectar el test
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.add(dto)
        );

        //Se lanza un mensaje con el error
        assertEquals("El correo electrónico ya está registrado.", ex.getMessage());

        // Si el correo ya existe, save() NUNCA debería llamarse
        verify(repository, times(1)).existsByEmail("pedro@mail.com");
        verify(repository, never()).save(any(User.class));
    }

    @Test //DELETE
    void delete_deberiaEliminarUsuario_cuandoExiste()
    {
        //Crea un usuario falso
        User userFalso = new User(1, "Juan", "juan@mail.com", "1234", "ADMIN");

        when(repository.findById(1)).thenReturn(Optional.of(userFalso));
        // deleteById no retorna nada (void), no se "mockea" con when()

        User resultado = service.delete(1);

        assertNotNull(resultado);
        assertEquals("juan@mail.com", resultado.getEmail());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void delete_deberiaRetornarNull_cuandoNoExiste() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        User resultado = service.delete(99);

        assertNull(resultado);

        verify(repository, times(1)).findById(99);
        // Si no existe, deleteById NUNCA debería ejecutarse
        verify(repository, never()).deleteById(anyInt());
    }


}
