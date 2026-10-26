package com.policiaciudad.service;

import com.policiaciudad.dto.BandaDTO;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.exception.RegistroDuplicadoException;
import com.policiaciudad.modelo.Banda;
import com.policiaciudad.repository.BandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.policiaciudad.exception.RecursoNoEncontradoException;
import com.policiaciudad.exception.RecursoEnUsoException;
import com.policiaciudad.repository.PersonaDetenidaRepository;

@Service
public class BandaServiceImpl implements BandaService {

    private final BandaRepository repositorio;
    private final PersonaDetenidaRepository personaDetenidaRepository;

    // Spring proporciona el repositorio mediante el constructor
    public BandaServiceImpl(
            BandaRepository repositorio,
            PersonaDetenidaRepository personaDetenidaRepository) {
        this.repositorio = repositorio;
        this.personaDetenidaRepository = personaDetenidaRepository;
    }

    @Override
    @Transactional
    public BandaDTO registrar(Integer numero, Integer cantidadMiembros) {

        // Comprueba que ambos datos estén presentes
        if (numero == null || cantidadMiembros == null) {
            throw new DatosInvalidosException(
                    "El numero y la cantidad de miembros son obligatorios.");
        }

        // El modelo valida el número y la cantidad de miembros
        Banda banda = new Banda(numero, cantidadMiembros);

        // Evita registrar otra banda con el mismo número
        if (repositorio.existsById(numero)) {
            throw new RegistroDuplicadoException(
                    "Ya existe una banda con ese numero.");
        }

        Banda guardada = repositorio.save(banda);

        // Devuelve los datos registrados
        return new BandaDTO(
                guardada.getNumero(),
                guardada.getCantidadMiembros());
    }
    @Override
    @Transactional(readOnly = true)
    public List<BandaDTO> listar() {

        // Convierte las bandas guardadas en datos de respuesta
        return repositorio.findAll().stream()
                .map(banda -> new BandaDTO(
                        banda.getNumero(),
                        banda.getCantidadMiembros()))
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public BandaDTO consultarPorNumero(int numero) {

        if (numero <= 0) {
            throw new DatosInvalidosException(
                    "El numero de la banda debe ser positivo.");
        }

        // Busca la banda o informa que no existe
        Banda banda = repositorio.findById(numero)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una banda con el numero " + numero + "."));

        return new BandaDTO(
                banda.getNumero(),
                banda.getCantidadMiembros());
    }
    @Override
    @Transactional
    public BandaDTO actualizarCantidadMiembros(
            int numero, Integer cantidadMiembros) {

        // Valida el número y la presencia del dato
        if (numero <= 0) {
            throw new DatosInvalidosException(
                    "El numero de la banda debe ser positivo.");
        }

        if (cantidadMiembros == null) {
            throw new DatosInvalidosException(
                    "La cantidad de miembros es obligatoria.");
        }

        // Busca la banda que se quiere actualizar
        Banda banda = repositorio.findById(numero)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una banda con el numero " + numero + "."));

        // El modelo rechaza cantidades negativas
        banda.actualizarCantidadMiembros(cantidadMiembros);

        Banda actualizada = repositorio.save(banda);

        return new BandaDTO(
                actualizada.getNumero(),
                actualizada.getCantidadMiembros());
    }
    @Override
    @Transactional
    public void eliminar(int numero) {

        if (numero <= 0) {
            throw new DatosInvalidosException(
                    "El numero de la banda debe ser positivo.");
        }

        // Busca la banda antes de eliminarla
        Banda banda = repositorio.findById(numero)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una banda con el numero " + numero + "."));

        // Protege las asociaciones con personas detenidas
        if (personaDetenidaRepository.existsByNumeroBanda(numero)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar la banda porque tiene personas detenidas asociadas.");
        }

        repositorio.delete(banda);
    }
}