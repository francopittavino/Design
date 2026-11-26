# Sesión 2026-11-26 10:54 — Implementación de los CU (Etapas 7 y 8)

Integrantes: Fumis
Objetivo: Terminar el TP: implementar CU020, CU021, CU025, CU026 y CU028 con documentación y programa funcionando.
Entrega: 7 y 8 (juntas; topes 06/11 y 20/11/2026, vencidos)

## Qué se hizo
- Proyecto `capital-humano/` (Maven war, Java 21): Servlets + JSP/JSTL sobre Tomcat 10.1, JDBC, PostgreSQL 16.
  `docker-compose.yml` levanta la base (con esquema y datos de prueba) y Tomcat, igual que el diagrama de despliegue.
- Esquema SQL armado desde el DER corregido de la Entrega 6, más un generador de datos de prueba que cubre
  los flujos alternativos de cada CU.
- Capas según el diagrama de clases: entidades, patrón State (5 estados + EstadoFinal), DTOs, DAOs (interfaz + JDBC),
  gestores singleton, Bridge (orden de mérito × PDF/Excel/CSV), Facade (LDAP simulado, IA no disponible).
- Pantallas siguiendo los wireframes de la Entrega 1: CU020, CU021, CU025, CU026 y CU028, con sus flujos alternativos.
- Cada CU se probó de punta a punta, incluidos los alternativos, y los puntajes del CU026 se verificaron en la base.
  Revisión visual en el navegador.
- 16 tests unitarios: puntajes, estados del cuestionario, orden de mérito y claves.
- Carpeta `Entrega 7 y 8 - Implementacion/` con el LEEME y los wireframes de la Entrega 1 (ya están en el repo).

## Decisiones
- Diferencias entre el diseño y la implementación: ver `docs/decisiones/0002-ajustes-diseno-en-implementacion.md`
  y el LEEME de la entrega.

## Archivos tocados
- `capital-humano/**` (nuevo), `Entrega 7 y 8 - Implementacion/**` (nuevo), `.gitignore`

## Pendientes
- Actualizar el diagrama de clases y los de secuencia de CU025, CU026 y CU028 según las diferencias del LEEME.
- Armar el informe y el zip de la Entrega 7 y 8.
- Corregir la nota del CU025 (Entrega 5): "salen de la misma evaluación" (sigue pendiente de la sesión anterior).
