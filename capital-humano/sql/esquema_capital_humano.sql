-- ============================================================
-- Capital Humano - esquema_capital_humano.sql (PostgreSQL 16)
-- Generado a partir del DER corregido de la Entrega 6
-- (Entrega 6/Correcciones entrega anterior/Diagrama Entidad Relacion.drawio).
-- Las entidades débiles del DER (CaracteristicaPuesto, Factor, Pregunta,
-- PonderacionItem, ItemOpcionDeRespuesta, Cuestionario, PreguntaCuestionario,
-- RespuestaCandidata, ResultadoCompetencia, ResultadoFactor) llevan FK
-- NOT NULL a su entidad dueña.
-- ============================================================

CREATE TABLE empresa (
    id_empresa      SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE consultor (
    id_consultor    SERIAL PRIMARY KEY,
    id_empresa      INTEGER      NOT NULL REFERENCES empresa(id_empresa),
    usuario         VARCHAR(50)  NOT NULL UNIQUE,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE candidato (
    id_candidato     SERIAL PRIMARY KEY,
    nro_candidato    VARCHAR(20)  NOT NULL UNIQUE,
    apellido         VARCHAR(100) NOT NULL,
    nombre           VARCHAR(100) NOT NULL,
    tipo_doc         VARCHAR(3)   NOT NULL CHECK (tipo_doc IN ('DNI','LE','LC','PP')),
    nro_doc          VARCHAR(20)  NOT NULL,
    fecha_nacimiento DATE,
    genero           CHAR(1)      CHECK (genero IN ('H','M')),
    nacionalidad     VARCHAR(50),
    email            VARCHAR(100),
    escolaridad      VARCHAR(100),
    activo           BOOLEAN      NOT NULL DEFAULT TRUE,
    UNIQUE (tipo_doc, nro_doc)
);

CREATE TABLE puesto (
    id_puesto       SERIAL PRIMARY KEY,
    id_empresa      INTEGER      NOT NULL REFERENCES empresa(id_empresa),
    codigo          VARCHAR(20)  NOT NULL UNIQUE,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(300),
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_baja      DATE
);

CREATE TABLE competencia (
    id_competencia  SERIAL PRIMARY KEY,
    codigo          VARCHAR(20)  NOT NULL UNIQUE,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(300),
    tipo            VARCHAR(15)  NOT NULL CHECK (tipo IN ('TECNICA','ACTITUDINAL')),
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Relación N:M Puesto-Competencia con atributo
CREATE TABLE caracteristica_puesto (
    id_caracteristica  SERIAL PRIMARY KEY,
    id_puesto          INTEGER      NOT NULL REFERENCES puesto(id_puesto),
    id_competencia     INTEGER      NOT NULL REFERENCES competencia(id_competencia),
    ponderacion_minima NUMERIC(4,2) NOT NULL CHECK (ponderacion_minima BETWEEN 0 AND 10),
    UNIQUE (id_puesto, id_competencia)
);

CREATE TABLE factor (
    id_factor       SERIAL PRIMARY KEY,
    id_competencia  INTEGER      NOT NULL REFERENCES competencia(id_competencia),
    codigo          VARCHAR(20)  NOT NULL,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(300),
    nro_orden       INTEGER      NOT NULL,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_baja      DATE,
    UNIQUE (id_competencia, codigo)
);

CREATE TABLE opcion_de_respuesta (
    id_opcion       SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL UNIQUE,
    descripcion     VARCHAR(300),
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE item_opcion_de_respuesta (
    id_item         SERIAL PRIMARY KEY,
    id_opcion       INTEGER      NOT NULL REFERENCES opcion_de_respuesta(id_opcion),
    orden           INTEGER      NOT NULL,
    descripcion     VARCHAR(200) NOT NULL,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    UNIQUE (id_opcion, orden)
);

CREATE TABLE pregunta (
    id_pregunta     SERIAL PRIMARY KEY,
    id_factor       INTEGER      NOT NULL REFERENCES factor(id_factor),
    id_opcion       INTEGER      NOT NULL REFERENCES opcion_de_respuesta(id_opcion),
    nombre          VARCHAR(200) NOT NULL,
    enunciado       VARCHAR(500) NOT NULL,
    descripcion     VARCHAR(300),
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_baja      DATE
);

-- Relación N:M Pregunta-ItemOpcionDeRespuesta con atributo
CREATE TABLE ponderacion_item (
    id_ponderacion  SERIAL PRIMARY KEY,
    id_pregunta     INTEGER      NOT NULL REFERENCES pregunta(id_pregunta),
    id_item         INTEGER      NOT NULL REFERENCES item_opcion_de_respuesta(id_item),
    valor           NUMERIC(4,2) NOT NULL CHECK (valor >= 0),
    UNIQUE (id_pregunta, id_item)
);

CREATE TABLE evaluacion (
    id_evaluacion   SERIAL PRIMARY KEY,
    id_puesto       INTEGER      NOT NULL REFERENCES puesto(id_puesto),
    id_consultor    INTEGER      NOT NULL REFERENCES consultor(id_consultor),
    fecha_creacion  TIMESTAMP    NOT NULL,
    bloque_tamanio  INTEGER      NOT NULL CHECK (bloque_tamanio > 0)
);

CREATE TABLE cuestionario (
    id_cuestionario     SERIAL PRIMARY KEY,
    id_evaluacion       INTEGER      NOT NULL REFERENCES evaluacion(id_evaluacion),
    id_candidato        INTEGER      NOT NULL REFERENCES candidato(id_candidato),
    clave               VARCHAR(8)   NOT NULL,
    estado              VARCHAR(20)  NOT NULL
                        CHECK (estado IN ('ACTIVO','EN PROCESO','COMPLETO','INCOMPLETO','SIN CONTESTAR')),
    fecha_inicio        TIMESTAMP,
    fecha_fin           TIMESTAMP,
    fecha_cambio_estado TIMESTAMP    NOT NULL,
    fecha_ultimo_acceso TIMESTAMP,
    cant_accesos        INTEGER      NOT NULL DEFAULT 0,
    accesos_maximos     INTEGER      NOT NULL,
    tiempo_max          INTEGER      NOT NULL,   -- minutos
    UNIQUE (id_evaluacion, id_candidato)
);

CREATE TABLE pregunta_cuestionario (
    id_pregunta_cuest  SERIAL PRIMARY KEY,
    id_cuestionario    INTEGER NOT NULL REFERENCES cuestionario(id_cuestionario),
    id_pregunta        INTEGER NOT NULL REFERENCES pregunta(id_pregunta),
    nro_orden          INTEGER NOT NULL,
    nro_bloque         INTEGER NOT NULL,
    UNIQUE (id_cuestionario, nro_orden)
);

-- PreguntaCuestionario 1 -- 0..1 RespuestaCandidata
CREATE TABLE respuesta_candidata (
    id_respuesta       SERIAL PRIMARY KEY,
    id_pregunta_cuest  INTEGER      NOT NULL UNIQUE REFERENCES pregunta_cuestionario(id_pregunta_cuest),
    id_item            INTEGER      NOT NULL REFERENCES item_opcion_de_respuesta(id_item),
    ponderacion        NUMERIC(4,2) NOT NULL
);

CREATE TABLE resultado_competencia (
    id_resultado_comp  SERIAL PRIMARY KEY,
    id_cuestionario    INTEGER      NOT NULL REFERENCES cuestionario(id_cuestionario),
    id_competencia     INTEGER      NOT NULL REFERENCES competencia(id_competencia),
    puntaje_obtenido   NUMERIC(5,2) NOT NULL,
    UNIQUE (id_cuestionario, id_competencia)
);

CREATE TABLE resultado_factor (
    id_resultado_fact  SERIAL PRIMARY KEY,
    id_resultado_comp  INTEGER      NOT NULL REFERENCES resultado_competencia(id_resultado_comp),
    id_factor          INTEGER      NOT NULL REFERENCES factor(id_factor),
    puntaje_obtenido   NUMERIC(5,2) NOT NULL
);

CREATE TABLE registro_auditoria (
    id_auditoria    SERIAL PRIMARY KEY,
    usuario         VARCHAR(50)  NOT NULL,
    entidad         VARCHAR(50)  NOT NULL,
    accion          VARCHAR(200) NOT NULL,
    fecha_hora      TIMESTAMP    NOT NULL
);

CREATE TABLE registro_ejecucion_batch (
    id_batch        SERIAL PRIMARY KEY,
    fecha_hora      TIMESTAMP    NOT NULL,
    accion          VARCHAR(200) NOT NULL
);

-- Puntaje total del cuestionario: atributo derivado en el DER (no se almacena)
CREATE VIEW v_puntaje_cuestionario AS
SELECT c.id_cuestionario, ROUND(AVG(rc.puntaje_obtenido), 2) AS puntaje_total
FROM cuestionario c JOIN resultado_competencia rc ON rc.id_cuestionario = c.id_cuestionario
GROUP BY c.id_cuestionario;
