package com.cinelog.cinelog_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    @ManyToOne(optional = false)
    private Pelicula pelicula;

    private LocalDateTime fechaAgregado;

    private boolean visto;

    private boolean favorito;

    private int puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;


}
