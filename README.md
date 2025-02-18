Agencia de Turismo - API REST

📌 Introducción

Este proyecto es una API REST para la gestión de una agencia de turismo, permitiendo la reserva de hoteles y vuelos. La API maneja autenticación segura con Spring Security y Basic Auth, gestión de pasajeros y disponibilidad de habitaciones y vuelos.

🚀 Características

Gestión de hoteles y habitaciones con disponibilidad actualizada.

Gestión de vuelos y reservas de vuelos.

Manejo de pasajeros y sus reservas asociadas.

Autenticación y autorización con Spring Security y Basic Auth.

Documentación con Swagger.

Pruebas unitarias para validar la lógica del negocio.

🛠️ Tecnologías Utilizadas

Java 17

Spring Boot

Spring Security + Basic Auth

Spring Data JPA + Hibernate

MySQL (configurado en MySQL Workbench)

Lombok

MapStruct

Swagger/OpenAPI

JUnit + Mockito

📦 Estructura del Proyecto

📂 agencia
 ├── 📂 src/main/java/com/luis/agencia
 │   ├── 📂 controller  # Controladores REST
 │   ├── 📂 service     # Servicios con la lógica de negocio
 │   ├── 📂 repository  # Repositorios JPA
 │   ├── 📂 model       # Entidades JPA
 │   ├── 📂 dto         # Data Transfer Objects (DTOs)
 │   ├── 📂 mapper      # MapStruct mappers
 │   ├── 📂 security    # Configuración de seguridad Basic Auth
 │   ├── 📂 exception   # Manejo de excepciones globales
 ├── 📂 src/test/java/com/luis/agencia  # Pruebas unitarias
 ├── 📂 src/main/resources
 │   ├── application.properties  # Configuración del proyecto
 ├── 📄 README.md  # Documentación del proyecto
 ├── 📄 agencia.sql  # Script de base de datos
 ├── 📄 postman_collection.json  # Colección de Postman

⚙️ Instalación y Configuración

1️⃣ Clonar el Repositorio

git clone https://github.com/tu-usuario/agencia.git
cd agencia

2️⃣ Configurar la Base de Datos

En src/main/resources/application.properties, configura los parámetros de conexión:

spring.datasource.url=jdbc:mysql://localhost:3306/agencia2
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update

Ejecuta el script agencia.sql en MySQL Workbench para generar datos de referencia.

3️⃣ Construir y Ejecutar el Proyecto

mvn clean install
mvn spring-boot:run

La API estará disponible en http://localhost:8080.

🔐 Autenticación y Seguridad

El sistema implementa autenticación basada en Basic Auth.

🔑 Credenciales de Acceso

Usuario: luis
Contraseña: 1234

🔒 Reglas de Seguridad

GET: No requieren autenticación.

POST, PUT, DELETE: Requieren autenticación con usuario y contraseña.

Todas las peticiones protegidas requieren autenticación HTTP Basic con estas credenciales.

📜 Documentación de la API (Swagger)

Swagger está disponible en:

http://localhost:8080/swagger-ui.html

Para acceder a los datos de Api-docs, están disponibles en: 

http://localhost:8080/v3/api-docs

🧪 Pruebas Unitarias

El proyecto incluye pruebas unitarias con JUnit y Mockito.
Ejecutar los tests:

mvn test

🛢️ Base de Datos

El script agencia.sql crea las tablas necesarias y proporciona datos iniciales.

🛠️ Colección de Postman

Se incluye postman_collection.json con todas las peticiones preconfiguradas para probar la API.

Importar:

Abrir Postman.

Ir a Importar y seleccionar postman_collection.json.

Configurar la autenticación Basic Auth en las peticiones protegidas.

📜 Licencia

Este proyecto está bajo la MIT License.

🤝 Contribución

Si deseas contribuir:

Haz un Fork del proyecto.

Crea una rama (git checkout -b feature-nueva).

Commitea los cambios (git commit -m 'Añadir nueva funcionalidad').

Haz un push (git push origin feature-nueva).

Abre un Pull Request.

📌 Autor: Luis - Agencia de Turismo 🚀