-- Datos de prueba generados por sql/generar_datos_prueba.py (no editar a mano)
BEGIN;

INSERT INTO empresa (id_empresa, nombre) VALUES
  (1, 'LOOPCORTEX'),
  (2, 'VSOFTWARE'),
  (3, 'AGRO LITORAL S.A.'),
  (4, 'CAPIT@L HUMANO CONSULTORES');
INSERT INTO consultor (id_consultor, id_empresa, usuario, nombre, apellido) VALUES
  (1, 4, 'jperez', 'JUAN', 'PEREZ'),
  (2, 4, 'mgomez', 'MARIA', 'GOMEZ');

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

INSERT INTO pregunta (id_pregunta, id_factor, id_opcion, nombre, enunciado, descripcion) VALUES
  (1, 1, 1, 'FIN A COMIENZO', '¿QUÉ SIGNIFICA LA DEPENDENCIA FIN A COMIENZO EN UN CRONOGRAMA? UNA ACTIVIDAD COMIENZA CUANDO OTRA TERMINA', NULL),
  (2, 1, 1, 'CAMINO CRÍTICO', 'EL CAMINO CRÍTICO ES LA SECUENCIA DE ACTIVIDADES CON MAYOR HOLGURA DEL PROYECTO', NULL),
  (3, 1, 2, 'ACTUALIZACIÓN DEL CRONOGRAMA', 'ACTUALIZO EL CRONOGRAMA CUANDO CAMBIA EL ALCANCE DEL PROYECTO', NULL),
  (4, 2, 1, 'VALOR GANADO', '¿LA METODOLOGÍA DE VALOR GANADO PERMITE PREDECIR COSTOS FUTUROS DE UN PROYECTO?', NULL),
  (5, 2, 1, 'RESERVA DE CONTINGENCIA', 'LA RESERVA DE CONTINGENCIA SE USA PARA CUBRIR RIESGOS IDENTIFICADOS', NULL),
  (6, 2, 2, 'CONTROL DE DESVÍOS', 'REVISO LOS DESVÍOS DE COSTO ANTES DE QUE AFECTEN AL PRESUPUESTO TOTAL', NULL),
  (7, 3, 2, 'MATRIZ DE RIESGOS', 'UTILIZO UNA MATRIZ DE PROBABILIDAD E IMPACTO PARA PRIORIZAR RIESGOS', NULL),
  (8, 4, 3, 'DECISIONES BAJO PRESIÓN', 'TOMO DECISIONES A TIEMPO AUNQUE NO TENGA TODA LA INFORMACIÓN', NULL),
  (9, 4, 2, 'CONSULTA AL EQUIPO', 'CONSULTO AL EQUIPO ANTES DE TOMAR DECISIONES QUE LO AFECTAN', NULL),
  (10, 4, 3, 'POSTERGAR DECISIONES', 'PREFIERO POSTERGAR LAS DECISIONES DIFÍCILES HASTA QUE SE RESUELVAN SOLAS', NULL),
  (11, 5, 2, 'RECONOCIMIENTO', 'RECONOZCO PÚBLICAMENTE LOS LOGROS DE LOS INTEGRANTES DEL EQUIPO', NULL),
  (12, 5, 2, 'OBJETIVOS CLAROS', 'ME ASEGURO DE QUE CADA PERSONA CONOZCA LOS OBJETIVOS DE SU TRABAJO', NULL),
  (13, 5, 3, 'DELEGACIÓN', 'DELEGAR TAREAS IMPORTANTES ES UNA PÉRDIDA DE CONTROL', NULL),
  (14, 6, 2, 'AYUDA A COMPAÑEROS', 'OFREZCO AYUDA A MIS COMPAÑEROS CUANDO TERMINO MIS TAREAS', NULL),
  (15, 6, 2, 'COMPARTIR INFORMACIÓN', 'COMPARTO LA INFORMACIÓN QUE PUEDE SERVIRLE AL RESTO DEL EQUIPO', NULL),
  (16, 6, 3, 'TRABAJO INDIVIDUAL', 'RINDO MEJOR CUANDO NO DEPENDO DE NADIE DEL EQUIPO', NULL),
  (17, 7, 2, 'ESCUCHA ACTIVA', 'ANTE UN CONFLICTO ESCUCHO TODAS LAS POSTURAS ANTES DE OPINAR', NULL),
  (18, 7, 4, 'CONFLICTOS ABIERTOS', 'LOS CONFLICTOS DEL EQUIPO DEBEN TRATARSE ABIERTAMENTE', NULL),
  (19, 7, 2, 'EVITAR CONFLICTOS', 'EVITO PARTICIPAR CUANDO HAY DISCUSIONES EN EL EQUIPO', NULL),
  (20, 8, 3, 'PRESENTACIONES', 'ME RESULTA SENCILLO PRESENTAR IDEAS FRENTE A UN GRUPO', NULL),
  (21, 8, 2, 'ADAPTAR EL MENSAJE', 'ADAPTO EL VOCABULARIO SEGÚN QUIÉN SEA MI INTERLOCUTOR', NULL),
  (22, 8, 2, 'INTERRUMPIR', 'INTERRUMPO A LOS DEMÁS CUANDO YA ENTENDÍ LO QUE QUIEREN DECIR', NULL),
  (23, 9, 2, 'CORREOS CLAROS', 'REVISO LOS CORREOS ANTES DE ENVIARLOS PARA QUE SEAN CLAROS Y BREVES', NULL),
  (24, 9, 2, 'DOCUMENTAR ACUERDOS', 'DEJO POR ESCRITO LOS ACUERDOS DE LAS REUNIONES', NULL),
  (25, 9, 3, 'INFORMES', 'LOS INFORMES EXTENSOS SON MÁS CLAROS QUE LOS RESUMIDOS', NULL),
  (26, 10, 1, 'POLIMORFISMO', 'EL POLIMORFISMO PERMITE TRATAR OBJETOS DE DISTINTAS CLASES A TRAVÉS DE UNA INTERFAZ COMÚN', NULL),
  (27, 10, 1, 'HERENCIA MÚLTIPLE', 'EN JAVA UNA CLASE PUEDE EXTENDER DE DOS CLASES A LA VEZ', NULL),
  (28, 10, 4, 'ENCAPSULAMIENTO', '¿LOS ATRIBUTOS DE UNA ENTIDAD DEBERÍAN SER PRIVADOS Y ACCEDERSE POR MÉTODOS?', NULL),
  (29, 11, 1, 'HASHMAP ORDEN', 'UN HASHMAP GARANTIZA EL ORDEN DE INSERCIÓN DE LAS CLAVES', NULL),
  (30, 11, 1, 'STREAMS PEREZOSOS', 'LAS OPERACIONES INTERMEDIAS DE UN STREAM SE EJECUTAN RECIÉN CON LA OPERACIÓN TERMINAL', NULL),
  (31, 11, 4, 'LIST INMUTABLE', '¿LIST.OF() DEVUELVE UNA LISTA QUE NO SE PUEDE MODIFICAR?', NULL),
  (32, 12, 2, 'PREPARAR ARGUMENTOS', 'PREPARO MIS ARGUMENTOS ANTES DE UNA NEGOCIACIÓN', NULL);
