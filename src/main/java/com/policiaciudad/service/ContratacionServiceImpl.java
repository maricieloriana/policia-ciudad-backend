package com.policiaciudad.service;

import com.policiaciudad.dto.ContratacionDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Contratacion;
import com.policiaciudad.repository.ContratacionRepository;
import com.policiaciudad.repository.SucursalRepository;
import com.policiaciudad.repository.VigilanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.modelo.Rol;
import com.policiaciudad.modelo.Usuario;
import com.policiaciudad.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;

@Service // Spring registra el servicio e inyecta sus repositorios
//que implementa el contrato y coordina las validaciones del registro
public class ContratacionServiceImpl implements ContratacionService { //cumple el contrato de la interfaz
//final : impide reemplazar esas referencias después de construir el servicio
    private final ContratacionRepository repositorio;
    private final SucursalRepository sucursalRepository;
    private final VigilanteRepository vigilanteRepository;
    private final UsuarioRepository usuarioRepository;

    public ContratacionServiceImpl(
        ContratacionRepository repositorio,
        SucursalRepository sucursalRepository,
        VigilanteRepository vigilanteRepository,
        UsuarioRepository usuarioRepository) {

        this.repositorio = repositorio;
        this.sucursalRepository = sucursalRepository;
        this.vigilanteRepository = vigilanteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override // indica que implementa su método
    @Transactional // Ejecuta el registro dentro de una transacción
    //Primero comprueba los datos recibidos. Al crear Contratacion, reutiliza sus validaciones de identificadores positivos y fecha obligatoria.
    public ContratacionDTO registrar(ContratacionDTO datos) {

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos de contratacion son obligatorios."
            );
        }

        if (datos.getId() != null) {
            throw new DatosInvalidosException(
                    "El ID se asigna al registrar la contratacion."
            );
        }

        Contratacion contratacion = new Contratacion(
                datos.getCodigoSucursal(),
                datos.getIdVigilante(),
                datos.getFecha(),
                datos.isConArma()
        );
//Después verifica la sucursal, el vigilante y los duplicados.
        if (!sucursalRepository.existsById(
                contratacion.getCodigoSucursal())) {

            throw new RecursoNoEncontradoException(
                    "La sucursal no existe."
            );
        }

        if (!vigilanteRepository.existsById(
                contratacion.getIdVigilante())) {

            throw new RecursoNoEncontradoException(
                    "El vigilante no existe."
            );
        }

        if (repositorio.existsByCodigoSucursalAndIdVigilanteAndFecha(
                contratacion.getCodigoSucursal(),
                contratacion.getIdVigilante(),
                contratacion.getFecha())) {

            throw new RegistroDuplicadoException(
                    "Ya existe una contratacion para esa sucursal, "
                    + "vigilante y fecha."
            );
        }

        Contratacion guardada = repositorio.save(contratacion);

