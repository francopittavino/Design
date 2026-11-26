# Entrega 7 y 8 - Implementación

Fechas tope: 06/11/2026 (Etapa 7) y 20/11/2026 (Etapa 8). Se entregan juntas.

Casos de uso implementados:

| CU | Nombre | Etapa |
|----|--------|-------|
| CU020 | Gestionar Preguntas | 7 y 8 |
| CU021 | Dar de alta Pregunta | 8 |
| CU025 | Evaluar a Candidatos | 8 |
| CU026 | Completar Cuestionario | 8 |
| CU028 | Emitir Orden de Mérito | 7 y 8 |

El código fuente está en `capital-humano/` (raíz del repo). Los wireframes que siguen las pantallas
están en `Wireframes - Entrega 1.pdf`, en esta misma carpeta.

## Tecnología (igual al diagrama de despliegue)

- Java 21, Jakarta Servlets 6 + JSP/JSTL en Tomcat 10.1 (capa de presentación y de negocio en el mismo war).
- PostgreSQL 16 con JDBC (DAO = interfaz + implementación JDBC).
- OpenPDF para imprimir el orden de mérito y Apache POI para el Excel de claves (patrón Bridge).
- Patrones: Singleton (gestores), State (estados del cuestionario), Bridge (reporte × formato),
  Facade (servicios externos: LDAP simulado, IA no disponible, exportador de resultados), DAO, DTO.

## Cómo ejecutarlo

Requisitos: JDK 21 o superior, Maven 3.9+ y Docker.

    cd capital-humano
    mvn package                 # compila, corre los tests y genera target/capital-humano.war
    docker compose up -d        # levanta PostgreSQL (con esquema y datos de prueba) y Tomcat

Abrir http://localhost:8080/capital-humano

- Para volver a la base inicial: `docker compose down -v && docker compose up -d`.
- Después de recompilar: `docker compose up -d --force-recreate app` (si solo se reinicia,
  Tomcat puede seguir usando los JSP compilados de antes).

## Usuarios y datos de prueba

Consultores (el LDAP está simulado, la contraseña de todos es `consultor`): `jperez`, `mgomez`.

Candidatos con cuestionario para el CU026:

| Tipo y nro. doc. | Clave | Para probar |
|------------------|-------|-------------|
| LE 7654321 | AB12CD34 | Flujo normal (cuestionario Activo) |
| DNI 41888999 | QW7ER5TY | Flujo normal (cuestionario Activo) |
| DNI 40555666 | Z9Y8X7NP | 1.A: estaba En Proceso y se venció el tiempo, pasa a Incompleto |

Para el 1.B (Activo que pasa a Sin Contestar) hay que envejecer la fecha a mano:
`update cuestionario set fecha_cambio_estado = now() - interval '8 days' where id_cuestionario = <id>;`

Otros casos que traen los datos:

- CU025: MEDINA, HERRERA, SUAREZ y GIMENEZ están libres para evaluar. BENITEZ y ACOSTA tienen un cuestionario
  activo (6.A). El puesto JC-01 JEFE DE COMPRAS tiene la competencia NEGOCIACIÓN sin factores evaluables (9.A).
- CU028: el puesto 082 ENCARGADO tiene candidatos que alcanzan y no alcanzan los mínimos, Incompleto y
  Sin Contestar. AN-FUN ANALISTA FUNCIONAL no tiene evaluaciones (6.A).
- CU021: NEGOCIACIÓN tiene una sola pregunta, sirve para probar el alta y que después sea evaluable.

## Tests

`mvn test` corre 16 tests unitarios sin base de datos: algoritmo de puntajes (factor y competencia),
transiciones de estado del cuestionario (tiempo, accesos, días sin contestar), orden de mérito
(ranking, grupos y delegación al exportador) y generación de claves.

## Diferencias entre el diseño y la implementación

El enunciado pide que el desarrollo se adecúe al diseño. Estas son las diferencias, con el diagrama que
hay que actualizar:

**Diagrama de clases**
- `Cuestionario.accesosMaximos`: estaba en el DER pero faltaba en el diagrama de clases.
- Se agrega la clase abstracta `EstadoFinal` (Completo, Incompleto y Sin Contestar comparten el
  comportamiento) y `EstadoCuestionarioState.desdeNombre()` para reconstruir el estado desde la base.
- `Evaluacion.crearCuestionarios(claves, tiempoMax, accesosMaximos)` recibe parámetros.
- DTOs nuevos: `CuestionarioDTO` (GestorCuestionario lo recibe en lugar de `EvaluacionDTO`),
  `ItemOpcionRespuestaDTO`, `CaracteristicaPuestoDTO`. DTOs con campos agregados: `PuestoDTO`,
  `EvaluacionDTO`, `PreguntaDTO`.
- DAOs: `PuestoDAO.contarCandidatos/contarEvaluacionesCompletas`, `FactorDAO.listarTodos`,
  `PreguntaDAO.buscarPorCriterios`, `ConsultorDAO.buscarPorUsuario`.
- `GestorPuestos.obtenerDetalle` y `GestorEvaluacion.competenciasNoEvaluables` son nuevos.
  `GestorPreguntas` usa `FactorDAO` y `OpcionRespuestaDAO`.
- Se quitó `ICuestionarioMediator`: la pantalla habla directo con `GestorCuestionario`, como en los
  otros CU.
- Exportadores, `imprimirOrdenMerito` y `exportarResultados` reciben un `OutputStream` en vez de una ruta,
  porque en una aplicación web el archivo se descarga y el usuario elige dónde guardarlo.

**Diagramas de secuencia**
- CU025: `generarCuestionarios` y `evaluarCandidatos` cambian de firma (ver arriba).
- CU026: `autenticarCandidato` devuelve `CuestionarioDTO`. `guardarRespuestas` vuelve a verificar
  el tiempo antes de guardar, por si se vence mientras el candidato responde.
- CU028: el Bridge recibe los datos ya armados por sección (`Map<String, List<List<String>>>`).

**Fuera de alcance**
- El LDAP está simulado y el servicio de IA siempre responde "no disponible" (mensaje del CU021).
- No se implementaron el reporte comparativo, el importador de candidatos ni los métodos de DAO de los
  CU que no entran en estas etapas. En el menú se ven deshabilitados.
