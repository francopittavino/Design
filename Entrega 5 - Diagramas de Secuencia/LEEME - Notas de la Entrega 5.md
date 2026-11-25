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
