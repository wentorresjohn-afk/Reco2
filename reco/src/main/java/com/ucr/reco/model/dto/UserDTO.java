package com.ucr.reco.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDTO
{
    @NotBlank(message = "El nombre no puede venir en blanco")
    private String name;
    @Email(message = "El correo es invalido")
    @NotBlank(message = "El correo no puede quedar en blanco")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[*@#$%])[a-zA-Z0-9*@#$%]{8,20}$", //Regexp es una expresion regular que da las reglas de la contraseña
            message = "La contraseña debe tener:\n" +
                    "- Al menos una mayúscula\n" +
                    "- Al menos un número\n" +
                    "- Al menos un carácter especial (*@#$%)\n" +
                    "- Entre 8 y 20 caracteres")
    private String password;
    @NotBlank(message = "El rol no debe ir en blanco")
    private String role;

    public UserDTO() {

    }

    public UserDTO(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
