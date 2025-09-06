## Nombre del Proyecto

API User - Gestión de Usuarios en Keycloak (Quarkus)

## 1. Descripción Técnica del Problema que Resuelve
Servicio backend que centraliza operaciones CRUD sobre usuarios en Keycloak, normalizando validaciones, manejo de errores y exposición de endpoints REST para otros sistemas internos.

### Requisitos Funcionales
- Crear / actualizar / eliminar / consultar usuario (individual + paginado).
- Validación de entrada con Bean Validation.
- Respuestas JSON consistentes.
- Documentación OpenAPI.

### Requisitos No Funcionales
- Bajo acoplamiento por capas.
- Observabilidad ampliable (pendiente métricas / health extra).
- Facilidad de extensión y testeo.

## 2. Arquitectura General
Tipo: Servicio monolítico modularizado por capas.

Flujo lógico:
```
Cliente -> Resource (JAX-RS) -> Service (orquesta) -> Repository (Keycloak Admin Client) -> Keycloak
```
Comunicación externa: HTTP REST a Keycloak Admin API.

## 3. Componentes Principales
| Capa | Responsabilidad | Tecnologías |
|------|-----------------|-------------|
| Resource | Endpoints REST (/users) | JAX-RS, Quarkus REST | 
| Service | Lógica orquestación | CDI |
| Repository | Integración Keycloak | Keycloak Admin Client |
| DTOs | Transporte + validación | Lombok, Bean Validation |
| Utils | Proveedores (KeycloakProvider), constantes | Java |
| Error Handling | (Pendiente de implementación global) | ExceptionMapper |

## 4. Endpoints (Matriz)
| Método | Path | Descripción | Auth | Request DTO | Response DTO |
|--------|------|------------|------|-------------|--------------|
| POST | /users | Crea usuario | Bearer | UserCreateRequestDto | UserResponseDto |
| PUT | /users | Actualiza usuario | Bearer | UserUpdateRequestDto | UserResponseDto |
| DELETE | /users/{id} | Elimina usuario | Bearer | - | 204/404 |
| GET | /users/{id} | Consulta por id | Bearer | - | UserResponseDto |
| GET | /users?page=&size= | Lista paginada | Bearer | - | UserPageResponseDto |

## 5. Modelo de Error (Propuesto)
```json
{
  "timestamp": "2025-09-06T12:34:56Z",
  "path": "/users",
  "error": "ValidationError",
  "message": "email must be valid",
  "details": ["email: must be a well-formed email address"],
  "traceId": "<opcional>"
}
```
Sugerencia: crear ExceptionMapper genérico.

## 6. Casos de Uso Técnicos (Secuencia Resumida)
1. Crear Usuario:
   - Resource valida DTO -> Service -> Repository construye UserRepresentation -> Keycloak -> retorna ID.
2. Actualizar Usuario:
   - Repository obtiene representación actual -> muta campos -> update.
3. Eliminar Usuario:
   - Repository invoca remove(); controla excepciones.
4. Listar Paginado:
   - Repository list(offset, size) + count() -> mapea a DTO.

## 7. Tecnologías
- Java 21, Quarkus 3.26.x
- Keycloak Admin Client (alinear versión con servidor: 21.1.1)
- Lombok, Bean Validation, SmallRye OpenAPI
- JUnit5, Mockito, Jacoco

## 8. Configuración / Variables Clave
`application.properties` (principales):
```
quarkus.http.port=8089
keycloak.auth-server-url=http://localhost:8083/
keycloak.realm=master
keycloak.resource=admin-cli
keycloak.credentials.secret=<secret>
keycloak.admin.username=admin
keycloak.admin.password=admin
quarkus.oidc.auth-server-url=http://localhost:8083/realms/<realm_app>
quarkus.oidc.client-id=<client_id>
quarkus.oidc.credentials.secret=<client_secret>
```

## 9. Ejecución Local
```bash
./mvnw quarkus:dev
# Probar endpoints (ejemplo crear):
curl -X POST http://localhost:8089/users \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","email":"demo@example.com","firstName":"De","lastName":"Mo","enabled":true,"password":"Password123!"}'
```

## 10. Seguridad
- Autenticación: Bearer Token via OIDC.
- Principio de mínimos privilegios (asignar solo roles necesarios al client).
- No almacenar secrets en repositorio (mover a vault/variables entorno en futuro).

## 11. Escalabilidad y Rendimiento
- Stateless; puede escalar horizontalmente.
- Bottleneck: latencia hacia Keycloak (considerar retry/backoff y circuit breaker si aumenta carga).
- Paginación implementada para GetAll.

## 12. Observabilidad (Pendiente)
- Agregar métricas (Micrometer / MP Metrics).
- Health checks adicionales (liveness a Keycloak). 
- Correlación de logs (traceId vía header). 

## 13. Testing
- Unit tests Service y Repository (mock admin client).
- Integración: perfil que apunte a Keycloak contenedor.
- Jacoco: definir umbral (ej. 70% líneas / 80% branches).

## 14. Roadmap Técnico
- Implementar ExceptionMapper global.
- Estándar de error JSON.
- Endpoint /health custom que valide reachability a Keycloak.
- Cache ligera para listados frecuentes.
- Añadir actualización de contraseña (reset credential).
- Automatizar build + análisis (CI con GitHub Actions).

## 15. Convenciones
- Interfaces prefijo `I`.
- DTO sufijo `Dto` (actual: `UserCreateRequestDto`, etc.).
- Paquete base: `com.fv.account.api.user`.
- Clases utilitarias sin estado (constructores privados si aplica).

## 16. Riesgos / Consideraciones
- Desalineación de versión Keycloak server vs client.
- Manejo de errores actual: lanza RuntimeException genérica (mejorar).
- Secrets en properties: migrar a variables de entorno.

## 17. Mejoras Futuras
- Implementar soporte para filtros (búsqueda por email / username).
- Rate limiting si se expone públicamente.
- Documentar políticas de password desde Keycloak para feedback validación previa.

---
Documento pensado para onboarding técnico rápido y soporte a agentes de automatización.