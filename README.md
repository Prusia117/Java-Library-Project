# Java Library Project

Mini framework en Java puro, escrito desde cero y sin frameworks externos de persistencia, más una aplicación de escritorio JavaFX (gestión de una biblioteca) construida encima de él.

El framework incluye:

- **ORM por reflexión**: mapea entidades Java a tablas y genera `SELECT`, `INSERT`, `UPDATE`, `DELETE` y `JOIN` sin anotaciones ni XML.
- **Sistema de migraciones**: DSL fluido para definir tablas y restricciones, con registro de migraciones ejecutadas y rollback.
- **Jartisan**: CLI inspirada en Laravel Artisan que genera entidades, modelos, controladores y migraciones. El código se arma en Java, sin motor de templates.

> Proyecto académico en equipo. El framework (`kernel/`) es el foco principal; la app JavaFX sirve como caso de uso real.

## Stack

Java 25 · JavaFX 21 · PostgreSQL · JDBC · Maven · Docker Compose · JUnit 5

## Arquitectura

```
src/main/java/com/library/
├── kernel/                     # El framework
│   ├── kernelSql/
│   │   ├── connection/         # ConnectionManager, drivers y propiedades de conexión
│   │   ├── orm/
│   │   │   ├── core/           # ORM, SQLBuilder, SQLExecutor, SQLResultMapper
│   │   │   ├── entity/         # Contrato Entity + mapeo/instanciación por reflexión
│   │   │   └── repository/     # Repository<T> genérico (CRUD)
│   │   └── migrator/           # DSL de DDL: Schema, Column, PK/FK/UNIQUE/CHECK
│   ├── jartisan/               # CLI: generador de archivos y ejecutor de migraciones
│   ├── framework/              # EnvReader, IModel, PasswordHasher
│   └── logger/
├── entities/                   # Entidades de la app (BookEntity, UserEntity)
├── models/                     # Lógica de negocio sobre Repository<T>
├── database/migrations/        # Migraciones de la app
├── controllers/                # Controladores JavaFX
└── applications/               # Ventanas / punto de entrada (Launcher)
```

## Ejemplos

### Entidad + repositorio

Las entidades son clases planas: cada campo público es una columna.

```java
public final class UserEntity implements Entity {
    public String tableName = "T_Users";
    public String primaryKey = "user_id";
    public boolean hasAutoIncrement = true;

    public Integer user_id;
    public String user_name;
    public String user_mail;
    public String user_password;
    // ...
}

Repository<UserEntity> users = new Repository<>(UserEntity.class);

users.getAll();                               // SELECT ... FROM T_Users ORDER BY user_id ASC;
users.getByField("user_mail", "ana@mail.com"); // ... WHERE user_mail = '...'
users.save(user);                              // INSERT (omite la PK autoincremental)
users.update(user, "7");                       // UPDATE solo con los campos no nulos
users.delete("7");
```

### Migración

```java
public class M20251111_141232335894400_UsersMigration extends Migration {

    @Override
    public ArrayList<Schema> up() {
        return new ArrayList<>(List.of(
                new Schema("T_Users")
                        .addColumn("user_id", SQLDataTypes.SERIAL).primaryKey()
                        .addColumn("user_name", SQLDataTypes.VARCHAR)
                        .addColumn("user_mail", SQLDataTypes.VARCHAR)
                        .addColumn("user_password", SQLDataTypes.VARCHAR)
        ));
    }

    @Override
    public ArrayList<String> rollback() {
        return new ArrayList<>(List.of("T_USERS"));
    }
}
```

Genera:

```sql
CREATE TABLE T_Users (user_id SERIAL NOT NULL, user_name VARCHAR NOT NULL, ...,
  CONSTRAINT t_users_user_id___pk PRIMARY KEY (user_id), ...);
```

### Jartisan (CLI)

```bash
./jartisan.sh Make Entity Author        # -> entities/AuthorEntity.java
./jartisan.sh Make Model Author         # -> models/AuthorModel.java
./jartisan.sh Make Controller Author    # -> controllers/AuthorController.java
./jartisan.sh Make Migration Authors    # -> database/migrations/M<timestamp>_AuthorsMigration.java

./jartisan.sh Migrations Execute        # Ejecuta las migraciones pendientes
./jartisan.sh Migrations Rollback       # Revierte las migraciones
```

En Windows: `.\jartisan.ps1 ...`

## Cómo correrlo

Requisitos: JDK 25 y Docker. Maven no hace falta porque el repo incluye el wrapper `mvnw`.

```bash
# 1. Variables de entorno
cp .env.example .env        # y edita las contraseñas

# 2. Base de datos (PostgreSQL + pgAdmin en http://localhost:5050)
docker compose up -d

# 3. Crear las tablas
./jartisan.sh Migrations Execute

# 4. Lanzar la app
./mvnw javafx:run
```

## Tests

```bash
./mvnw test
```

Cubren la generación de SQL del ORM, el DSL de migraciones, el lector de `.env` y el hash de contraseñas. No necesitan base de datos.

## Limitaciones conocidas / siguientes pasos

- Los valores se concatenan en el SQL; el siguiente paso es migrar `SQLExecutor` a `PreparedStatement` con parámetros.
- Las relaciones solo soportan `INNER JOIN`.
- Los archivos subidos (portadas y PDFs) se guardan en `src/main/resources/img/`; en una versión empaquetada deberían ir a un directorio de datos externo.

## Autores

- [Prusia117](https://github.com/Prusia117): framework (ORM, migraciones, Jartisan)
- [JoseP11](https://github.com/JoseP11) y [JavVM448](https://github.com/JavVM448): vistas y CRUDs de la app JavaFX

## Licencia

[MIT](LICENSE)
