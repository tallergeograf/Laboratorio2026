# 🌍 GIS Enterprise Address Processing API

## 📌 Descripción del proyecto

Este proyecto corresponde a un backend desarrollado como parte de la materia  
**Taller de Sistemas de Información Geográficos Empresariales**.

El sistema expone una **API REST** construida con Spring Boot que permite:

- Procesar direcciones almacenadas en una base de datos propia
- Integrar servicios de geocoding externos
- Retornar información geográfica asociada (coordenadas, métricas, etc.)
- Servir como base para análisis y consultas de datos espaciales

El objetivo del sistema es centralizar el procesamiento de direcciones y su conversión a información geográfica utilizable en aplicaciones GIS.

---

## 🧰 Tecnologías utilizadas

- Java 25
- Maven
- Spring Boot
- PostgreSQL

---

## 📦 Dependencias principales

### 🚀 Spring Web
Permite crear APIs REST utilizando controladores (`@RestController`) y endpoints HTTP.

### 🔄 Spring Boot DevTools
Herramienta de desarrollo que permite:
- Reinicio automático de la aplicación al detectar cambios en el código
- Mejor experiencia de desarrollo
- Desactivación de cache en entorno local

### 🗄️ Spring Data JPA
Permite interactuar con la base de datos utilizando entidades Java, evitando SQL manual mediante ORM (Hibernate).

### 🐘 PostgreSQL Driver
Driver JDBC necesario para la conexión entre la aplicación y la base de datos PostgreSQL.

---

## 🏗️ Arquitectura del proyecto

El sistema sigue una arquitectura en capas:

- **Controller** → expone endpoints REST
- **Service** → lógica de negocio (geocoding, procesamiento)
- **Repository** → acceso a base de datos
- **Entity** → modelo de datos persistente

---

## ▶️ Cómo correr el proyecto en local

### 📌 Requisitos previos

Antes de ejecutar el proyecto, asegurarse de tener instalado:

- Java 25
- Maven 3.8+
- PostgreSQL en ejecución
- Una base de datos creada para el proyecto

---