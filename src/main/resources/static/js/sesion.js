(() => {
    const configuracion = document.currentScript.dataset;
    let cerrando = false;

    async function cerrarPorHistorial() {
        if (cerrando) {
            return;
        }

        cerrando = true;
        document.documentElement.style.visibility = "hidden";

        try {
            // Solicita un token vigente, incluso si la página era antigua
            const respuestaToken = await fetch(configuracion.csrf, {
                credentials: "same-origin",
                cache: "no-store"
            });

            if (!respuestaToken.ok) {
                throw new Error("No se pudo obtener el token.");
            }

            const csrf = await respuestaToken.json();

            // El cierre real se realiza en Spring Security
            const respuestaSalida = await fetch(configuracion.logout, {
                method: "POST",
                credentials: "same-origin",
                cache: "no-store",
                headers: {
                    [csrf.headerName]: csrf.token
                }
            });

            if (!respuestaSalida.ok) {
                throw new Error("No se pudo confirmar el cierre.");
            }

            window.location.replace(configuracion.login + "?logout");

        } catch (error) {
            cerrando = false;

            // Evita volver a mostrar los datos de la página antigua
            const mensaje = document.createElement("p");
            mensaje.textContent =
                    "No se pudo confirmar el cierre de sesión. Revisá la conexión y reintentá.";
            mensaje.setAttribute("role", "alert");

            const boton = document.createElement("button");
            boton.type = "button";
            boton.textContent = "Reintentar cierre de sesión";
            boton.addEventListener("click", cerrarPorHistorial);

            document.body.replaceChildren(mensaje, boton);
            document.documentElement.style.visibility = "visible";
        }
    }

    window.addEventListener("pageshow", (evento) => {
        const navegacion = performance.getEntriesByType("navigation")[0];
        const vuelvePorHistorial =
                evento.persisted || navegacion?.type === "back_forward";

        if (vuelvePorHistorial) {
            cerrarPorHistorial();
        } else {
            document.documentElement.style.visibility = "visible";
        }
    });

    // Oculta la página antes de que el navegador guarde su estado
    window.addEventListener("pagehide", () => {
        document.documentElement.style.visibility = "hidden";
    });
})();
