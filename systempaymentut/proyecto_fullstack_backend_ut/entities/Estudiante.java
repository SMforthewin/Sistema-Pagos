package com.systempaymentut.proyecto_fullstack_backend_ut.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder //permitir construir objetos de esta clase con el patrón builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {
    //public, private, protected
    //metodos accesores - get obtener - set establecer

    @Id
    private String id;

    private String nombre;
    private String apellido;

    @Column (unique = true)
    private String codigo;
    
    private String programaId;

    private String foto;

}
