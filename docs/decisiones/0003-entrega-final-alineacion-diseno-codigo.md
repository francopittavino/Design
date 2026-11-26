# 0003 — Entrega final: alineación entre diseño y código

Fecha: 2026-11-26
Estado: Aceptada

## Contexto
La cátedra pide para la entrega final un zip con TODAS las entregas y sus correcciones (la Entrega 6 se corrige
en el coloquio), y un código fuente que contenga "todo lo diseñado previamente, clases y todo lo necesario para
las funcionalidades en las que hicieron diagramas de secuencia". El auxiliar además marcó correcciones en los
diagramas de secuencia de la Entrega 5 (CU020, CU021 y CU025). Al empezar la sesión, 23 clases del diagrama no
existían en el código y los diagramas todavía no reflejaban la decisión 0002.

## Decisión
1. Las clases y métodos de los CU que no entran en las Etapas 7 y 8 se agregan como **esqueletos**: atributos y
   firmas como en el diagrama, con el cuerpo `throw new UnsupportedOperationException("CUxxx: fuera del alcance...")`.
   Las pantallas de esos CU extienden `PantallaBase` pero no tienen `@WebServlet`, así que la aplicación no cambia.
2. Los diagramas (de clases y de secuencia) se actualizan para que coincidan con el código. Las versiones que se
   entregaron se conservan en sus carpetas: `Casos/` y `casos.zip` de la Entrega 5, la copia de la Entrega 6 y los
   originales de las Entregas 2 a 4.
3. Las correcciones del CU020 (búsqueda en una sola consulta) y del CU025 (`generarClaves`,
   `verificarCuestionariosActivos`) se aplican **también en el código**, no solo en los diagramas.
4. El zip final lleva solo la versión corregida de cada etapa. No incluye los informes .docx/.pdf de las
   Entregas 5 y 6, que tienen las imágenes viejas de los diagramas.

## Consecuencias
- Cambian el diagrama de clases, los cinco diagramas de secuencia y los LEEME de las Entregas 5 y 7-8.
- Las clases de infraestructura (ConexionBD, Catalogo, Mapeos, Configuracion, PantallaBase, Modal, FiltroSesion,
  excepciones) quedan fuera del diagrama, con una nota que lo aclara.
- Si en el futuro se implementa un CU que hoy está como esqueleto, la clase y su lugar en el diseño ya existen:
  alcanza con escribir el cuerpo y su diagrama de secuencia.
