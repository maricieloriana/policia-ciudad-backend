package com.policiaciudad.service;

import com.policiaciudad.dto.*;
import com.policiaciudad.exception.DatosInvalidosException;
import com.policiaciudad.modelo.*;
import com.policiaciudad.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReglasServiciosTest {

    private final PersonaService personaService = new PersonaService();

    private final AsaltoRepository asaltos = mock(AsaltoRepository.class);
    private final BandaRepository bandas = mock(BandaRepository.class);
    private final ContratacionRepository contratos = mock(ContratacionRepository.class);
    private final EntidadBancariaRepository entidades = mock(EntidadBancariaRepository.class);
    private final SucursalRepository sucursales = mock(SucursalRepository.class);
    private final PersonaRepository personas = mock(PersonaRepository.class);
    private final PersonaDetenidaRepository detenidas = mock(PersonaDetenidaRepository.class);
    private final JuezRepository jueces = mock(JuezRepository.class);
    private final JuicioRepository juicios = mock(JuicioRepository.class);
    private final VigilanteRepository vigilantes = mock(VigilanteRepository.class);
    private final UsuarioRepository usuarios = mock(UsuarioRepository.class);
    private final PasswordEncoder contrasenias = mock(PasswordEncoder.class);
    private final LocalDate fecha = LocalDate.of(2026, 1, 1);

    @Test
    void asaltoRechazaIdentificadoresYFechaAntesDeGuardar() {
        AsaltoServiceImpl servicio = new AsaltoServiceImpl(asaltos, sucursales, detenidas, juicios);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(0, 1, fecha));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, -1, fecha));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, 1, null));
        verify(asaltos, never()).save(any());
    }

    @Test
    void asaltoValidoLlegaAlRepositorioYLaEdicionValidaLaFecha() {
        AsaltoServiceImpl servicio = new AsaltoServiceImpl(asaltos, sucursales, detenidas, juicios);
        when(sucursales.existsById(1)).thenReturn(true);
        when(detenidas.existsById(2)).thenReturn(true);
        when(asaltos.save(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
        assertEquals(fecha, servicio.registrar(1, 2, fecha).getFecha());
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarFecha(1, null));
    }

    @Test
    void bandaRechazaCantidadNegativaTantoAlRegistrarComoAlEditar() {
        BandaServiceImpl servicio = new BandaServiceImpl(bandas, detenidas);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(0, 2));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, -1));
        Banda banda = new Banda(1, 3);
        when(bandas.findById(1)).thenReturn(Optional.of(banda));
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarCantidadMiembros(1, -1));
        assertEquals(3, banda.getCantidadMiembros());
        verify(bandas, never()).save(any());
    }

    @Test
    void contratacionRechazaIdentificadoresYFecha() {
        ContratacionServiceImpl servicio = new ContratacionServiceImpl(
                contratos, sucursales, vigilantes, usuarios);
        assertThrows(DatosInvalidosException.class,
                () -> servicio.registrar(new ContratacionDTO(null, 0, 1, fecha, true)));
        assertThrows(DatosInvalidosException.class,
                () -> servicio.registrar(new ContratacionDTO(null, 1, -1, fecha, true)));
        assertThrows(DatosInvalidosException.class,
                () -> servicio.registrar(new ContratacionDTO(null, 1, 1, null, true)));
        verify(contratos, never()).save(any());
    }

    @Test
    void juicioRechazaIdentificadoresYResolucionesIncoherentes() {
        JuicioServiceImpl servicio = new JuicioServiceImpl(juicios, asaltos, detenidas, jueces);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(0, 1, 1, true, 1));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, 1, 1, true, 0));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, 1, 1, false, 1));
        verify(juicios, never()).save(any());
    }

    @Test
    void juicioInvalidoNoSeModificaParcialmente() {
        JuicioServiceImpl servicio = new JuicioServiceImpl(juicios, asaltos, detenidas, jueces);
        Juicio juicio = new Juicio(1, 2, 3, true, 4);
        when(juicios.findById(1)).thenReturn(Optional.of(juicio));
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarResolucion(1, false, 2));
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarResolucion(1, true, -1));
        assertTrue(juicio.isCondenado());
        assertEquals(4, juicio.getAniosCarcel());
        verify(juicios, never()).save(any());
    }

    @Test
    void juicioPermiteAbsolverConCeroAnios() {
        JuicioServiceImpl servicio = new JuicioServiceImpl(juicios, asaltos, detenidas, jueces);
        Juicio juicio = new Juicio(1, 2, 3, true, 4);
        when(juicios.findById(1)).thenReturn(Optional.of(juicio));
        when(juicios.save(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
        servicio.actualizarResolucion(1, false, 0);
        assertFalse(juicio.isCondenado());
        assertEquals(0, juicio.getAniosCarcel());
    }

    private RegistroSucursalDTO sucursal(int codigo, int empleados) {
        RegistroSucursalDTO datos = new RegistroSucursalDTO();
        datos.setCodigo(codigo);
        datos.setDomicilio(" Avenida 123 ");
        datos.setCantidadEmpleados(empleados);
        return datos;
    }

    @Test
    void entidadBancariaValidaTambienLasSucursalesDelAltaConjunta() {
        EntidadBancariaServiceImpl servicio = new EntidadBancariaServiceImpl(
                entidades, sucursales, asaltos, contratos);
        RegistroEntidadBancariaDTO datos = new RegistroEntidadBancariaDTO();
        datos.setCodigo(1);
        datos.setDomicilioCentral("Central 123");
        datos.setSucursales(List.of(sucursal(2, 3), sucursal(3, -1)));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        datos.setSucursales(List.of(sucursal(0, 3), sucursal(3, 2)));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        datos.setCodigo(0);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        verify(entidades, never()).saveAndFlush(any());
        verify(sucursales, never()).save(any());
    }

    @Test
    void entidadBancariaRechazaDomicilioInvalidoAlEditar() {
        EntidadBancariaServiceImpl servicio = new EntidadBancariaServiceImpl(
                entidades, sucursales, asaltos, contratos);
        EntidadBancaria entidad = new EntidadBancaria(1, "Original");
        when(entidades.findById(1)).thenReturn(Optional.of(entidad));
        assertThrows(DatosInvalidosException.class,
                () -> servicio.actualizarDomicilioCentral(1, " "));
        assertEquals("Original", entidad.getDomicilioCentral());
        verify(entidades, never()).save(any());
    }

    @Test
    void sucursalRechazaCodigoDomicilioYEmpleadosInvalidos() {
        SucursalServiceImpl servicio = new SucursalServiceImpl(sucursales, entidades, contratos, asaltos);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, sucursal(0, 1)));
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, sucursal(2, -1)));
        RegistroSucursalDTO datos = sucursal(2, 1);
        datos.setDomicilio(" ");
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(1, datos));
        verify(sucursales, never()).save(any());
    }

    @Test
    void sucursalValidaAmbosCamposAntesDeEditar() {
        SucursalServiceImpl servicio = new SucursalServiceImpl(sucursales, entidades, contratos, asaltos);
        Sucursal sucursal = new Sucursal(2, 1, "Original", 3);
        when(sucursales.findById(2)).thenReturn(Optional.of(sucursal));
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarDatos(2, "Nuevo", -1));
        assertEquals("Original", sucursal.getDomicilio());
        assertEquals(3, sucursal.getCantidadEmpleados());
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarCantidadEmpleados(2, -1));
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarDomicilio(2, " "));
        verify(sucursales, never()).save(any());
    }

    private RegistroJuezDTO juez() {
        RegistroJuezDTO datos = new RegistroJuezDTO();
        datos.setNombre("Ana");
        datos.setApellido("Pérez");
        datos.setDni("12345678");
        datos.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        datos.setMail("ana@example.com");
        datos.setTelefono("11111111");
        datos.setClaveInterna(5001);
        datos.setAniosServicio(3);
        return datos;
    }

    @Test
    void juezValidaClaveAntiguedadYDatosPersonalesEnElServicio() {
        JuezServiceImpl servicio = new JuezServiceImpl(jueces, juicios, usuarios, personas, personaService);
        RegistroJuezDTO datos = juez();
        datos.setClaveInterna(0);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        datos.setClaveInterna(5001);
        datos.setAniosServicio(-1);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        datos.setAniosServicio(3);
        datos.setMail("invalido");
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        when(jueces.findById(1)).thenReturn(Optional.of(new Juez("Ana", 5001, 3)));
        assertThrows(DatosInvalidosException.class, () -> servicio.actualizarAniosServicio(1, -1));
        verify(jueces, never()).saveAndFlush(any());
    }

    @Test
    void detenidaRechazaBandaNegativaYMailInvalido() {
        PersonaDetenidaServiceImpl servicio = new PersonaDetenidaServiceImpl(
                detenidas, bandas, asaltos, juicios, usuarios, personas, personaService);
        RegistroPersonaDetenidaDTO datos = new RegistroPersonaDetenidaDTO();
        datos.setNombre("Ana");
        datos.setApellido("Pérez");
        datos.setDni("12345678");
        datos.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        datos.setMail("ana@example.com");
        datos.setTelefono("11111111");
        datos.setNumeroBanda(-1);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        datos.setNumeroBanda(null);
        datos.setMail("invalido");
        assertThrows(DatosInvalidosException.class, () -> servicio.registrar(datos));
        verify(detenidas, never()).saveAndFlush(any());
    }

    @Test
    void usuarioExigeUnHashValidoAntesDeGuardar() {
        UsuarioServiceImpl servicio = new UsuarioServiceImpl(personas, usuarios, vigilantes, contrasenias);
        when(personas.existsById(1)).thenReturn(true);
        when(contrasenias.encode(any())).thenReturn("");
        assertThrows(DatosInvalidosException.class,
                () -> servicio.registrar(1, "usuario", "Clave12345", Rol.INVESTIGADOR));
        verify(usuarios, never()).save(any());
    }

    @Test
    void administradorInicialExistenteConservaLaCuenta() {
        UsuarioServiceImpl servicio = new UsuarioServiceImpl(personas, usuarios, vigilantes, contrasenias);
        when(usuarios.existsByNombreUsuario("admin")).thenReturn(true);
        servicio.crearAdministradorInicial(" admin ", "Clave12345");
        verify(personas, never()).save(any());
        verify(usuarios, never()).save(any());
    }

    @Test
    void administradorInicialReutilizaElRegistroDeUsuarios() {
        UsuarioServiceImpl servicio = new UsuarioServiceImpl(personas, usuarios, vigilantes, contrasenias);
        Persona titular = new Persona("Administrador inicial");
        ReflectionTestUtils.setField(titular, "id", 1);
        when(personas.save(any(Persona.class))).thenReturn(titular);
        when(personas.existsById(1)).thenReturn(true);
        when(contrasenias.encode("Clave12345")).thenReturn("hash-de-prueba");
        when(usuarios.save(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
        servicio.crearAdministradorInicial(" admin ", "Clave12345");
        verify(usuarios).save(argThat(usuario -> usuario.getIdPersona() == 1
                && usuario.getRol() == Rol.ADMINISTRADOR
                && usuario.getNombreUsuario().equals("admin")
                && usuario.getPasswordHash().equals("hash-de-prueba")));
    }
}
