package com.ucr.reco.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO
{
    @Email(message = "Correo invalido")//Valida que el siguiente atributo tenga formato de correo.
    @NotBlank(message = "No puede dejar el correo en blanco")
    private String email;
    @NotBlank(message = "No puede dejar el password en blanco")//Que no tenga valores en blanco.
    private String password;

    public LoginDTO()
    {
    }

    public LoginDTO(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
