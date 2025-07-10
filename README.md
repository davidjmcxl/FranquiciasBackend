# Franquicias API

API REST para la gestión de franquicias, sucursales y productos. Desarrollada con Spring Boot y MongoDB.

---

## Características principales

* CRUD de franquicias, sucursales y productos.
* Actualización de nombre para franquicia, sucursal y producto (Plus ✅).
* Pruebas unitarias con JUnit y Mockito (Plus ✅).
* Documentación automática con Swagger (Springdoc).
* Empaquetado y despliegue con Docker (Plus ✅).
* Despliegue en la nube usando AWS Elastic Beanstalk (Plus ✅).
* Soporte para variables de entorno como `MONGO_URI` (Plus ✅).

---

## Endpoints principales

### Franquicias

* `POST /api/franquicias`: crear franquicia
* `GET /api/franquicias`: listar todas
* `PUT /api/franquicias/{id}/nombre`: actualizar nombre de franquicia ✅

### Sucursales

* `POST /api/franquicias/{franquiciaId}/sucursales`: agregar sucursal
* `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalNombreAntiguo}/nombre`: actualizar nombre de sucursal ✅

### Productos

* `POST /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos`: agregar producto
* `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombreAntiguo}/nombre`: actualizar nombre del producto ✅

---

## 🧪 Pruebas

Las pruebas unitarias se encuentran en el paquete `service` y `controller`, usando JUnit 5 y Mockito.

Ejecutar con:

```bash
mvn test
```

---

## 🐳 Docker

### Crear imagen

```bash
docker build -t franquicias-api:1.0 .
```

### Ejecutar contenedor

```bash
docker run -p 8080:8080 --env MONGO_URI=<tu-uri-mongodb> franquicias-api:1.0
```

---

##  Despliegue en AWS (Elastic Beanstalk)

1. Empaquetar el proyecto:

```bash
mvn clean package
```

2. Subir el archivo `.jar` generado a AWS Elastic Beanstalk (como archivo local o en S3).
3. Crear el entorno (Java 17, plataforma Tomcat).
4. Asignar las variables de entorno (`MONGO_URI`).
5. Desplegar.

---

## Cómo correr el proyecto en local

### 1. Requisitos

* Java 17
* Maven
* MongoDB Atlas o local

### 2. Clonar el proyecto

```bash
git clone https://github.com/tu-usuario/franquicias-api.git
cd franquicias-api
```

### 3. Configurar variable de entorno `MONGO_URI`

```bash
# Linux/macOS
export MONGO_URI="mongodb+srv://usuario:clave@cluster.mongodb.net/franquiciaDB"

# Windows CMD
set MONGO_URI=mongodb+srv://usuario:clave@cluster.mongodb.net/franquiciaDB
```

### 4. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

Swagger estará disponible en:

* [http://localhost:5000/swagger-ui.html](http://localhost:5000/swagger-ui.html)

---

## Autor

David Moreno

---

