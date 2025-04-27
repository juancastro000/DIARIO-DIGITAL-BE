# 📝 Digital Diary Backend
🚀 REST API para una aplicación de diario personal, construida con Spring Boot, JPA/Hibernate y JWT.

---

## 🛠️ Tech Stack

### 🔹 Backend
- **Java 21**
- **Spring Boot 3.x** (Web, Data JPA, Security)
- **Hibernate**
- **JPA (H2, MySQL)**
- **JWT** (Nimbus JOSE + JWT)
- **Testing**: JUnit5, Mockito, MockMvc

### 🔹 Database
- **H2** (perfil `dev`, en memoria)
- **MySQL 8** (perfil `mysql`)

---



---

## 📦 Entidades Principales

- **User** (`users`)
  - `id_user`, `user_name`, `password`, `user_email`

- **Tag** (`tags`)
  - `id`, `name` (ENUM: PERSONAL, TRABAJO, SALUD, OCIO, FINANZAS, APRENDIZAJE, FAMILIA, DEPORTE)

- **Entry** (`entries`)
  - `id_entry`, `content`, `date`, `mood`, `productivity`, `user_id`
  - Relación `@ManyToMany` con `tags` vía `entry_tags`

---

## ⚙️ Configuración y Perfiles

### application.properties (común)
```properties
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}
api-endpoint=${api-endpoint:/api}
jwt.secret=${JWT_SECRET:ChangeMe123!}
jwt.expirationMs=${JWT_EXPIRATION_MS:3600000}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### application-dev.properties (H2)
```properties
spring.profiles=dev
spring.datasource.url=jdbc:h2:mem:diary;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.datasource.initialization-mode=always
spring.sql.init.data-locations=classpath:data.sql
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
``` 

### application-mysql.properties (MySQL)
```properties
spring.profiles=mysql
spring.datasource.url=jdbc:mysql://localhost:3306/digital_diary?useSSL=false&serverTimezone=UTC
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.datasource.initialization-mode=never
``` 

---

## 📑 Inicialización de Datos (dev)

**data.sql** carga al arrancar (H2):
```sql
-- Usuario demo
INSERT INTO users (id_user, user_name, password, user_email) VALUES
  (1,'demo','$2a$10$7EqJtq98hPqE…','demo@example.com');

-- Tags
INSERT INTO tags (id,name) VALUES
  (1,'PERSONAL'),(2,'TRABAJO'),…,(8,'DEPORTE');

-- Entradas (20)
INSERT INTO entries (id_entry,content,date,mood,productivity,user_id) VALUES
  (1,'Entrada 1','2025-04-01 08:00','HAPPY','3',1),…,(20,'Entrada 20','2025-04-20 17:30','SAD','1',1);

-- Relación entry_tags
INSERT INTO entry_tags (entry_id,tag_id) VALUES
  (1,1),(1,2),…,(20,4),(20,5);
```

---

## 🚀 Endpoints de la API

Base: `{{api-endpoint}}` (/api por defecto)

| Método  | Ruta                  | Descripción                |
|---------|-----------------------|----------------------------|
| POST    | `/auth/register`      | Registrar usuario          |
| POST    | `/auth/login`         | Login → JWT                |
| POST    | `/entry`              | Crear entrada (JWT)        |
| GET     | `/entry`              | Listar entradas (JWT)      |
| GET     | `/entry/{id}`         | Obtener por id (JWT)       |
| PUT     | `/entry/{id}`         | Actualizar (JWT)           |
| DELETE  | `/entry/{id}`         | Eliminar (JWT)             |

> **JWT** en header: `Authorization: Bearer <token>`

---

## 🔒 Seguridad (JWT)

1. `JwtUtil` genera/valida HS512 (Nimbus)
2. `JwtAuthenticationFilter` extrae token y setea `SecurityContext`
3. Rutas públicas: `/auth/**`, `/public/**`, `/h2-console/**`
4. Rutas protegidas: `/entry/**`

---

## 🧪 Ejecución y Pruebas

```bash
# Dev (H2)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# MySQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

# Tests
./mvnw test
```

En VS Code (`.vscode/launch.json`):
```json
{
  "args": ["--spring.profiles.active=dev"],
  "env": { /* variables */ }
}
```

---

## ✉️ Contacto

**Juan Castro**  
🔗 [GitHub/jcastro](https://github.com/juancastro000)  
🔗 [LinkedIn/jcastro](www.linkedin.com/in/juan-esteban-castro)  

¡Gracias por usar **Digital Diary Backend**!  