INSERT INTO ponderacion_item (id_pregunta, id_item, valor) VALUES
  (1, 1, 10),
  (1, 2, 0),
  (2, 1, 0),
  (2, 2, 10),
  (3, 3, 5),
  (3, 4, 3),
  (3, 5, 2),
  (3, 6, 0),
  (4, 1, 10),
  (4, 2, 0),
  (5, 1, 10),
  (5, 2, 0),
  (6, 3, 5),
  (6, 4, 3),
  (6, 5, 2),
  (6, 6, 0),
  (7, 3, 5),
  (7, 4, 3),
  (7, 5, 2),
  (7, 6, 0),
  (8, 7, 4),
  (8, 8, 3),
  (8, 9, 2),
  (8, 10, 1),
  (8, 11, 0),
  (9, 3, 5),
  (9, 4, 3),
  (9, 5, 2),
  (9, 6, 0),
  (10, 7, 0),
  (10, 8, 1),
  (10, 9, 2),
  (10, 10, 3),
  (10, 11, 4),
  (11, 3, 5),
  (11, 4, 3),
  (11, 5, 2),
  (11, 6, 0),
  (12, 3, 5),
  (12, 4, 3),
  (12, 5, 2),
  (12, 6, 0),
  (13, 7, 0),
  (13, 8, 1),
  (13, 9, 2),
  (13, 10, 3),
  (13, 11, 4),
  (14, 3, 5),
  (14, 4, 3),
  (14, 5, 2),
  (14, 6, 0),
  (15, 3, 5),
  (15, 4, 3),
  (15, 5, 2),
  (15, 6, 0),
  (16, 7, 0),
  (16, 8, 1),
  (16, 9, 2),
  (16, 10, 3),
  (16, 11, 4),
  (17, 3, 5),
  (17, 4, 3),
  (17, 5, 2),
  (17, 6, 0),
  (18, 12, 10),
  (18, 13, 0),
  (19, 3, 0),
  (19, 4, 2),
  (19, 5, 3),
  (19, 6, 5),
  (20, 7, 4),
  (20, 8, 3),
  (20, 9, 2),
  (20, 10, 1),
  (20, 11, 0),
  (21, 3, 5),
  (21, 4, 3),
  (21, 5, 2),
  (21, 6, 0),
  (22, 3, 0),
  (22, 4, 2),
  (22, 5, 3),
  (22, 6, 5),
  (23, 3, 5),
  (23, 4, 3),
  (23, 5, 2),
  (23, 6, 0),
  (24, 3, 5),
  (24, 4, 3),
  (24, 5, 2),
  (24, 6, 0),
  (25, 7, 0),
  (25, 8, 1),
  (25, 9, 2),
  (25, 10, 3),
  (25, 11, 4),
  (26, 1, 10),
  (26, 2, 0),
  (27, 1, 0),
  (27, 2, 10),
  (28, 12, 10),
  (28, 13, 0),
  (29, 1, 0),
  (29, 2, 10),
  (30, 1, 10),
  (30, 2, 0),
  (31, 12, 10),
  (31, 13, 0),
  (32, 3, 5),
  (32, 4, 3),
  (32, 5, 2),
  (32, 6, 0);

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

