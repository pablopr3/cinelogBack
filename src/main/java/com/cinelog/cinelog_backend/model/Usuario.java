package com.cinelog.cinelog_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(unique = true, nullable = false)
    private String email;

    private String contrasena;

    private String imagenPerfil;

    // Indica si la cuenta del usuario está activa o no.
    // Por defecto está en `false` cuando el usuario se registra, y se cambia a `true` solo cuando confirma su correo.
    private boolean activo;


    // Token generado automáticamente al registrarse.
    // Se usa para verificar que el usuario hizo clic en el enlace del email de activación.
    // Se borra (se pone en null) una vez que el usuario activa su cuenta.
    private String tokenActivacion;



    // Token generado cuando el usuario solicita recuperar su contrasena.
    // Se manda por correo para permitirle acceder a una página donde la puede cambiar.
    private String tokenRecuperacion;

    // Fecha y hora de expiración del token de recuperación.
    // Se valida cuando el usuario intenta cambiar su contrasena.
    // Si esta fecha ya pasó, el token ya no es válido.
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpira;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EntradaDiario> entradas;

}
