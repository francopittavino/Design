# 0005 — Entrega final: interfaz original centrada, datos iniciales y zip con carpeta raíz corta

Fecha: 2026-11-27
Estado: Aceptada (reemplaza el punto 2 de la decisión 0004 y resuelve la elección que dejaba pendiente)

## Contexto
La decisión 0004 dejó dos variantes del zip ("Diseño original" y "Diseño nuevo") para elegir antes de subir.
Al verificar la entrega aparecieron además tres problemas:
- Las pantallas quedaban pegadas arriba, con mucho espacio vacío debajo.
- La base se cargaba con datos de prueba (preguntas, evaluaciones, cuestionarios y resultados ya armados).
- La carpeta raíz del zip era tan larga que, al descomprimirlo con el Explorador de Windows en una carpeta
  profunda, se perdían archivos `.java` por el límite de 260 caracteres y el código no compilaba.

## Decisión
1. Se entrega la interfaz original y se borra la variante "Diseño nuevo". Las pantallas se centran en la
   página (solo cambia el CSS: mismos campos, botones y distribución que los wireframes) y el nombre se escribe
   "Capital Humano", sin @.
2. La base se entrega "de fábrica": `sql/datos_iniciales.sql` reemplaza a `sql/datos_prueba.sql` y a
   `sql/generar_datos_prueba.py`. Solo trae lo que el sistema no puede cargar porque sus ABM están fuera del
   alcance: empresas, el consultor `jperez`, opciones de respuesta, competencias, factores, puestos con sus
   competencias y candidatos. Las preguntas, evaluaciones, cuestionarios y resultados se generan usando el sistema.
3. El zip descomprime en una carpeta `Entrega Final/` (ruta interna más larga: 126 caracteres) y los nombres van
   marcados como UTF-8.

## Consecuencias
- No cambia ningún diagrama: la interfaz solo cambia de aspecto y los datos iniciales no tocan el esquema.
- Para evaluar un puesto (CU025) primero hay que dar de alta (CU021) al menos 2 preguntas en un factor de cada
  competencia del puesto.
- Los casos de prueba del LEEME de la Entrega 7 y 8 ya no se pueden reproducir con la base entregada. Los
  archivos de datos de prueba se pueden recuperar del historial de git.
- Hay que revisar `Entrega Final/Apunte para el coloquio.pdf`, que puede mencionar los datos de prueba.