INSERT INTO evaluacion (id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio) VALUES (1, 1, 1, '2026-04-01 09:00', 4);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (1, 1, 1, 'K7M2P9QA', 'COMPLETO', '2026-04-02 10:00', '2026-04-02 10:41', '2026-04-02 10:41', '2026-04-02 10:00', 2, 5, 60);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (1, 1, 1, 8.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (1, 1, 8.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (1, 2, 8.5);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (2, 1, 2, 8.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (2, 4, 8.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (2, 5, 8.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (3, 1, 3, 9.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (3, 6, 9.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (3, 7, 9.0);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (2, 1, 2, 'R4T8W2ZB', 'COMPLETO', '2026-04-03 15:10', '2026-04-03 15:52', '2026-04-03 15:52', '2026-04-03 15:10', 1, 5, 60);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (4, 2, 1, 7.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (4, 1, 7.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (4, 2, 7.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (5, 2, 2, 6.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (5, 4, 6.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (5, 5, 6.5);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (6, 2, 3, 8.1);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (6, 6, 8.1);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (6, 7, 8.1);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (3, 1, 3, 'H3N6Y8CD', 'COMPLETO', '2026-04-04 09:30', '2026-04-04 10:20', '2026-04-04 10:20', '2026-04-04 09:30', 3, 5, 60);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (7, 3, 1, 4.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (7, 1, 4.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (7, 2, 4.5);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (8, 3, 2, 6.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (8, 4, 6.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (8, 5, 6.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (9, 3, 3, 4.8);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (9, 6, 4.8);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (9, 7, 4.8);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (4, 1, 4, 'P2Q5S7EF', 'INCOMPLETO', '2026-04-02 18:00', NULL, '2026-04-02 19:05', '2026-04-02 18:00', 1, 5, 60);
INSERT INTO evaluacion (id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio) VALUES (2, 1, 1, '2026-03-10 11:00', 4);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (5, 2, 5, 'M8V3X6GH', 'COMPLETO', '2026-03-11 08:00', '2026-03-11 08:35', '2026-03-11 08:35', '2026-03-11 08:00', 1, 5, 60);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (10, 5, 1, 6.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (10, 1, 6.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (10, 2, 6.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (11, 5, 2, 5.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (11, 4, 5.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (11, 5, 5.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (12, 5, 3, 6.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (12, 6, 6.5);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (12, 7, 6.5);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (6, 2, 6, 'B5D9F2JK', 'SIN CONTESTAR', NULL, NULL, '2026-03-17 11:00', NULL, 0, 5, 60);
INSERT INTO evaluacion (id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio) VALUES (3, 2, 1, '2026-05-04 10:00', 4);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (7, 3, 7, 'T6U2V4LM', 'COMPLETO', '2026-05-05 09:00', '2026-05-05 09:45', '2026-05-05 09:45', '2026-05-05 09:00', 1, 5, 60);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (13, 7, 1, 7.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (13, 1, 7.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (13, 2, 7.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (14, 7, 3, 6.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (14, 6, 6.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (14, 7, 6.0);
INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido) VALUES (15, 7, 4, 8.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (15, 8, 8.0);
INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (15, 9, 8.0);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (8, 3, 8, 'Z9Y8X7NP', 'EN PROCESO', '2026-05-05 10:00', NULL, '2026-05-05 10:00', '2026-05-05 10:00', 1, 5, 60);
INSERT INTO evaluacion (id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio) VALUES (4, 3, 1, NOW(), 4);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (9, 4, 9, 'AB12CD34', 'ACTIVO', NULL, NULL, NOW(), NULL, 0, 5, 60);
INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) VALUES (10, 4, 10, 'QW7ER5TY', 'ACTIVO', NULL, NULL, NOW(), NULL, 0, 5, 60);

SELECT setval(pg_get_serial_sequence('empresa', 'id_empresa'), (SELECT COALESCE(MAX(id_empresa), 1) FROM empresa));
SELECT setval(pg_get_serial_sequence('consultor', 'id_consultor'), (SELECT COALESCE(MAX(id_consultor), 1) FROM consultor));
SELECT setval(pg_get_serial_sequence('opcion_de_respuesta', 'id_opcion'), (SELECT COALESCE(MAX(id_opcion), 1) FROM opcion_de_respuesta));
SELECT setval(pg_get_serial_sequence('item_opcion_de_respuesta', 'id_item'), (SELECT COALESCE(MAX(id_item), 1) FROM item_opcion_de_respuesta));
SELECT setval(pg_get_serial_sequence('competencia', 'id_competencia'), (SELECT COALESCE(MAX(id_competencia), 1) FROM competencia));
SELECT setval(pg_get_serial_sequence('factor', 'id_factor'), (SELECT COALESCE(MAX(id_factor), 1) FROM factor));
SELECT setval(pg_get_serial_sequence('pregunta', 'id_pregunta'), (SELECT COALESCE(MAX(id_pregunta), 1) FROM pregunta));
SELECT setval(pg_get_serial_sequence('ponderacion_item', 'id_ponderacion'), (SELECT COALESCE(MAX(id_ponderacion), 1) FROM ponderacion_item));
SELECT setval(pg_get_serial_sequence('puesto', 'id_puesto'), (SELECT COALESCE(MAX(id_puesto), 1) FROM puesto));
SELECT setval(pg_get_serial_sequence('caracteristica_puesto', 'id_caracteristica'), (SELECT COALESCE(MAX(id_caracteristica), 1) FROM caracteristica_puesto));
SELECT setval(pg_get_serial_sequence('candidato', 'id_candidato'), (SELECT COALESCE(MAX(id_candidato), 1) FROM candidato));
SELECT setval(pg_get_serial_sequence('evaluacion', 'id_evaluacion'), (SELECT COALESCE(MAX(id_evaluacion), 1) FROM evaluacion));
SELECT setval(pg_get_serial_sequence('cuestionario', 'id_cuestionario'), (SELECT COALESCE(MAX(id_cuestionario), 1) FROM cuestionario));
SELECT setval(pg_get_serial_sequence('resultado_competencia', 'id_resultado_comp'), (SELECT COALESCE(MAX(id_resultado_comp), 1) FROM resultado_competencia));
SELECT setval(pg_get_serial_sequence('resultado_factor', 'id_resultado_fact'), (SELECT COALESCE(MAX(id_resultado_fact), 1) FROM resultado_factor));

COMMIT;
