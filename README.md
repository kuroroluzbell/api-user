# API User - User Management Service

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.26.1-blue.svg)](https://quarkus.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![Keycloak](https://img.shields.io/badge/Keycloak-24.0.3-red.svg)](https://www.keycloak.org/)

API REST para la gestión integral de usuarios con autenticación y autorización mediante Keycloak, desarrollada con Quarkus Framework. Este servicio proporciona operaciones CRUD completas sobre usuarios, gestión de roles, autenticación OAuth2/OIDC y persistencia dual (Keycloak + PostgreSQL).

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Prerrequisitos](#-prerrequisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Endpoints API](#-endpoints-api)
- [Base de Datos](#-base-de-datos)
- [Seguridad](#-seguridad)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Testing](#-testing)
- [Documentación Adicional](#-documentación-adicional)

## ✨ Características

### Gestión de Usuarios
- ✅ **CRUD completo de usuarios**: Crear, leer, actualizar y eliminar usuarios
- ✅ **Paginación**: Listado de usuarios con soporte de paginación
- ✅ **Validación de datos**: Validación automática con Bean Validation
- ✅ **Gestión de imágenes de perfil**: Actualización de imagen de perfil en formato Base64
- ✅ **Persistencia dual**: Sincronización automática entre Keycloak y PostgreSQL

### Autenticación y Autorización
- ✅ **Autenticación OAuth2/OIDC**: Integración completa con Keycloak
- ✅ **Login de usuarios**: Endpoint de autenticación con generación de tokens JWT
- ✅ **Protección de endpoints**: Control de acceso basado en roles
- ✅ **Token de cliente**: Validación adicional mediante X-Client-Token para endpoints públicos

### Gestión de Roles
- ✅ **Asignación de roles**: Asignar roles de Keycloak a usuarios
- ✅ **Eliminación de roles**: Remover roles de usuarios
- ✅ **Consulta de roles**: Listar roles asignados a un usuario

### Documentación y Observabilidad
- ✅ **OpenAPI/Swagger**: Documentación interactiva de la API
- ✅ **Health checks**: Endpoints de salud para monitoreo
- ✅ **Logs estructurados**: Sistema de logging configurable
- ✅ **Code coverage**: Integración con Jacoco para análisis de cobertura

## 🛠 Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Quarkus** | 3.26.1 | Framework backend supersónico |
| **Keycloak** | 24.0.3 | Gestión de identidad y acceso |
| **PostgreSQL** | 17 | Base de datos relacional |
| **Hibernate ORM Panache** | - | ORM simplificado |
| **SmallRye OpenAPI** | - | Especificación OpenAPI 3.0 |
| **Lombok** | 1.18.32 | Reducción de código boilerplate |
| **Mockito** | 5.2.0 | Framework de testing |
| **JUnit 5** | - | Framework de pruebas unitarias |
| **REST Assured** | - | Testing de APIs REST |
| **Jacoco** | - | Cobertura de código |
| **Maven** | 3.x | Gestión de dependencias y build |
| **Docker** | - | Containerización |

## 📦 Prerrequisitos

- **Java 21** o superior ([OpenJDK](https://openjdk.org/) recomendado)
- **Maven 3.8+** para gestión de dependencias
- **Keycloak 21+** instancia en ejecución
- **PostgreSQL 12+** base de datos
- **Docker y Docker Compose** (opcional, para ejecución con contenedores)
- **Git** para clonar el repositorio

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/Transversal-kl/api-user.git
cd api-user
```

### 2. Configurar variables de entorno

Copiar el archivo de template y configurar las variables:

```bash
cp .env-template .env
```

Editar el archivo `.env` con tus configuraciones locales:

```env
# Configuración general
QUARKUS_HTTP_PORT=8091
QUARKUS_PROFILE=dev

# Configuración de Keycloak
KEYCLOAK_URL=http://localhost:8083/
KEYCLOAK_CLIENT_ID=cli_billpay_app
KEYCLOAK_CLIENT_SECRET=YGMyRIRzM2qa4hYZFxVVqrSQzRWlrZuB
KEYCLOAK_REALM=billpay_app
KEYCLOAK_ADMIN_USER=kuroroluzbell
KEYCLOAK_ADMIN_PASSWORD=123456

# Token de autenticación para endpoints públicos
AUTH_CLIENT_TOKEN=YGMyRIRzM2qa4hYZFxVVqrSQzRWlrZuB

# Configuración de PostgreSQL
DB_HOST=localhost
DB_PORT=5432
DB_NAME=db_account
DB_USERNAME=user_account
DB_PASSWORD=1234
QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION=update

# Configuración de logs
QUARKUS_LOG_CONSOLE_LEVEL=INFO
```

### 3. Compilar el proyecto

```bash
./mvnw clean compile
```

## ⚙️ Configuración

### Configuración de Keycloak

1. Crear un realm llamado `billpay_app` (o el nombre configurado en `KEYCLOAK_REALM`)
2. Crear un cliente con:
   - **Client ID**: `cli_billpay_app`
   - **Client Protocol**: openid-connect
   - **Access Type**: confidential
   - **Service Accounts Enabled**: ON
   - **Authorization Enabled**: ON
3. Configurar los roles necesarios:
   - `billpay_user_create`
   - `billpay_user_update`
   - `billpay_user_delete`
   - `billpay_user_selectone`
   - `billpay_user_selectall`
   - `admin`
   - `user`

### Configuración de PostgreSQL

Crear la base de datos y usuario:

```sql
CREATE DATABASE db_account;
CREATE USER user_account WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE db_account TO user_account;
```

La tabla de usuarios se creará automáticamente con Hibernate en modo `update`.

## 🏃 Ejecución

### Modo Desarrollo (Dev Mode)

Ejecutar con hot reload activado:

```bash
./mvnw quarkus:dev
```

La aplicación estará disponible en:
- **API**: http://localhost:8091
- **Swagger UI**: http://localhost:8091/swagger-ui
- **OpenAPI Spec**: http://localhost:8091/openapi
- **Dev UI**: http://localhost:8091/q/dev

### Modo Docker

Usar Docker Compose para ejecutar todo el stack:

```bash
docker-compose up --build api-user
```

> **Nota**: Asegúrate de tener la red `quarkus-net` creada o ajusta el `docker-compose.yml`.

### Modo Producción

#### Empaquetar como JAR

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

#### Empaquetar como Uber-JAR

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

#### Crear ejecutable nativo

```bash
# Con GraalVM instalado
./mvnw package -Dnative

# O usando contenedor
./mvnw package -Dnative -Dquarkus.native.container-build=true

# Ejecutar
./target/api-user-1.0.0-SNAPSHOT-runner
```

## 📡 Endpoints API

### Autenticación

#### POST /auth/login
Autenticación de usuario y generación de token JWT.

**Headers:**
- `X-Client-Token`: Token de cliente para validación

**Request Body:**
```json
{
  "username": "usuario",
  "password": "contraseña"
}
```

**Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 3600,
  "refresh_expires_in": 1800,
  "token_type": "Bearer"
}
```

### Gestión de Usuarios

#### POST /users
Crear un nuevo usuario.

**Headers:**
- `X-Client-Token`: Token de cliente para validación

**Request Body:**
```json
{
  "username": "nuevouser",
  "email": "usuario@ejemplo.com",
  "firstName": "Nombre",
  "lastName": "Apellido",
  "enabled": true,
  "password": "Password123!"
}
```

**Response:** `201 Created`

#### PUT /users
Actualizar usuario existente.

**Headers:**
- `Authorization`: Bearer token

**Roles requeridos:** `user`, `admin`, `billpay_user_update`

**Request Body:**
```json
{
  "id": "uuid-usuario",
  "username": "usuarioactualizado",
  "email": "nuevo@ejemplo.com",
  "firstName": "NuevoNombre",
  "lastName": "NuevoApellido",
  "enabled": true
}
```

**Response:** `200 OK`

#### GET /users/{id}
Obtener usuario por ID.

**Headers:**
- `Authorization`: Bearer token

**Roles requeridos:** `user`, `admin`, `billpay_user_selectone`

**Response:** `200 OK`
```json
{
  "id": "uuid-usuario",
  "username": "usuario",
  "email": "usuario@ejemplo.com",
  "firstName": "Nombre",
  "lastName": "Apellido",
  "enabled": true
}
```

#### GET /users?page=0&size=10
Listar usuarios con paginación.

**Headers:**
- `Authorization`: Bearer token

**Roles requeridos:** `user`, `admin`, `billpay_user_selectall`

**Query Parameters:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "users": [...],
  "totalUsers": 100,
  "page": 0,
  "size": 10
}
```

#### DELETE /users/{id}
Eliminar usuario por ID.

**Headers:**
- `Authorization`: Bearer token

**Roles requeridos:** `user`, `admin`, `billpay_user_delete`

**Response:** `204 No Content`

#### PUT /users/{id}/profile-image
Actualizar imagen de perfil del usuario.

**Headers:**
- `Authorization`: Bearer token
- `Content-Type`: text/plain

**Roles requeridos:** `user`, `admin`, `billpay_user_update`

**Request Body:** Imagen en formato Base64 (string)

**Response:** `200 OK`
```json
{
  "message": "Profile image updated successfully"
}
```

### Gestión de Roles

#### POST /user-role-map/{id}/roles/{roleName}
Asignar rol a usuario.

**Headers:**
- `Authorization`: Bearer token

**Response:** `204 No Content`

#### DELETE /user-role-map/{id}/roles/{roleName}
Remover rol de usuario.

**Headers:**
- `Authorization`: Bearer token

**Response:** `204 No Content`

#### GET /user-role-map/{id}/roles
Obtener roles de usuario.

**Headers:**
- `Authorization`: Bearer token

**Response:** `200 OK`
```json
["admin", "user", "billpay_user_update"]
```

## 🗄️ Base de Datos

### Persistencia Dual

El sistema implementa una estrategia de persistencia dual:

1. **Keycloak**: Fuente principal de autenticación, autorización y gestión de credenciales
2. **PostgreSQL**: Almacenamiento complementario para consultas optimizadas y datos extendidos

### Esquema de Base de Datos

```sql
CREATE TABLE "user" (
    id UUID PRIMARY KEY,                -- ID de usuario en Keycloak
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at TIMESTAMP,
    profile_image BYTEA               -- Imagen de perfil en formato binario
);
```

### Sincronización

- **CREATE**: Usuario creado en Keycloak → Automáticamente guardado en PostgreSQL
- **UPDATE**: Actualización en Keycloak → Sincronización a PostgreSQL
- **DELETE**: Eliminación de Keycloak → Eliminación de PostgreSQL
- **Rollback**: Si falla PostgreSQL durante CREATE, se elimina el usuario de Keycloak

## 🔐 Seguridad

### Autenticación

- **OAuth2/OIDC**: Protocolo estándar de autenticación
- **JWT Tokens**: Tokens seguros con firma RSA256
- **Bearer Authentication**: Tokens en header Authorization

### Autorización

- **RBAC**: Control de acceso basado en roles
- **Roles granulares**: Permisos específicos por operación
- **Token de cliente**: Capa adicional de seguridad para endpoints públicos

### Endpoints Públicos

Los siguientes endpoints están accesibles sin autenticación:
- `/auth/login` - Login de usuarios
- `/users` (POST) - Creación de usuarios
- `/q/swagger-ui/*` - Documentación Swagger
- `/q/openapi` - Especificación OpenAPI

### Buenas Prácticas

- ✅ No almacenar secrets en el repositorio (usar variables de entorno)
- ✅ Usar HTTPS en producción
- ✅ Rotar secretos periódicamente
- ✅ Implementar rate limiting para endpoints públicos
- ✅ Validar y sanitizar todas las entradas

## 📁 Estructura del Proyecto

```
api-user/
├── src/
│   ├── main/
│   │   ├── docker/              # Dockerfiles
│   │   ├── java/
│   │   │   └── com/fv/billpay/api/user/
│   │   │       ├── dto/         # Data Transfer Objects
│   │   │       │   ├── request/ # DTOs de entrada
│   │   │       │   └── response/# DTOs de salida
│   │   │       ├── entity/      # Entidades JPA
│   │   │       ├── repository/  # Capa de acceso a datos
│   │   │       ├── resource/    # Endpoints REST (JAX-RS)
│   │   │       ├── service/     # Lógica de negocio
│   │   │       └── util/        # Utilidades y constantes
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Tests unitarios e integración
├── docs/
│   ├── copilot.md              # Documentación técnica detallada
│   └── postgresql.md           # Documentación de PostgreSQL
├── docker-compose.yml          # Orquestación Docker
├── pom.xml                     # Configuración Maven
├── .env-template               # Template de variables de entorno
└── README.md                   # Este archivo
```

### Arquitectura por Capas

```
Cliente → Resource (JAX-RS) → Service → Repository → Keycloak/PostgreSQL
```

- **Resource**: Endpoints REST, validación de entrada, manejo de respuestas HTTP
- **Service**: Orquestación de lógica de negocio, transacciones
- **Repository**: Integración con Keycloak Admin Client y PostgreSQL
- **Entity**: Modelos de datos JPA/Hibernate
- **DTO**: Objetos de transferencia con validación Bean Validation

## 🧪 Testing

### Ejecutar tests

```bash
# Todos los tests
./mvnw test

# Con cobertura
./mvnw test jacoco:report

# Ver reporte de cobertura
open target/site/jacoco/index.html
```

### Framework de Testing

- **JUnit 5**: Tests unitarios
- **Mockito**: Mocking de dependencias
- **REST Assured**: Testing de endpoints REST
- **Jacoco**: Análisis de cobertura de código

### Estructura de Tests

```java
@QuarkusTest
class UserServiceTest {
    @InjectMock
    IUserRepository userRepository;
    
    @Inject
    IUserService userService;
    
    @Test
    void testCreateUser() {
        // Given
        UserCreateRequestDto dto = new UserCreateRequestDto();
        // ...
        
        // When & Then
        assertNotNull(userService.createUser(dto));
    }
}
```

## 📚 Documentación Adicional

- **[docs/copilot.md](docs/copilot.md)**: Arquitectura técnica detallada, casos de uso y roadmap
- **[docs/postgresql.md](docs/postgresql.md)**: Integración con PostgreSQL y estrategia de persistencia dual
- **Swagger UI**: Documentación interactiva disponible en `/swagger-ui` cuando la app está ejecutándose
- **Quarkus Guides**: https://quarkus.io/guides/

## 🤝 Contribución

1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

### Convenciones de Código

- Interfaces con prefijo `I` (ej: `IUserService`)
- DTOs con sufijo `Dto` (ej: `UserCreateRequestDto`)
- Clases de implementación con sufijo `Impl` (ej: `UserServiceImpl`)
- Usar Lombok para reducir boilerplate
- Seguir principios SOLID y Clean Code

## 📝 Licencia

Este proyecto es parte de la organización Transversal-kl.

## 👥 Autores

- **Transversal-kl Team** - [GitHub](https://github.com/Transversal-kl)

## 🔗 Enlaces Útiles

- [Quarkus Framework](https://quarkus.io/)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [OpenAPI Specification](https://swagger.io/specification/)

---

**¿Necesitas ayuda?** Revisa la [documentación técnica detallada](docs/copilot.md) o abre un issue en GitHub.