        return new ContratacionDTO(
                guardada.getId(),
                guardada.getCodigoSucursal(),
                guardada.getIdVigilante(),
                guardada.getFecha(),
                guardada.isConArma()
        );
    }
    @Override // readonly indica que la transacción está destinada a consulta
    @Transactional(readOnly = true) // Consulta datos sin realizar modificaciones
    public List<ContratacionDTO> listar() {
//findall es una operación heredada de JpaRepository que obtiene todas las contrataciones
        return repositorio.findAll() 
                .stream() // Recorre las contrataciones obtenidas; permite procesar los elementos de la colección
                .map(contratacion -> new ContratacionDTO(
                        contratacion.getId(),
                        contratacion.getCodigoSucursal(),
                        contratacion.getIdVigilante(),
                        contratacion.getFecha(),
                        contratacion.isConArma()
                )) // Convierte cada entidad en un DTO
                .toList(); // Reúne los DTO en una lista
    }//map transforma
    //Finalmente guarda el modelo y lo convierte en el DTO de respuesta
    @Override
    @Transactional(readOnly = true) // Solo consulta datos
    public List<ContratacionDTO> listarPropias(String nombreUsuario) {

        // Busca la cuenta cuyo nombre recibiremos desde la autenticación
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new AccessDeniedException(
                        "La cuenta no esta disponible."
                ));

        // Esta operación corresponde únicamente a cuentas de vigilantes
        if (usuario.getRol() != Rol.VIGILANTE) {
            throw new AccessDeniedException(
                    "La consulta propia requiere el rol VIGILANTE."
            );
        }

        // El titular de la cuenta debe existir como vigilante
        if (!vigilanteRepository.existsById(usuario.getIdPersona())) {
            throw new AccessDeniedException(
                    "La cuenta no esta asociada a un vigilante."
            );
        }

        // Filtra utilizando el ID del titular, no el ID de la cuenta
        return repositorio.findByIdVigilante(usuario.getIdPersona())
                .stream()
                .map(contratacion -> new ContratacionDTO(
                        contratacion.getId(),
                        contratacion.getCodigoSucursal(),
                        contratacion.getIdVigilante(),
                        contratacion.getFecha(),
                        contratacion.isConArma()
                ))
                .toList();
    }//busca la cuenta, comprueba que corresponda a un vigilante y filtra por su idPersona. AccessDeniedException pertenece a Spring Security y representa una denegación de acceso.
    @Override
    @Transactional(readOnly = true)
    public ContratacionDTO consultarPorId(int id) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la contratacion debe ser positivo.");
        }

        // Busca la contratación o informa que no existe
        Contratacion contratacion = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una contratacion con el identificador "
                        + id + "."));

        return new ContratacionDTO(
                contratacion.getId(),
                contratacion.getCodigoSucursal(),
                contratacion.getIdVigilante(),
                contratacion.getFecha(),
                contratacion.isConArma());
    }
    @Override
    @Transactional
    public ContratacionDTO actualizarCondicionArma(int id, Boolean conArma) {

        // Valida el identificador y la presencia del dato
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la contratacion debe ser positivo.");
        }

        if (conArma == null) {
            throw new DatosInvalidosException(
                    "La condicion de arma es obligatoria.");
        }

        // Busca la contratación que se quiere actualizar
        Contratacion contratacion = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una contratacion con el identificador "
                        + id + "."));

        // Modifica únicamente la condición de arma
        contratacion.cambiarCondicionArma(conArma);

        Contratacion actualizada = repositorio.save(contratacion);

        return new ContratacionDTO(
                actualizada.getId(),
                actualizada.getCodigoSucursal(),
                actualizada.getIdVigilante(),
                actualizada.getFecha(),
                actualizada.isConArma());
    }
    @Override
    @Transactional
    public ContratacionDTO actualizarFecha(int id, LocalDate fecha) {

        // Valida el identificador y la presencia de la fecha
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la contratacion debe ser positivo.");
        }

        if (fecha == null) {
            throw new DatosInvalidosException(
                    "La fecha de la contratacion es obligatoria.");
        }

        // Busca la contratación que se quiere actualizar
        Contratacion contratacion = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una contratacion con el identificador "
                        + id + "."));

        // Si la fecha cambia, comprueba que no exista otra igual
        if (!fecha.equals(contratacion.getFecha())
                && repositorio.existsByCodigoSucursalAndIdVigilanteAndFecha(
                        contratacion.getCodigoSucursal(),
                        contratacion.getIdVigilante(),
                        fecha)) {

            throw new RegistroDuplicadoException(
                    "Ya existe una contratacion para esa sucursal, vigilante y fecha.");
        }

        contratacion.cambiarFecha(fecha);

        Contratacion actualizada = repositorio.save(contratacion);

        return new ContratacionDTO(
                actualizada.getId(),
                actualizada.getCodigoSucursal(),
                actualizada.getIdVigilante(),
                actualizada.getFecha(),
                actualizada.isConArma());
    }
    @Override
    @Transactional
    public void eliminar(int id) {

        // Valida el identificador recibido
        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la contratacion debe ser positivo.");
        }

        // Busca la contratación antes de eliminarla
        Contratacion contratacion = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una contratacion con el identificador "
                        + id + "."));

        repositorio.delete(contratacion);
    }
    @Override
    @Transactional
    public ContratacionDTO actualizarDatos(
            int id, LocalDate fecha, Boolean conArma) {

        if (id <= 0) {
            throw new DatosInvalidosException(
                    "El identificador de la contratacion debe ser positivo.");
        }

        if (fecha == null) {
            throw new DatosInvalidosException(
                    "La fecha de la contratacion es obligatoria.");
        }

        if (conArma == null) {
            throw new DatosInvalidosException(
                    "La condicion de arma es obligatoria.");
        }

        Contratacion contratacion = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una contratacion con el identificador "
                        + id + "."));

        // Comprueba los duplicados antes de modificar los datos
        if (!fecha.equals(contratacion.getFecha())
                && repositorio.existsByCodigoSucursalAndIdVigilanteAndFecha(
                        contratacion.getCodigoSucursal(),
                        contratacion.getIdVigilante(),
                        fecha)) {

            throw new RegistroDuplicadoException(
                    "Ya existe una contratacion para esa sucursal, vigilante y fecha.");
        }

        contratacion.cambiarFecha(fecha);
        contratacion.cambiarCondicionArma(conArma);

        Contratacion actualizada = repositorio.save(contratacion);

        return new ContratacionDTO(
                actualizada.getId(),
                actualizada.getCodigoSucursal(),
                actualizada.getIdVigilante(),
                actualizada.getFecha(),
                actualizada.isConArma());
    }
}
