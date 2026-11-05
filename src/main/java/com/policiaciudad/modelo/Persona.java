package com.policiaciudad.modelo;

import com.policiaciudad.exception.DatosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

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
    
    protected Persona() {
        // Constructor vacío que JPA necesita para reconstruir objetos desde la base de datos
    }

    public Persona(String nombreCompleto) {
        cambiarNombre(nombreCompleto); // El constructor recibe el nombre y utiliza cambiarNombre para validarlo
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void cambiarNombre(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) { // isBlank detecta texto vacío o formado únicamente por espacios
            throw new DatosInvalidosException( // Rechaza un nombre inválido
                    "El nombre completo es obligatorio."
            );
        }

        // this.nombreCompleto identifica el atributo del objeto
        this.nombreCompleto = nombreCompleto.trim(); // trim elimina espacios al principio y al final
    }

    public LocalDate getFechaNacimiento() {//permite consultar la fecha guardada
        return fechaNacimiento;
    }


    
    public void actualizarDatosPersonales(
            String nombre,
            String apellido,
            String dni,
            LocalDate fechaNacimiento,
            String mail,
            String telefono) {
//comprueba campos obligatorios, elimina espacios de los extremos y controla la longitud
        String nombreValidado = validarTexto(nombre, "nombre", 100);
        String apellidoValidado = validarTexto(apellido, "apellido", 100);
        String dniValidado = validarTexto(dni, "DNI", 8);
        String mailValidado = validarTexto(mail, "mail", 254);
        String telefonoValidado = validarTexto(telefono, "teléfono", 30);
//comprueba el formato del DNI y un formato basico de mail
        if (!dniValidado.matches("[0-9]{8}")) {
            throw new DatosInvalidosException(
                    "El DNI debe contener exactamente 8 dígitos.");
        }

        LocalDate fechaActual = LocalDate.now(
                ZoneId.of("America/Argentina/Buenos_Aires"));

        if (fechaNacimiento == null
                || fechaNacimiento.isAfter(fechaActual)) { //detecta una fecha de nacimiento futura
            throw new DatosInvalidosException(
                    "La fecha de nacimiento es obligatoria y no puede ser futura.");
        }

        if (!mailValidado.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new DatosInvalidosException(
                    "El formato del mail no es válido.");
        }

        this.nombre = nombreValidado;
        this.apellido = apellidoValidado;
        this.dni = dniValidado;
        this.fechaNacimiento = fechaNacimiento;
        this.mail = mailValidado;
        this.telefono = telefonoValidado;

        cambiarNombre(nombreValidado + " " + apellidoValidado); //mantiene actualizado nombreCompleto
    }

    private static String validarTexto(
            String valor, String campo, int longitudMaxima) {

        if (valor == null || valor.isBlank()) {
            throw new DatosInvalidosException(
                    "El campo " + campo + " es obligatorio.");
        }

        String texto = valor.trim();

        if (texto.length() > longitudMaxima) {
            throw new DatosInvalidosException(
                    "El campo " + campo + " admite hasta "
                    + longitudMaxima + " caracteres.");
        }

        return texto;
    }
//permiten consultar los atributos privados
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getMail() {
        return mail;
    }

    public String getTelefono() {
        return telefono;
    }
    public Integer calcularEdad() {
        return calcularEdad(LocalDate.now(
                ZoneId.of("America/Argentina/Buenos_Aires")));
    }

    public Integer calcularEdad(LocalDate fechaReferencia) {
        if (fechaReferencia == null) {
            throw new DatosInvalidosException("La fecha de referencia es obligatoria.");
        }
        if (fechaNacimiento == null) {
            return null; // Datos históricos pendientes de completar.
        }
        if (fechaReferencia.isBefore(fechaNacimiento)) {
            throw new DatosInvalidosException("La fecha de referencia precede al nacimiento.");
        }
        return Period.between(fechaNacimiento, fechaReferencia).getYears();
    }
}
