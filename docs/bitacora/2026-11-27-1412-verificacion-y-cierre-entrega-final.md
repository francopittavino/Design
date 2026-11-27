# Sesión 2026-11-27 14:12 — Verificación y cierre de la entrega final

Integrantes: Pittavino
Objetivo: Verificar la entrega final y dejarla lista para subir
Entrega: Final; queda el coloquio, sin fecha todavía

## Qué se hizo
- Se compararon los dos zips de la decisión 0004 contra el repo: el código y los diagramas coincidían, y entre
  sí solo diferían en los 5 archivos de la interfaz.
- Se descartó el "Diseño nuevo": se entrega la interfaz original.
- Las pantallas quedan centradas en la página (solo CSS; mismos campos y botones que los wireframes). El menú
  principal pasa a una grilla de 3x3.
- "Capit@l Humano" pasa a escribirse "Capital Humano" en la interfaz, `web.xml`, `pom.xml`, el comentario del
  esquema y la empresa de la consultora.
- La base pasa a ser "de fábrica" (`sql/datos_iniciales.sql`): sin preguntas, evaluaciones, cuestionarios,
  resultados ni auditoría; con empresas, el consultor `jperez`, catálogos, puestos y candidatos. Se borraron
  `sql/datos_prueba.sql` y `sql/generar_datos_prueba.py`.
- El zip se rearmó con la carpeta raíz `Entrega Final/` (ruta interna más larga: 126 caracteres) y los nombres
  en UTF-8. Resuelve dos pendientes del 26/11: la pérdida de archivos por rutas largas y los acentos.
- Pruebas (no hay Docker en esta máquina: se usaron JDK 21, Maven, PostgreSQL 16 y Tomcat 10.1 portables):
  - Con los datos de prueba anteriores: 62 de 63 casos de punta a punta OK sobre CU020, CU021, CU025, CU026 y
    CU028. El que falla es "Salir", por un error del script de prueba; a mano funciona.
  - Con la base de fábrica, compilando desde el zip descomprimido: `mvn package` con 16 tests OK y 16 de 16
    pasos OK (alta de 6 preguntas, evaluación, cuestionario completo, orden de mérito y PDF).

- Se agregó `COMO EJECUTAR.txt` dentro del zip (`7 y 8 - Implementacion/`).
- Login (CU001 - Autenticar usuario, fuera del alcance de las etapas 7 y 8): se aclaró en el LEEME y en el txt, y
  se ajustó a los requerimientos especiales del enunciado. El usuario va siempre en mayúsculas, con un máximo de
  15 caracteres, y la contraseña con un máximo de 20. Se probaron `jperez`, `JPEREZ` y `JpErEz`, que entran; una
  contraseña mala, un usuario inexistente y los campos vacíos dan el mensaje de error. Con el zip rearmado:
  16 tests OK y 16 de 16 pasos OK.

## Decisiones
- 0005: interfaz original centrada, datos iniciales sin datos de prueba y zip con carpeta raíz corta.

## Archivos tocados
- `capital-humano/src/main/webapp/`: `css/estilos.css`, `WEB-INF/jsp/_encabezado.jspf`, `login.jsp`,
  `menu.jsp`, `WEB-INF/web.xml`.
- `capital-humano/`: `pom.xml`, `docker-compose.yml`, `sql/esquema_capital_humano.sql` (comentario),
  `sql/datos_iniciales.sql` (nuevo); se borraron `sql/datos_prueba.sql` y `sql/generar_datos_prueba.py`.
- `Entrega Final/`: zip de `Diseño original/` rearmado, `Diseño nuevo/` borrado, `LEEME - Entrega Final.md`.
- `Entrega 7 y 8 - Implementacion/LEEME - Notas de la Entrega 7 y 8.md`: nota sobre los datos de prueba.

## Pendientes
- PDF del orden de mérito: INCOMPLETO y SIN CONTESTAR se rotulan los dos "C" (`reportes/OrdenMerito.java:70`).
- Una ponderación no numérica escrita en el navegador muestra "campo obligatorio" en vez de "valor inválido".
- Revisar `Entrega Final/Apunte para el coloquio.pdf`: puede mencionar los usuarios y candidatos de prueba.
