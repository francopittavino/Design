# 0001 — Tecnología de implementación

Fecha: 2026-11-25
Estado: Aceptada

## Contexto
El enunciado deja elegir el lenguaje OO (con base relacional) pero exige que el desarrollo se adecúe
al diseño. El diagrama de despliegue de la Entrega 6 ya define Java 21 + Tomcat 10
(`capital-humano.war`) + PostgreSQL 16, y el diagrama de clases usa gestores singleton, DAOs con
interfaz + implementación y pantallas (boundary).

## Decisión
Java 21 con Jakarta Servlets + JSP sobre Tomcat 10, persistencia con JDBC en los `*DAOImpl`,
PostgreSQL 16 (levantado con Docker para que todo el grupo use la misma versión). Build con Maven.

## Consecuencias
- El código mapea 1:1 con el diagrama de clases y el de despliegue: no hay que modificarlos.
- Se descartó Spring Boot + JPA: agregaba clases del framework (repositorios, inyección) que se alejan
  de los gestores singleton y DAOs del diseño.
- Si en la implementación aparece alguna diferencia con los diagramas, se documenta en el LEEME de la
  Entrega 7 y 8 y se actualiza el diagrama.
