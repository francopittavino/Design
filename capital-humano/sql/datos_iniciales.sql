-- Datos iniciales del sistema: lo que la aplicación no puede cargar por sí misma
-- (los ABM de empresas, consultores, opciones de respuesta, competencias, factores,
-- puestos y candidatos están fuera del alcance de las Etapas 7 y 8).
-- No incluye preguntas, evaluaciones, cuestionarios ni resultados: se generan usando el sistema.
BEGIN;

INSERT INTO empresa (id_empresa, nombre) VALUES
  (1, 'LOOPCORTEX'),
  (2, 'VSOFTWARE'),
  (3, 'AGRO LITORAL S.A.'),
  (4, 'CAPITAL HUMANO CONSULTORES');
INSERT INTO consultor (id_consultor, id_empresa, usuario, nombre, apellido) VALUES
  (1, 4, 'jperez', 'JUAN', 'PEREZ');

INSERT INTO opcion_de_respuesta (id_opcion, nombre, descripcion) VALUES
  (1, 'VERDADERO / FALSO', NULL),
  (2, 'FRECUENCIA', NULL),
  (3, 'GRADO DE ACUERDO', NULL),
  (4, 'SI / NO', NULL);
INSERT INTO item_opcion_de_respuesta (id_item, id_opcion, orden, descripcion) VALUES
  (1, 1, 1, 'VERDADERO'),
  (2, 1, 2, 'FALSO'),
  (3, 2, 1, 'SIEMPRE'),
  (4, 2, 2, 'CASI SIEMPRE'),
  (5, 2, 3, 'A VECES'),
  (6, 2, 4, 'NUNCA'),
  (7, 3, 1, 'TOTALMENTE DE ACUERDO'),
  (8, 3, 2, 'DE ACUERDO'),
  (9, 3, 3, 'NI DE ACUERDO NI EN DESACUERDO'),
  (10, 3, 4, 'EN DESACUERDO'),
  (11, 3, 5, 'TOTALMENTE EN DESACUERDO'),
  (12, 4, 1, 'SI'),
  (13, 4, 2, 'NO');

INSERT INTO competencia (id_competencia, codigo, nombre, descripcion, tipo) VALUES
  (1, 'GP', 'GESTIÓN DE PROYECTOS', NULL, 'TECNICA'),
  (2, 'LID', 'LIDERAZGO', NULL, 'ACTITUDINAL'),
  (3, 'TEQ', 'TRABAJO EN EQUIPO', NULL, 'ACTITUDINAL'),
  (4, 'COM', 'COMUNICACIÓN', NULL, 'ACTITUDINAL'),
  (5, 'JAVA', 'PROGRAMACIÓN JAVA', NULL, 'TECNICA'),
  (6, 'NEG', 'NEGOCIACIÓN', NULL, 'ACTITUDINAL');
INSERT INTO factor (id_factor, id_competencia, codigo, nombre, nro_orden) VALUES
  (1, 1, 'GP-T', 'GESTIÓN DEL TIEMPO', 1),
  (2, 1, 'GP-C', 'GESTIÓN DE COSTOS', 2),
  (3, 1, 'GP-R', 'GESTIÓN DE RIESGOS', 3),
  (4, 2, 'LID-D', 'TOMA DE DECISIONES', 1),
  (5, 2, 'LID-M', 'MOTIVACIÓN DEL EQUIPO', 2),
  (6, 3, 'TEQ-C', 'COLABORACIÓN', 1),
  (7, 3, 'TEQ-F', 'MANEJO DE CONFLICTOS', 2),
  (8, 4, 'COM-O', 'COMUNICACIÓN ORAL', 1),
  (9, 4, 'COM-E', 'COMUNICACIÓN ESCRITA', 2),
  (10, 5, 'JAVA-OO', 'ORIENTACIÓN A OBJETOS', 1),
  (11, 5, 'JAVA-C', 'COLECCIONES Y STREAMS', 2),
  (12, 6, 'NEG-A', 'ARGUMENTACIÓN', 1);


INSERT INTO puesto (id_puesto, id_empresa, codigo, nombre, descripcion) VALUES
  (1, 1, '082', 'ENCARGADO', 'ENCARGADO DE PROYECTOS DE SOFTWARE'),
  (2, 2, '710', 'ENCARGADO', 'ENCARGADO DE ÁREA DE SOPORTE'),
  (3, 2, 'DEV-SR', 'DESARROLLADOR JAVA SR', 'DESARROLLADOR BACKEND JAVA SENIOR'),
  (4, 3, 'JC-01', 'JEFE DE COMPRAS', 'RESPONSABLE DE COMPRAS E INSUMOS'),
  (5, 3, 'AN-FUN', 'ANALISTA FUNCIONAL', 'ANALISTA FUNCIONAL DE SISTEMAS DE GESTIÓN');
