package com.policiaciudad.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity // Permite guardar objetos de esta clase mediante JPA
@Table(name = "personas") // Nombre de la tabla en la base de datos
@Inheritance(strategy = InheritanceType.JOINED) // Separa los datos comunes de los propios de cada subclase
public class Persona {

    @Id // Identifica la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera el ID al guardar
    private Integer id; // Integer permite que el identificador sea null mientras la persona todavía no fue guardada

    @Column(name = "nombre_completo", nullable = false) // Columna que no admite NULL
    private String nombreCompleto; // private protege los atributos; se consultan mediante los métodos get

    @Column(length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column(length = 8)
    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 254)
    private String mail;

    @Column(length = 30)
    private String telefono;

    public Persona() {
        // Constructor necesario para JPA.
    }

    public Persona(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Persona(String nombre, String apellido, String dni,
            LocalDate fechaNacimiento, String mail, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.mail = mail;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
