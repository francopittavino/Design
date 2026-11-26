# Sesión 2026-11-26 16:09 — Verificación de la entrega final

Integrantes: Fumis
Objetivo: Verificar que la entrega final esté lista y preparar el coloquio
Entrega: Final (zip armado en 9ec7367); queda el coloquio, sin fecha todavía

## Qué se hizo
- Se comparó el zip `Entrega Final/Entrega Final - Fumis, Paletto, Pittavino, Valente.zip` contra el repo:
  - `src/` es idéntico a `capital-humano/src`, salvo los finales de línea.
  - El diagrama de clases es igual a `diagrama corregido.puml`.
  - CU020, CU021 y CU025 son las versiones corregidas, no las de `Casos/`.
  - CU026, CU028 y el despliegue son iguales a los de la Entrega 6.
  - El zip tiene 175 archivos, como dice su LEEME.
- Se probó el zip de punta a punta, extrayéndolo en `C:\tp` y siguiendo los pasos de su LEEME:
  - `mvn package` con JDK 25: BUILD SUCCESS, 16 tests en verde.
  - `docker compose up -d`: la app levanta.
  - Login: usuario inválido, `jperez`/`consultor`, y el menú con los CU fuera del alcance deshabilitados.
  - CU020: sin criterios, solo por nombre, por competencia, sin resultados, CU022/023 "no disponible".
  - CU021: obligatorios, duplicado por nombre, por enunciado y por ambos, suma distinta de 10, valores negativos,
    IA no disponible, alta OK con registro de auditoría, y vuelta a la grilla.
  - CU025: 4.A, 6.A, 10.A y 9.A (JC-01 deja de fallar después de dar de alta una pregunta en NEGOCIACIÓN).
    Los cuestionarios se crean una sola vez, al finalizar: había 10, siguieron 10 después de generar las claves
    y pasaron a 12. El Excel de claves se descarga y es válido.
  - CU026: clave inválida, instrucciones, 6.A, cuestionario completo, 1.A (INCOMPLETO), 1.B (SIN CONTESTAR),
    1.C (reingreso en el bloque 2) y un candidato COMPLETO que no puede reingresar.
  - CU028: sin criterios, 6.A AN-FUN, falta de selección, emitir TODAS, PDF. Lo completado en el CU026 aparece
    en el orden de mérito.
- No se modificó nada del repo.
- Al cerrar, entraron los commits de Pittavino de las 16:33 (decisión 0004): reemplazan ese zip por dos zips sin
  LEEME (`Diseño original/` y `Diseño nuevo/`). La prueba de punta a punta se hizo sobre el zip anterior. A los
  zips nuevos solo se les revisaron los nombres y las rutas, no se probaron funcionando.

## Decisiones
- Ninguna. Las correcciones de abajo quedan para decidir con el grupo.

## Archivos tocados
- Solo esta entrada de bitácora.

## Pendientes
- **Rutas largas en el zip (importante):** al extraerlo con el Explorador de Windows en una carpeta profunda,
  se pierden sin aviso `facade/ImportadorCandidatosNoImplementado.java` y
  `entidades/estados/EstadoCuestionarioState.java`, que llegan a 260 caracteres, y el código no compila.
  **Sigue pasando en los dos zips nuevos** de la decisión 0004: tienen la misma carpeta raíz larga y además
  quedan dentro de `Diseño original/` y `Diseño nuevo/`. Propuesta: acortar el nombre de la carpeta raíz del zip
  (por ejemplo `Entrega Final/`) antes de subir el que se elija.
- **PDF del orden de mérito:** INCOMPLETO y SIN CONTESTAR se rotulan los dos "C" (`reportes/OrdenMerito.java:70`).
  Además, en pantalla esos grupos van dentro de la sección B y en el PDF salen como secciones aparte.
- ~~LEEME de la Entrega 7 y 8 dentro del zip con ubicaciones del repo~~: ya no aplica, los zips nuevos no
  llevan LEEME (decisión 0004).
- **Menor:** una ponderación no numérica muestra "campo obligatorio" en vez de "valor inválido".
- **Menor:** los nombres del zip no tienen la marca UTF-8. "Diseño" se ve mal fuera de Windows. Sigue igual en los
  zips nuevos.
- **Coloquio:** Pittavino ya armó `Entrega Final/Apunte para el coloquio.pdf`. Falta revisarlo.
