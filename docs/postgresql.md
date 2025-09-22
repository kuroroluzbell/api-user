# PostgreSQL Integration

## Configuración de Base de Datos

La aplicación ahora soporta persistencia dual:
- **Keycloak**: Para autenticación y gestión de usuarios
- **PostgreSQL**: Para almacenamiento y consultas de datos

### Estructura de Base de Datos

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    keycloak_id VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    enabled BOOLEAN
);
```

### Configuración de Conexión

#### Local (.env)
```env
QUARKUS_DATASOURCE_DB_KIND=postgresql
QUARKUS_DATASOURCE_USERNAME=postgres
QUARKUS_DATASOURCE_PASSWORD=postgres
QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://localhost:5432/billpay_users
QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION=update
```

#### Docker (.env.docker)
```env
QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://postgres:5432/billpay_users
```

## Funcionamiento

### Operaciones CRUD

1. **CREATE**: Crea usuario en Keycloak → Guarda en PostgreSQL
2. **UPDATE**: Actualiza en Keycloak → Actualiza en PostgreSQL
3. **DELETE**: Elimina de Keycloak → Elimina de PostgreSQL
4. **READ**: Lee de Keycloak (fuente de verdad para datos de autenticación)

### Manejo de Errores

- Si falla PostgreSQL durante CREATE, se elimina el usuario de Keycloak (rollback)
- Las operaciones están envueltas en transacciones `@Transactional`
- Logs de errores para debugging

## Ejecución

### Desarrollo Local
```bash
# Iniciar PostgreSQL local en puerto 5432
./run.sh dev
```

### Docker
```bash
# Incluye PostgreSQL en el stack
./run.sh docker
```

## Ventajas de la Persistencia Dual

1. **Keycloak**: Autenticación, autorización, tokens
2. **PostgreSQL**: Consultas complejas, reportes, integraciones
3. **Consistencia**: Sincronización automática entre ambos sistemas
4. **Flexibilidad**: Cada sistema para lo que mejor hace

## Monitoreo

- Logs de transacciones en consola
- Verificación de sincronización entre sistemas
- Rollback automático en caso de fallos