package com.policiaciudad.controller;

import com.policiaciudad.dto.*;
import com.policiaciudad.modelo.PersonaDetenida;
import com.policiaciudad.service.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PersonasVistasTest {
    private final VigilanteService vigilantes = mock(VigilanteService.class);
    private final JuezService jueces = mock(JuezService.class);
    private final PersonaDetenidaService detenidas = mock(PersonaDetenidaService.class);
    private final BandaService bandas = mock(BandaService.class);
    private MockMvc mvc;
    private final UsernamePasswordAuthenticationToken admin =
            UsernamePasswordAuthenticationToken.authenticated("admin", null,
                    List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR")));

    @BeforeEach void preparar() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/"); resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML"); resolver.setCharacterEncoding("UTF-8");
        var motor = new SpringTemplateEngine(); motor.setTemplateResolver(resolver);
        var vistas = new ThymeleafViewResolver(); vistas.setTemplateEngine(motor);
        vistas.setCharacterEncoding("UTF-8");
        mvc = MockMvcBuilders.standaloneSetup(new VigilanteWebController(vigilantes),
                new JuezWebController(jueces), new PersonaDetenidaWebController(detenidas, bandas))
                .setViewResolvers(vistas).build();
        var fecha = LocalDate.of(2000, 1, 1);
        var v = new VigilanteDTO(1, "Ana Pérez", "Ana", "Pérez", "00123456",
                fecha, 26, "ana@example.com", "11111111");
        var j = new JuezDTO(2, "Ana Pérez", "Ana", "Pérez", "00123457",
                fecha, 26, "ana@example.com", "11111111", 5001, 3);
        var p = new PersonaDetenidaDTO(new PersonaDetenida("Ana", "Pérez", "00123458",
                fecha, "ana@example.com", "11111111"));
        when(vigilantes.listar()).thenReturn(List.of(v));
        when(jueces.listar()).thenReturn(List.of(j));
        when(detenidas.listar()).thenReturn(List.of(p));
        when(vigilantes.consultarPorId(1)).thenReturn(v);
        when(jueces.consultarPorId(2)).thenReturn(j);
        when(detenidas.consultarPorId(3)).thenReturn(p);
        when(vigilantes.consultarPropio("admin")).thenReturn(v);
        when(bandas.listar()).thenReturn(List.of());
    }

    @Test void listadosYPerfilMuestranDatosPersonales() throws Exception {
        for (String ruta : List.of("/vigilantes", "/jueces", "/personas-detenidas", "/mi-perfil")) {
            mvc.perform(get(ruta).principal(admin)).andExpect(status().isOk())
                    .andExpect(content().string(containsString("ana@example.com")));
        }
    }

    @Test void formulariosDeAltaYEdicionGeneranTodosLosCampos() throws Exception {
        for (String ruta : List.of("/vigilantes/nuevo", "/jueces/nuevo",
                "/personas-detenidas/nueva", "/vigilantes/1/editar",
                "/jueces/2/editar", "/personas-detenidas/3/nombre")) {
            mvc.perform(get(ruta).principal(admin)).andExpect(status().isOk())
                    .andExpect(content().string(containsString("name=\"fechaNacimiento\"")))
                    .andExpect(content().string(containsString("name=\"dni\"")));
        }
    }

    @Test void registroWebConvierteFechaYEntregaDTOCompleto() throws Exception {
        mvc.perform(post("/vigilantes").param("nombre", "Ana").param("apellido", "Pérez")
                .param("dni", "00123456").param("fechaNacimiento", "2000-01-01")
                .param("mail", "ana@example.com").param("telefono", "11111111"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/vigilantes"));
        verify(vigilantes).registrar(argThat(d -> d.getDni().equals("00123456")
                && d.getFechaNacimiento().equals(LocalDate.of(2000, 1, 1))));
    }

    @Test void fechaMalFormadaRegresaAlFormulario() throws Exception {
        mvc.perform(post("/jueces").param("fechaNacimiento", "no-es-fecha"))
                .andExpect(status().isOk()).andExpect(view().name("juez-formulario"))
                .andExpect(content().string(containsString("Revis")));
        verify(jueces, never()).registrar(any(RegistroJuezDTO.class));
    }
}
