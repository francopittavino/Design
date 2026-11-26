# 0002 — Ajustes al diseño surgidos en la implementación

Fecha: 2026-11-26
Estado: Aceptada

## Contexto
Al implementar los CU de las Etapas 7 y 8 aparecieron diferencias con el diagrama de clases y los de secuencia.
La decisión 0001 suponía que no iba a hacer falta modificar los diagramas.

## Decisión
- Se quita `ICuestionarioMediator`: la pantalla del CU026 habla directo con `GestorCuestionario`, como en los otros CU.
- Los exportadores, `imprimirOrdenMerito` y `exportarResultados` reciben un `OutputStream` en vez de una ruta:
  en una aplicación web el archivo se descarga y el usuario elige dónde guardarlo.
- `GestorCuestionario` trabaja con un `CuestionarioDTO` nuevo en lugar de `EvaluacionDTO`.
- Se agrega la clase abstracta `EstadoFinal`, y `guardarRespuestas` vuelve a verificar el tiempo antes de guardar.
- El resto de los ajustes (métodos de DAO, campos de DTOs, firmas) está listado en el LEEME de la Entrega 7 y 8.

## Consecuencias
- Hay que actualizar el diagrama de clases y los de secuencia de CU025, CU026 y CU028 antes de entregar.
- Reemplaza parcialmente la consecuencia de la 0001 que decía que no había que modificar los diagramas.
