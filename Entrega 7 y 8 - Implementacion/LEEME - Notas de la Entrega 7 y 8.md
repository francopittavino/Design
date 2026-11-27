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

> **Nota (entrega final, decisión 0005):** estos datos de prueba ya no están en el repo. `sql/datos_prueba.sql` y
> `sql/generar_datos_prueba.py` se reemplazaron por `sql/datos_iniciales.sql`, que solo trae los catálogos, el
> consultor `jperez` y los candidatos. Lo de abajo queda como registro de cómo se probó en las Etapas 7 y 8; los
> archivos se pueden recuperar del historial de git.

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

El enunciado pide que el desarrollo se adecúe al diseño. Al implementar aparecieron las diferencias de
abajo y **ya se pasaron a los diagramas**: el diagrama de clases (`diagrama corregido.puml` en la raíz del
repo, con su imagen `DiagramaClasesTP2026.png/.svg`) y los de secuencia del CU020, CU021 y CU025
(carpeta de la Entrega 5) y del CU026 y CU028 (carpeta de la Entrega 6). Hoy el diagrama y el código
coinciden 1:1: todas las clases y todos los métodos del diagrama existen en el código (se verificó con un
script). Las únicas clases del código que no se dibujan son las de infraestructura técnica (ConexionBD,
Catalogo, Mapeos, Configuracion, PantallaBase, Modal, FiltroSesion y las excepciones); una nota del
diagrama lo aclara.

Los cambios fueron:

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

- Se agregaron al diagrama las implementaciones de los subsistemas externos que usa el TP:
  `AutenticacionLDAPSimulada`, `ServicioIANoDisponible`, `ImportadorCandidatosNoImplementado` y
  `ExportadorResultadosArchivo`.

**Diagramas de secuencia**
- CU020: una sola consulta `PreguntaDAO.buscarPorCriterios` (corrección del auxiliar, ver abajo).
- CU021: `crearPregunta(dto, usuario)`, `OpcionRespuestaDAO.buscarPorId` y el registro de auditoría.
- CU025: `GestorPuestos.obtenerDetalle`, `competenciasNoEvaluables` (9.A), `generarClaves`,
  `evaluarCandidatos(claves, puesto, consultor)` (vuelve a verificar que nadie tenga un cuestionario activo),
  `crearCuestionarios(claves, tiempoMax, accesosMaximos)`, `nombreArchivoClaves` y la exportación por `OutputStream`.
- CU026: `autenticarCandidato` devuelve `CuestionarioDTO`. `guardarRespuestas` vuelve a verificar
  el tiempo antes de guardar, por si se vence mientras el candidato responde. Al finalizar se verifica que
  no queden bloques sin responder y los puntajes se calculan sobre `getFactoresEvaluados`.
- CU028: el puesto se toma de la evaluación, el Bridge recibe los datos ya armados por sección
  (`Map<String, List<List<String>>>`) y `tieneCuestionariosCompletos()` lo consulta la pantalla después de emitir.

## Correcciones del auxiliar aplicadas

- **Diagrama de clases y DER** ("ModeloEvaluacion no es necesario"; "CaracteristicaPuesto y PonderacionItem
  son N:M con atributos, revisen los DAOs"): corregidos en la Entrega 6 y respetados en el código
  (sin `ModeloEvaluacion`, sin DAO propio para esas dos clases).
- **"¿OrdenMerito es entidad de negocio? No debe estar en el Gestor ni en la UI"**: `OrdenMerito` es una clase
  propia (paquete `reportes`, lado abstracción del Bridge). El gestor solo la arma y la pantalla solo la muestra.
  No se persiste: se calcula cada vez a partir de los cuestionarios.
- **Diagramas de secuencia de la Entrega 5** (CU020 buscar solo por nombre y error sin criterios; CU021 acceso
  a los DAOs; CU025 cuestionarios generados dos veces y `evaluarCandidatos` con dos significados): corregidos
  en los diagramas y en el código. El detalle está en el LEEME de la Entrega 5.

## Clases del diseño fuera del alcance de estas etapas

Para que el código tenga todo lo diseñado, las clases y métodos del diagrama de clases de los CU que no entran
en las Etapas 7 y 8 están como **esqueletos**: tienen sus atributos y firmas, pero el cuerpo lanza
`UnsupportedOperationException("CUxxx: fuera del alcance de las Etapas 7 y 8")`.

- CU006/007 (importar candidatos y exportar resultados), CU008 a CU019 y CU024/029 a CU031 (ABM de puestos,
  competencias, factores y opciones), CU022/023 (modificar y dar de baja pregunta), CU027 (reporte comparativo),
  CU032 (proceso batch: `GestorFinalizacion`, `InterfazBatch`) y CU033 (pregunta con IA).
- Las pantallas de esos CU extienden `PantallaBase` pero no tienen ruta (`@WebServlet`): en el menú se ven
  deshabilitadas.
- El LDAP está simulado y el servicio de IA siempre responde "no disponible" (mensaje del CU021).