INSERT INTO caracteristica_puesto (id_puesto, id_competencia, ponderacion_minima) VALUES
  (1, 1, 6.0),
  (1, 2, 5.0),
  (1, 3, 6.0),
  (2, 1, 5.0),
  (2, 3, 5.0),
  (2, 4, 5.0),
  (3, 5, 6.5),
  (3, 3, 5.0),
  (3, 4, 4.0),
  (4, 4, 5.0),
  (4, 6, 6.0),
  (5, 1, 5.0),
  (5, 4, 5.0);

INSERT INTO candidato (id_candidato, nro_candidato, apellido, nombre, tipo_doc, nro_doc, fecha_nacimiento, genero, nacionalidad, email, escolaridad) VALUES
  (1, '00123', 'GARCIA', 'LUIS', 'DNI', '28345678', '1981-03-14', 'H', 'ARGENTINA', 'lgarcia@mail.com', 'UNIVERSITARIO'),
  (2, '00124', 'LOPEZ', 'PEDRO', 'DNI', '31234567', '1985-07-02', 'H', 'ARGENTINA', 'plopez@mail.com', 'UNIVERSITARIO'),
  (3, '00125', 'MARTINEZ', 'ANA', 'DNI', '26345678', '1978-11-21', 'M', 'ARGENTINA', 'amartinez@mail.com', 'TERCIARIO'),
  (4, '00126', 'TORRES', 'ROSA', 'DNI', '39876543', '1996-01-30', 'M', 'ARGENTINA', 'rtorres@mail.com', 'SECUNDARIO'),
  (5, '00127', 'GARCIA', 'PEDRO', 'DNI', '30111222', '1983-05-09', 'H', 'ARGENTINA', 'pgarcia@mail.com', 'UNIVERSITARIO'),
  (6, '00128', 'FERNANDEZ', 'LUCIA', 'DNI', '35222333', '1990-09-17', 'M', 'ARGENTINA', 'lfernandez@mail.com', 'UNIVERSITARIO'),
  (7, '00129', 'SOSA', 'MARTIN', 'DNI', '33444555', '1988-12-01', 'H', 'ARGENTINA', 'msosa@mail.com', 'TERCIARIO'),
  (8, '00130', 'ROMERO', 'JULIETA', 'DNI', '40555666', '1997-04-25', 'M', 'ARGENTINA', 'jromero@mail.com', 'UNIVERSITARIO'),
  (9, '00131', 'BENITEZ', 'CARLOS', 'LE', '7654321', '1960-02-11', 'H', 'ARGENTINA', 'cbenitez@mail.com', 'SECUNDARIO'),
  (10, '00132', 'ACOSTA', 'VALENTINA', 'DNI', '41888999', '1998-08-08', 'M', 'ARGENTINA', 'vacosta@mail.com', 'UNIVERSITARIO'),
  (11, '00133', 'MEDINA', 'DIEGO', 'DNI', '29999000', '1982-06-19', 'H', 'ARGENTINA', 'dmedina@mail.com', 'UNIVERSITARIO'),
  (12, '00134', 'HERRERA', 'SOFIA', 'PP', 'AAB123456', '1993-10-05', 'M', 'ARGENTINA', 'sherrera@mail.com', 'POSGRADO'),
  (13, '00135', 'SUAREZ', 'TOMAS', 'DNI', '38123123', '1994-03-03', 'H', 'ARGENTINA', 'tsuarez@mail.com', 'UNIVERSITARIO'),
  (14, '00136', 'GIMENEZ', 'CAROLINA', 'DNI', '36456456', '1991-12-12', 'M', 'ARGENTINA', 'cgimenez@mail.com', 'TERCIARIO');

SELECT setval(pg_get_serial_sequence('empresa', 'id_empresa'), (SELECT MAX(id_empresa) FROM empresa));
SELECT setval(pg_get_serial_sequence('consultor', 'id_consultor'), (SELECT MAX(id_consultor) FROM consultor));
SELECT setval(pg_get_serial_sequence('opcion_de_respuesta', 'id_opcion'), (SELECT MAX(id_opcion) FROM opcion_de_respuesta));
SELECT setval(pg_get_serial_sequence('item_opcion_de_respuesta', 'id_item'), (SELECT MAX(id_item) FROM item_opcion_de_respuesta));
SELECT setval(pg_get_serial_sequence('competencia', 'id_competencia'), (SELECT MAX(id_competencia) FROM competencia));
SELECT setval(pg_get_serial_sequence('factor', 'id_factor'), (SELECT MAX(id_factor) FROM factor));
SELECT setval(pg_get_serial_sequence('puesto', 'id_puesto'), (SELECT MAX(id_puesto) FROM puesto));
SELECT setval(pg_get_serial_sequence('caracteristica_puesto', 'id_caracteristica'), (SELECT MAX(id_caracteristica) FROM caracteristica_puesto));
SELECT setval(pg_get_serial_sequence('candidato', 'id_candidato'), (SELECT MAX(id_candidato) FROM candidato));

COMMIT;
