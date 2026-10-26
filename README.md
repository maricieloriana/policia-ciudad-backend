# Policía de la Ciudad — Trabajo Práctico Final

Aplicación desarrollada en Java con Spring Boot, Spring Security, Spring Data JPA, Hibernate, Thymeleaf y MySQL.

## Requisitos para ejecutar

- JDK 21.
- MySQL en ejecución.
- NetBeans u otro entorno compatible con proyectos Maven.
- Acceso al repositorio de GitHub.

## Preparación de la base de datos

1. Descargar o clonar este repositorio.
2. Abrir HeidiSQL y conectarse al servidor MySQL local.
3. Abrir el archivo `database/policia_ciudad.sql` mediante **Archivo → Cargar archivo SQL**.
4. Ejecutar el archivo completo.

El archivo contiene la estructura y los datos de prueba. Importarlo inicialmente en un servidor donde no exista una base `policia_ciudad` con datos propios.

Cada integrante utiliza su propia base de datos local. Los cambios en MySQL no se sincronizan mediante GitHub.

## Configuración local

Crear el archivo `src/main/resources/application-local.properties` con este contenido, reemplazando los valores por las credenciales de MySQL de cada computadora:

```properties
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_CONTRASENIA_MYSQL
```

Este archivo está excluido de Git para mantener privadas las credenciales.

La conexión compartida utiliza la base `policia_ciudad` en `localhost:3306`. Si el servidor utiliza otra dirección o puerto, agregar también la configuración correspondiente en el archivo local:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/policia_ciudad
```

## Ejecución

1. En NetBeans, abrir la carpeta del proyecto que contiene `pom.xml`.
2. Configurar el proyecto para utilizar JDK 21.
3. Ejecutar **Build** y comprobar que finalice correctamente.
4. Ejecutar **Run** una sola vez.
5. Esperar a que la aplicación termine de iniciar.
6. Abrir en el navegador: http://localhost:8080/login

El puerto 8080 debe estar disponible.

## Cuentas de prueba

La base exportada incluye las siguientes cuentas:

| Usuario | Rol |
| --- | --- |
| adminprueba | ADMINISTRADOR |
| investigadorprueba | INVESTIGADOR |
| vigilanteprueba | VIGILANTE |

Las contraseñas de prueba se comunican entre los integrantes por un medio privado. Las credenciales de estas cuentas son independientes de las credenciales de MySQL.

## Organización del proyecto

- `controller`: recibe las solicitudes de la API y de las pantallas web.
- `service`: implementa las operaciones y reglas del sistema.
- `repository`: permite acceder a los datos persistidos.
- `modelo`: contiene las entidades y sus comportamientos.
- `dto`: define los datos de entrada y de respuesta.
- `security` y `config`: contienen la autenticación y la configuración de acceso.
- `exception`: contiene las excepciones y el manejo de errores.
- `src/main/resources/templates`: contiene las pantallas Thymeleaf.
- `src/main/resources/static/css`: contiene los estilos compartidos.
- `database`: contiene la copia SQL para preparar la base de prueba.

## Trabajo en equipo

Antes de comenzar a trabajar, consultar los cambios remotos con **Fetch origin** y, si aparece, utilizar **Pull origin**.

Coordinar qué archivos modifica cada integrante. Al terminar una tarea, guardar los archivos, revisar los cambios en GitHub Desktop, crear un **Commit** con un mensaje descriptivo y utilizar **Push origin** para compartirlo.

Cada integrante debe realizar sus aportes desde su propia cuenta de GitHub.
