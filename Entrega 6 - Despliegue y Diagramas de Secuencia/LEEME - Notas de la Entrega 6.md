# Entrega 6 - Diagrama de Despliegue y Diagramas de Secuencia (2da entrega)

Fecha tope: 23/10/2026

Contenido:
- Diagrama de Despliegue
- CU026 - Completar cuestionario
- CU028 - Emitir orden de mérito
- Carpeta "Correcciones entrega anterior": diagrama de clases (.puml, .png, .svg) y DER (.drawio y .png) corregidos

Zip de entrega: "Entrega 6 - Fumis, Paletto, Pittavino, Valente.zip" (informe .pdf/.docx, diagramas .puml + .png, DER .drawio + .png).

Cada diagrama tiene su .puml y la imagen generada (.png y .svg). El informe (.docx y .pdf) tiene
la portada, las correcciones y los diagramas largos partidos en varias hojas, igual que en la Entrega 5.

Para regenerar las imagenes:

    java -DPLANTUML_LIMIT_SIZE=16384 -jar plantuml.jar -charset UTF-8 -tpng *.puml

El diagrama de despliegue usa `!pragma layout smetana`, así que no hace falta tener Graphviz instalado.
La imagen del diagrama de clases de la carpeta de correcciones también se generó con smetana
(sin Graphviz no se pueden usar las líneas ortogonales); si tienen Graphviz conviene regenerarla.

## Correcciones de la entrega anterior

1. **ModeloEvaluacion no es necesario**: se sacó la clase y la asociación con Evaluacion del
   diagrama de clases, y la entidad, sus atributos y la relación "utiliza" del DER.
2. **CaracteristicaPuesto y PonderacionItem (N:M con atributos)**: se dejaron como clases, sin DAO propio.
   - PuestoDAO: guardar/actualizar persisten las características del puesto; se agregaron
     `buscarCaracteristicas(idPuesto)` y `existePuestoConCompetencia(idCompetencia)`.
   - PreguntaDAO: guardar/actualizar persisten las ponderaciones; se agregaron
     `buscarPonderaciones(idPregunta)` y `existePreguntaConOpcion(idOpcion)`.
   - GestorCompetencias usa PuestoDAO (CU015, competencia en uso), GestorOpcionesRespuesta usa
     PreguntaDAO (CU031, opción en uso) y GestorReportes usa PuestoDAO (CU028).
   - El CU028 ya recupera las ponderaciones mínimas con `PuestoDAO.buscarCaracteristicas`.

Los archivos originales de la raíz (`diagrama corregido.puml` y `diagrama_entidad_relacion (2).drawio`)
también quedaron actualizados.

También se agregaron al diagrama de clases los métodos que usan el CU026 y el CU028
(GestorCuestionario, GestorReportes, Cuestionario, ResultadoCompetencia, OrdenMerito, Puesto)
y los campos que faltaban en PreguntaDTO (opciones) y PuestoDTO (cantidad de candidatos y de evaluaciones completas).

## Diagrama de despliegue

Se rehizo siguiendo la clase práctica del 16/10 (presentación de la cátedra):
- Forma descriptor, arquitectura web en 3 capas (como el "Ejemplo 2" de la clase).
- Adentro de los nodos solo van entornos de ejecución y artefactos (archivos), no clases ni módulos lógicos
  (antes teníamos "Pantallas", "Gestores", "DAOs" y "ServiciosExternosFacade", que la cátedra marca como error).
- Servidor de aplicaciones: JVM 21 > Tomcat 10 con `capital-humano.war` y `configuracion.properties` (CU026);
  en la misma JVM, `finalizar-cuestionarios.jar` (proceso batch del CU032).
- Servidor de BD: PostgreSQL 16 con `esquema_capital_humano.sql`.
- Cada camino de comunicación tiene protocolo y multiplicidad (en la etiqueta: "origen  destino").
- El Sistema de Gestión de Candidatos y CV se conecta con la PC del Consultor («archivo CSV»), no con el servidor:
  el enunciado dice que en el CU006 el consultor elige el CSV de una carpeta y en el CU007 se genera un archivo.
- LDAP («LDAP», CU001) y servicio de IA («HTTPS», CU033) conectados al servidor de aplicaciones.
- Impresora conectada a la PC del Consultor («USB», CU027/CU028).
- Tecnología: Java 21 + Tomcat 10 + PostgreSQL 16 (la que muestra la cátedra). Si en las Etapas 7 y 8 se usa
  otra, hay que cambiar los entornos de ejecución y los artefactos del diagrama.

## Notas de los diagramas

- CU026: los cambios de estado (Incompleto, Sin Contestar, Completo) los hace el estado actual
  (patrón State) y devuelve el resultado al cuestionario, que se lo devuelve al gestor. Recién ahí
  el gestor persiste (un solo canal de comunicación, sin notificarCambioEstado).
- CU026: el pasaje a "En Proceso" lo hace el gestor al aceptar las instrucciones (paso 3 del CU).
- CU026: el idCuestionario sale del cuestionarioDTO que el CU001 dejó en la sesión del candidato.
- CU026: el acceso se registra con `cuestionario.registrarAcceso()` (encapsula contador y fecha).
- CU026: el estado solo valida que se pueda responder; las RespuestaCandidata las crea el cuestionario.
- CU028: si el puesto no tiene evaluaciones se usa un alt que vuelve al paso 4 (antes era un loop).
- CU028: si ninguna evaluación seleccionada tiene cuestionarios completos se avisa al consultor
  (`OrdenMerito.tieneCuestionariosCompletos()`) y se emite igual, agrupado por estado.
- CU028: la impresión pasa por `GestorReportes.imprimirOrdenMerito`: el gestor crea el ExportadorPDF,
  se lo pasa al reporte con setFormato y el reporte le delega exportar() (patrón Bridge:
  jerarquía Reporte {OrdenMerito, ReporteComparativo} × jerarquía ExportadorFormato {PDF, Excel, CSV}).
- Diagrama de clases: se agregaron `Cuestionario.registrarAcceso()`, `PreguntaCuestionario.setRespuesta()`,
  `OrdenMerito.tieneCuestionariosCompletos()` y `GestorReportes.imprimirOrdenMerito()`.
- La nota del CU025 (Entrega 5) todavía dice "Todos los cuestionarios salen del mismo modelo";
  si se vuelve a entregar, cambiar por "salen de la misma evaluación".
