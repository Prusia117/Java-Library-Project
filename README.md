# Java Library Project

Descripción
-----------
Proyecto Java (Maven) con interfaz JavaFX para una librería. Contiene lógica de ejemplo, entidades, migraciones de base de datos y configuraciones de kernel/servicios.

Estructura principal
--------------------
- `pom.xml` — configuración Maven.
- `src/main/java/module-info.java` — módulo principal.
- Código fuente: `src/main/java/com/library/`
    - `App.java` — clase principal.
    - `PrimaryController.java`, `SecondaryController.java` — controladores JavaFX.
    - `config/DatabaseConfig.java` — configuración de BD.
    - `database/migrations/` — migraciones fuente.
    - `examples/` — entidades y ejemplos (e.g. `AutorEntity`, `BookEntity`).
    - `kernel/` — utilidades y framework interno.
- Recursos UI: `src/main/resources/com/library/primary.fxml`, `secondary.fxml`.
- Dependencias locales: `dependency/` (JavaFX jars, MongoDB, PostgreSQL, etc.).
- Compilado: `target/` (clases compiladas y migraciones compiladas).

Requisitos
---------
- JDK 11+ (probar con JDK 17+ recomendado).
- Maven 3.6+.
- JavaFX 13 (proporcionado en `dependency/`).
- Opcional: IntelliJ IDEA (proyecto ya abierto en IntelliJ).

Construir
--------
1. Limpiar y compilar:
   mvn clean package
2. Las clases compiladas aparecerán en `target/classes`.

Nota: adaptar `--module-path` a los JAR concretos de JavaFX incluidos en `dependency/`.

Dependencias notables (carpeta `dependency/`)
----------------------------------------------
- JavaFX: `javafx-base-13*.jar`, `javafx-controls-13*.jar`, `javafx-fxml-13*.jar`, `javafx-graphics-13*.jar`
- MongoDB: `mongodb-driver-sync-5.5.0.jar`, `mongodb-driver-core-5.5.0.jar`, `bson-5.5.0.jar`
- PostgreSQL: `postgresql-42.7.8.jar`
- Otras utilidades: `checker-qual-3.49.5.jar`, `bson-record-codec-5.5.0.jar`

Migraciones y datos
-------------------
- Migraciones fuente: `src/main/java/com/library/database/migrations/`
- Migraciones compiladas: `target/classes/com/library/database/migrations/`

Notas
-----
- Proyecto usa `module-info.java`: ejecutar con soporte de módulos o adaptar al classpath.
- Recursos FXML se encuentran en `src/main/resources/com/library/`.
- Si se desea empaquetar en un jar ejecutable con dependencias, añadir o configurar un plugin Maven (ej. `maven-assembly-plugin` o `javafx-maven-plugin`).

Contribución
------------
Abrir issues o pull requests. Mantener estilo Java y pruebas mínimas al añadir funcionalidades.
