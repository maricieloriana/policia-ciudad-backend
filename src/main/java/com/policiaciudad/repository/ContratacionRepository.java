package com.policiaciudad.repository;

import com.policiaciudad.modelo.Contratacion;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository proporciona operaciones de persistencia para Contratacion
public interface ContratacionRepository
        extends JpaRepository<Contratacion, Integer> {

    boolean existsByCodigoSucursalAndIdVigilanteAndFecha(
            int codigoSucursal,
            int idVigilante,
            LocalDate fecha
    ); // Comprueba si existe esa combinación; devuelve true o false
    // Obtiene únicamente las contrataciones del vigilante indicado
    List<Contratacion> findByIdVigilante(int idVigilante);
    // Comprueba si la sucursal tiene contrataciones registradas
    boolean existsByCodigoSucursal(int codigoSucursal);
    // Comprueba si el vigilante tiene contrataciones registradas
    boolean existsByIdVigilante(int idVigilante);
}//Spring Data JPA generará una consulta que filtre por el atributo idVigilante. 
//Devuelve una lista vacía si ese vigilante no tiene contrataciones.