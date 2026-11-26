# Entrega 5 - Diagramas de Secuencia (1ra entrega)

Fecha tope: 25/09/2026

Casos de uso: CU020 - Gestionar Preguntas, CU021 - Dar de alta Pregunta y CU025 - Evaluar a Candidatos.

Cada CU tiene su .puml y la imagen generada (.png y .svg).

Para regenerar las imagenes:

    java -DPLANTUML_LIMIT_SIZE=16384 -jar plantuml.jar -charset UTF-8 -tpng *.puml

(el LIMIT_SIZE hace falta porque el CU025 es largo y si no PlantUML lo corta)

Notas:
- El CU023 - Dar de baja Pregunta no va en esta entrega; quedo en la carpeta "_no pedido (CU023)".
- En el CU025, evaluarCandidatos() de GestorCandidatos se llama antes de elegir el puesto, por eso se pasa null.
  (Versión entregada. En la corrección se renombró a verificarCuestionariosActivos(), ver abajo.)

## Correcciones del auxiliar (aplicadas para la entrega final)

Los .puml y las imágenes de esta carpeta ya están corregidos. `Casos/` y `casos.zip` son la versión
que se entregó el 25/09.

1. **CU020: "Busca con un loop por factor (buscarPorFactor), que no cubre la búsqueda solo por nombre."**
   Ahora `GestorPreguntas.buscarPreguntas` hace una sola consulta,
   `PreguntaDAO.buscarPorCriterios(idCompetencia, idFactor, nombre)`, donde 0 o vacío significa "sin ese filtro"
   y el nombre se busca con criterio "contiene". Se cambió en el diagrama y en el código.
2. **CU020: "Falta el error si no se ingresa ningún criterio."** Se agregó el alternativo "Debe ingresar al menos
   un criterio de búsqueda" y también el aviso "No se encontraron preguntas".
3. **CU021: "GestorCompetencias y GestorOpcionesRespuesta no acceden a sus DAO."** Se agregaron
   `CompetenciaDAO.listarTodas()`, `OpcionRespuestaDAO.listarTodas()` y `OpcionRespuestaDAO.buscarPorId()`.
4. **CU025: "Genera los cuestionarios dos veces (generarCuestionarios antes de Finalizar y crearCuestionarios
   al finalizar)."** `generarCuestionarios` pasó a llamarse `generarClaves`: solo arma las claves que se muestran.
   Los cuestionarios se crean una sola vez, en `Evaluacion.crearCuestionarios`, al presionar "Finalizar".
5. **CU025: "evaluarCandidatos se usa con dos significados distintos."** El de `GestorCandidatos`, que solo
   verifica que nadie tenga un cuestionario activo, pasó a llamarse `verificarCuestionariosActivos(nros)`.
   `GestorEvaluacion.evaluarCandidatos` es el único que crea la evaluación.
6. La nota del CU025 ahora dice "salen de la misma evaluación" (antes decía "del mismo modelo").

Además se actualizaron los tres diagramas para que coincidan con la implementación de las Etapas 7 y 8
(ver el LEEME de la Entrega 7 y 8).
