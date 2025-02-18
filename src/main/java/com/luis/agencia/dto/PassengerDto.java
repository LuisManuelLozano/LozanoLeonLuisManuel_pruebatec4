package com.luis.agencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa la información de un pasajero.
 * Incluye el identificador, nombre, apellido y DNI del pasajero.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {

    /**
     * Identificador único del pasajero.
     */
    private Long id;

    /**
     * Nombre del pasajero.
     * No puede estar vacío y su longitud máxima es de 100 caracteres.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    /**
     * Apellido del pasajero.
     * No puede estar vacío y su longitud máxima es de 100 caracteres.
     */
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    private String lastName;

    /**
     * Documento Nacional de Identidad (DNI) del pasajero.
     * No puede estar vacío y su longitud debe estar entre 7 y 20 caracteres.
     */
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 7, max = 20, message = "El DNI debe tener entre 7 y 20 caracteres")
    private String dni;
}

