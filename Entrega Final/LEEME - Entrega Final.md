# Entrega Final - TP Diseño de Sistemas de Información 2026 (Capit@l Humano)

Grupo: Fumis, Santiago - Paletto, Agustín - Pittavino, Franco - Valente, Thiago Mateo

Esta entrega reúne todas las etapas del TP con sus correcciones, como pide la cátedra. La Entrega 6 no tuvo
correcciones: se van a dar en el coloquio. El código fuente incluye todo lo diseñado: las clases del diagrama
de clases y todo lo necesario para los CU con diagrama de secuencia.

## Qué hay en esta carpeta

| Archivo | Para qué |
|---------|----------|
| `Diseño original/Entrega Final - ... .zip` | El zip de entrega, con la interfaz como en los wireframes de la Etapa 1. Coincide con el código del repo. |
| `Apunte para el coloquio.pdf` | Uso interno del grupo para preparar el coloquio. No se entrega. |

El zip no lleva ningún LEEME. Se descartó la variante con la interfaz renovada ("Diseño nuevo"): se entrega la
original.

## Contenido del zip

| Carpeta | Etapa | Qué tiene |
|---------|-------|-----------|
| `1 - Diseño de Entradas y Salidas` | 1 | Informe con los wireframes, la tabla de campos y la tabla de mensajes de los CU025, CU020, CU021, CU023, CU008, CU009, CU026, CU027 y CU028 |
| `2 y 3 - Diagrama de Clases` | 2 y 3 | Diagrama de clases final corregido (.puml, .png, .svg) |
| `4 - Diagrama Entidad Relacion` | 4 | DER corregido (.drawio, .png) e informe de justificación |
| `5 - Diagramas de Secuencia` | 5 | CU020, CU021 y CU025 corregidos (.puml, .png, .svg) |
| `6 - Despliegue y Diagramas de Secuencia` | 6 | Diagrama de despliegue, CU026 y CU028 (.puml, .png, .svg) |
| `7 y 8 - Implementacion` | 7 y 8 | Código fuente (`capital-humano/`) |

## Correcciones aplicadas

**Diagrama de clases (Etapas 2 y 3) y DER (Etapa 4)**
- "ModeloEvaluacion no es necesario, suma complejidad y no aporta": se quitó la clase del diagrama de clases y
  la entidad del DER.
- "CaracteristicaPuesto y PonderacionItem son relaciones N:M con atributos. Si lo dejan así, revisen los DAOs":
  quedaron como clases sin DAO propio. Se guardan y se leen a través de `PuestoDAO` (`buscarCaracteristicas`) y
  `PreguntaDAO` (`buscarPonderaciones`), en la misma transacción que su dueño.
- "¿OrdenMerito es entidad de negocio? No debe estar en el Gestor ni en la UI": `OrdenMerito` es una clase propia
  (lado abstracción del patrón Bridge, junto con `ReporteComparativo`). El gestor solo la arma y la pantalla solo la
  muestra. No se persiste: se calcula a partir de los cuestionarios cada vez que se emite.

**Diagramas de secuencia (Etapa 5)**
- CU020, "busca con un loop por factor, que no cubre la búsqueda solo por nombre": ahora es una sola consulta,
  `PreguntaDAO.buscarPorCriterios(idCompetencia, idFactor, nombre)`, con criterio "contiene" en el nombre.
- CU020, "falta el error si no se ingresa ningún criterio": se agregó el alternativo.
- CU021, "GestorCompetencias y GestorOpcionesRespuesta no acceden a sus DAO": se agregaron las llamadas a
  `CompetenciaDAO` y `OpcionRespuestaDAO`.
- CU025, "genera los cuestionarios dos veces": `generarCuestionarios` pasó a llamarse `generarClaves` y solo arma
  las claves que se muestran. Los cuestionarios se crean una sola vez, en `Evaluacion.crearCuestionarios`, al
  presionar "Finalizar".
- CU025, "evaluarCandidatos se usa con dos significados distintos": el método de `GestorCandidatos` pasó a
  llamarse `verificarCuestionariosActivos`.

**Ajustes de la implementación (Etapas 7 y 8)**
Al implementar aparecieron diferencias con el diseño: se quitó `ICuestionarioMediator`, los exportadores
reciben un `OutputStream`, hay un `CuestionarioDTO` nuevo, se agregó `EstadoFinal`, etc. Todas se pasaron al
diagrama de clases y a los de secuencia, así que diagramas y código coinciden. El detalle está en
`7 y 8 - Implementacion/LEEME - Notas de la Entrega 7 y 8.md`.

## Cómo ejecutar el programa

Requisitos: JDK 21, Maven 3.9+ y Docker.

    cd "7 y 8 - Implementacion/capital-humano"
    mvn package             # compila, corre los 16 tests y genera target/capital-humano.war
    docker compose up -d    # PostgreSQL 16 con esquema y datos iniciales + Tomcat 10.1

Abrir http://localhost:8080/capital-humano. Para entrar como consultor, el usuario es `jperez` y la contraseña
`consultor`.

La base arranca "de fábrica" (`sql/datos_iniciales.sql`): solo trae lo que el sistema no puede cargar porque sus
ABM están fuera del alcance (empresas, el consultor, opciones de respuesta, competencias, factores, puestos con sus
competencias y candidatos). No trae preguntas, evaluaciones, cuestionarios ni resultados: se generan usando el
sistema. Para evaluar un puesto (CU025), antes hay que dar de alta (CU021) al menos 2 preguntas en un factor de
cada competencia del puesto.

El zip descomprime en una carpeta `Entrega Final/` con rutas cortas, para no superar el límite de 260
caracteres de Windows.
