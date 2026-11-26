"""Genera sql/datos_prueba.sql (datos de prueba para los CU020, CU021, CU025, CU026 y CU028).

Uso:  python sql/generar_datos_prueba.py
"""
from pathlib import Path

SALIDA = Path(__file__).with_name("datos_prueba.sql")

# ---------------------------------------------------------------- opciones de respuesta
OPCIONES = {
    1: ("VERDADERO / FALSO", ["VERDADERO", "FALSO"]),
    2: ("FRECUENCIA", ["SIEMPRE", "CASI SIEMPRE", "A VECES", "NUNCA"]),
    3: ("GRADO DE ACUERDO", ["TOTALMENTE DE ACUERDO", "DE ACUERDO", "NI DE ACUERDO NI EN DESACUERDO",
                             "EN DESACUERDO", "TOTALMENTE EN DESACUERDO"]),
    4: ("SI / NO", ["SI", "NO"]),
}
# ponderaciones (suman 10) según la "polaridad" de la pregunta
PONDERACIONES = {
    (1, "+"): [10, 0], (1, "-"): [0, 10],
    (2, "+"): [5, 3, 2, 0], (2, "-"): [0, 2, 3, 5],
    (3, "+"): [4, 3, 2, 1, 0], (3, "-"): [0, 1, 2, 3, 4],
    (4, "+"): [10, 0], (4, "-"): [0, 10],
}

COMPETENCIAS = {
    1: ("GP", "GESTIÓN DE PROYECTOS", "TECNICA"),
    2: ("LID", "LIDERAZGO", "ACTITUDINAL"),
    3: ("TEQ", "TRABAJO EN EQUIPO", "ACTITUDINAL"),
    4: ("COM", "COMUNICACIÓN", "ACTITUDINAL"),
    5: ("JAVA", "PROGRAMACIÓN JAVA", "TECNICA"),
    6: ("NEG", "NEGOCIACIÓN", "ACTITUDINAL"),
}

# id factor: (competencia, código, nombre, orden, [ (nombre, enunciado, opcion, polaridad) ])
FACTORES = {
    1: (1, "GP-T", "GESTIÓN DEL TIEMPO", 1, [
        ("FIN A COMIENZO", "¿QUÉ SIGNIFICA LA DEPENDENCIA FIN A COMIENZO EN UN CRONOGRAMA? UNA ACTIVIDAD COMIENZA CUANDO OTRA TERMINA", 1, "+"),
        ("CAMINO CRÍTICO", "EL CAMINO CRÍTICO ES LA SECUENCIA DE ACTIVIDADES CON MAYOR HOLGURA DEL PROYECTO", 1, "-"),
        ("ACTUALIZACIÓN DEL CRONOGRAMA", "ACTUALIZO EL CRONOGRAMA CUANDO CAMBIA EL ALCANCE DEL PROYECTO", 2, "+"),
    ]),
    2: (1, "GP-C", "GESTIÓN DE COSTOS", 2, [
        ("VALOR GANADO", "¿LA METODOLOGÍA DE VALOR GANADO PERMITE PREDECIR COSTOS FUTUROS DE UN PROYECTO?", 1, "+"),
        ("RESERVA DE CONTINGENCIA", "LA RESERVA DE CONTINGENCIA SE USA PARA CUBRIR RIESGOS IDENTIFICADOS", 1, "+"),
        ("CONTROL DE DESVÍOS", "REVISO LOS DESVÍOS DE COSTO ANTES DE QUE AFECTEN AL PRESUPUESTO TOTAL", 2, "+"),
    ]),
    3: (1, "GP-R", "GESTIÓN DE RIESGOS", 3, [
        ("MATRIZ DE RIESGOS", "UTILIZO UNA MATRIZ DE PROBABILIDAD E IMPACTO PARA PRIORIZAR RIESGOS", 2, "+"),
    ]),
    4: (2, "LID-D", "TOMA DE DECISIONES", 1, [
        ("DECISIONES BAJO PRESIÓN", "TOMO DECISIONES A TIEMPO AUNQUE NO TENGA TODA LA INFORMACIÓN", 3, "+"),
        ("CONSULTA AL EQUIPO", "CONSULTO AL EQUIPO ANTES DE TOMAR DECISIONES QUE LO AFECTAN", 2, "+"),
        ("POSTERGAR DECISIONES", "PREFIERO POSTERGAR LAS DECISIONES DIFÍCILES HASTA QUE SE RESUELVAN SOLAS", 3, "-"),
    ]),
    5: (2, "LID-M", "MOTIVACIÓN DEL EQUIPO", 2, [
        ("RECONOCIMIENTO", "RECONOZCO PÚBLICAMENTE LOS LOGROS DE LOS INTEGRANTES DEL EQUIPO", 2, "+"),
        ("OBJETIVOS CLAROS", "ME ASEGURO DE QUE CADA PERSONA CONOZCA LOS OBJETIVOS DE SU TRABAJO", 2, "+"),
        ("DELEGACIÓN", "DELEGAR TAREAS IMPORTANTES ES UNA PÉRDIDA DE CONTROL", 3, "-"),
    ]),
    6: (3, "TEQ-C", "COLABORACIÓN", 1, [
        ("AYUDA A COMPAÑEROS", "OFREZCO AYUDA A MIS COMPAÑEROS CUANDO TERMINO MIS TAREAS", 2, "+"),
        ("COMPARTIR INFORMACIÓN", "COMPARTO LA INFORMACIÓN QUE PUEDE SERVIRLE AL RESTO DEL EQUIPO", 2, "+"),
        ("TRABAJO INDIVIDUAL", "RINDO MEJOR CUANDO NO DEPENDO DE NADIE DEL EQUIPO", 3, "-"),
    ]),
    7: (3, "TEQ-F", "MANEJO DE CONFLICTOS", 2, [
        ("ESCUCHA ACTIVA", "ANTE UN CONFLICTO ESCUCHO TODAS LAS POSTURAS ANTES DE OPINAR", 2, "+"),
        ("CONFLICTOS ABIERTOS", "LOS CONFLICTOS DEL EQUIPO DEBEN TRATARSE ABIERTAMENTE", 4, "+"),
        ("EVITAR CONFLICTOS", "EVITO PARTICIPAR CUANDO HAY DISCUSIONES EN EL EQUIPO", 2, "-"),
    ]),
    8: (4, "COM-O", "COMUNICACIÓN ORAL", 1, [
        ("PRESENTACIONES", "ME RESULTA SENCILLO PRESENTAR IDEAS FRENTE A UN GRUPO", 3, "+"),
        ("ADAPTAR EL MENSAJE", "ADAPTO EL VOCABULARIO SEGÚN QUIÉN SEA MI INTERLOCUTOR", 2, "+"),
        ("INTERRUMPIR", "INTERRUMPO A LOS DEMÁS CUANDO YA ENTENDÍ LO QUE QUIEREN DECIR", 2, "-"),
    ]),
    9: (4, "COM-E", "COMUNICACIÓN ESCRITA", 2, [
        ("CORREOS CLAROS", "REVISO LOS CORREOS ANTES DE ENVIARLOS PARA QUE SEAN CLAROS Y BREVES", 2, "+"),
        ("DOCUMENTAR ACUERDOS", "DEJO POR ESCRITO LOS ACUERDOS DE LAS REUNIONES", 2, "+"),
        ("INFORMES", "LOS INFORMES EXTENSOS SON MÁS CLAROS QUE LOS RESUMIDOS", 3, "-"),
    ]),
    10: (5, "JAVA-OO", "ORIENTACIÓN A OBJETOS", 1, [
        ("POLIMORFISMO", "EL POLIMORFISMO PERMITE TRATAR OBJETOS DE DISTINTAS CLASES A TRAVÉS DE UNA INTERFAZ COMÚN", 1, "+"),
        ("HERENCIA MÚLTIPLE", "EN JAVA UNA CLASE PUEDE EXTENDER DE DOS CLASES A LA VEZ", 1, "-"),
        ("ENCAPSULAMIENTO", "¿LOS ATRIBUTOS DE UNA ENTIDAD DEBERÍAN SER PRIVADOS Y ACCEDERSE POR MÉTODOS?", 4, "+"),
    ]),
    11: (5, "JAVA-C", "COLECCIONES Y STREAMS", 2, [
        ("HASHMAP ORDEN", "UN HASHMAP GARANTIZA EL ORDEN DE INSERCIÓN DE LAS CLAVES", 1, "-"),
        ("STREAMS PEREZOSOS", "LAS OPERACIONES INTERMEDIAS DE UN STREAM SE EJECUTAN RECIÉN CON LA OPERACIÓN TERMINAL", 1, "+"),
        ("LIST INMUTABLE", "¿LIST.OF() DEVUELVE UNA LISTA QUE NO SE PUEDE MODIFICAR?", 4, "+"),
    ]),
    12: (6, "NEG-A", "ARGUMENTACIÓN", 1, [
        ("PREPARAR ARGUMENTOS", "PREPARO MIS ARGUMENTOS ANTES DE UNA NEGOCIACIÓN", 2, "+"),
    ]),
}

EMPRESAS = {1: "LOOPCORTEX", 2: "VSOFTWARE", 3: "AGRO LITORAL S.A.", 4: "CAPIT@L HUMANO CONSULTORES"}

PUESTOS = {  # id: (empresa, código, nombre, descripción, {competencia: ponderación mínima})
    1: (1, "082", "ENCARGADO", "ENCARGADO DE PROYECTOS DE SOFTWARE", {1: 6.0, 2: 5.0, 3: 6.0}),
    2: (2, "710", "ENCARGADO", "ENCARGADO DE ÁREA DE SOPORTE", {1: 5.0, 3: 5.0, 4: 5.0}),
    3: (2, "DEV-SR", "DESARROLLADOR JAVA SR", "DESARROLLADOR BACKEND JAVA SENIOR", {5: 6.5, 3: 5.0, 4: 4.0}),
    4: (3, "JC-01", "JEFE DE COMPRAS", "RESPONSABLE DE COMPRAS E INSUMOS", {4: 5.0, 6: 6.0}),
    5: (3, "AN-FUN", "ANALISTA FUNCIONAL", "ANALISTA FUNCIONAL DE SISTEMAS DE GESTIÓN", {1: 5.0, 4: 5.0}),
}

CANDIDATOS = {  # id: (nro, apellido, nombre, tipo, doc, nacimiento, género, email, escolaridad)
    1: ("00123", "GARCIA", "LUIS", "DNI", "28345678", "1981-03-14", "H", "lgarcia@mail.com", "UNIVERSITARIO"),
    2: ("00124", "LOPEZ", "PEDRO", "DNI", "31234567", "1985-07-02", "H", "plopez@mail.com", "UNIVERSITARIO"),
    3: ("00125", "MARTINEZ", "ANA", "DNI", "26345678", "1978-11-21", "M", "amartinez@mail.com", "TERCIARIO"),
    4: ("00126", "TORRES", "ROSA", "DNI", "39876543", "1996-01-30", "M", "rtorres@mail.com", "SECUNDARIO"),
    5: ("00127", "GARCIA", "PEDRO", "DNI", "30111222", "1983-05-09", "H", "pgarcia@mail.com", "UNIVERSITARIO"),
    6: ("00128", "FERNANDEZ", "LUCIA", "DNI", "35222333", "1990-09-17", "M", "lfernandez@mail.com", "UNIVERSITARIO"),
    7: ("00129", "SOSA", "MARTIN", "DNI", "33444555", "1988-12-01", "H", "msosa@mail.com", "TERCIARIO"),
    8: ("00130", "ROMERO", "JULIETA", "DNI", "40555666", "1997-04-25", "M", "jromero@mail.com", "UNIVERSITARIO"),
    9: ("00131", "BENITEZ", "CARLOS", "LE", "7654321", "1960-02-11", "H", "cbenitez@mail.com", "SECUNDARIO"),
    10: ("00132", "ACOSTA", "VALENTINA", "DNI", "41888999", "1998-08-08", "M", "vacosta@mail.com", "UNIVERSITARIO"),
    11: ("00133", "MEDINA", "DIEGO", "DNI", "29999000", "1982-06-19", "H", "dmedina@mail.com", "UNIVERSITARIO"),
    12: ("00134", "HERRERA", "SOFIA", "PP", "AAB123456", "1993-10-05", "M", "sherrera@mail.com", "POSGRADO"),
    13: ("00135", "SUAREZ", "TOMAS", "DNI", "38123123", "1994-03-03", "H", "tsuarez@mail.com", "UNIVERSITARIO"),
    14: ("00136", "GIMENEZ", "CAROLINA", "DNI", "36456456", "1991-12-12", "M", "cgimenez@mail.com", "TERCIARIO"),
}

# Evaluaciones: id: (puesto, fecha_creacion (SQL), [cuestionarios])
# cuestionario: (id, candidato, clave, estado, inicio, fin, cambio_estado, ult_acceso, accesos, {comp: puntaje})
EVALUACIONES = {
    1: (1, "'2026-04-01 09:00'", [
        (1, 1, "K7M2P9QA", "COMPLETO", "'2026-04-02 10:00'", "'2026-04-02 10:41'", "'2026-04-02 10:41'", "'2026-04-02 10:00'", 2, {1: 8.5, 2: 8.0, 3: 9.0}),
        (2, 2, "R4T8W2ZB", "COMPLETO", "'2026-04-03 15:10'", "'2026-04-03 15:52'", "'2026-04-03 15:52'", "'2026-04-03 15:10'", 1, {1: 7.0, 2: 6.5, 3: 8.1}),
        (3, 3, "H3N6Y8CD", "COMPLETO", "'2026-04-04 09:30'", "'2026-04-04 10:20'", "'2026-04-04 10:20'", "'2026-04-04 09:30'", 3, {1: 4.5, 2: 6.0, 3: 4.8}),
        (4, 4, "P2Q5S7EF", "INCOMPLETO", "'2026-04-02 18:00'", "NULL", "'2026-04-02 19:05'", "'2026-04-02 18:00'", 1, {}),
    ]),
    2: (1, "'2026-03-10 11:00'", [
        (5, 5, "M8V3X6GH", "COMPLETO", "'2026-03-11 08:00'", "'2026-03-11 08:35'", "'2026-03-11 08:35'", "'2026-03-11 08:00'", 1, {1: 6.0, 2: 5.0, 3: 6.5}),
        (6, 6, "B5D9F2JK", "SIN CONTESTAR", "NULL", "NULL", "'2026-03-17 11:00'", "NULL", 0, {}),
    ]),
    3: (2, "'2026-05-04 10:00'", [
        (7, 7, "T6U2V4LM", "COMPLETO", "'2026-05-05 09:00'", "'2026-05-05 09:45'", "'2026-05-05 09:45'", "'2026-05-05 09:00'", 1, {1: 7.0, 3: 6.0, 4: 8.0}),
        # En proceso desde mayo: al ingresar (CU026 1.A) pasa a INCOMPLETO por tiempo máximo
        (8, 8, "Z9Y8X7NP", "EN PROCESO", "'2026-05-05 10:00'", "NULL", "'2026-05-05 10:00'", "'2026-05-05 10:00'", 1, {}),
    ]),
    # Evaluación reciente (fecha = momento de carga de la BD): cuestionarios ACTIVOS para probar el CU026
    4: (3, "NOW()", [
        (9, 9, "AB12CD34", "ACTIVO", "NULL", "NULL", "NOW()", "NULL", 0, {}),
        (10, 10, "QW7ER5TY", "ACTIVO", "NULL", "NULL", "NOW()", "NULL", 0, {}),
    ]),
}


def q(s):
    return "NULL" if s is None else "'" + s.replace("'", "''") + "'"


def main():
    out = ["-- Datos de prueba generados por sql/generar_datos_prueba.py (no editar a mano)", "BEGIN;", ""]
    out.append("INSERT INTO empresa (id_empresa, nombre) VALUES")
    out.append(",\n".join(f"  ({i}, {q(n)})" for i, n in EMPRESAS.items()) + ";")
    out.append("INSERT INTO consultor (id_consultor, id_empresa, usuario, nombre, apellido) VALUES\n"
               "  (1, 4, 'jperez', 'JUAN', 'PEREZ'),\n  (2, 4, 'mgomez', 'MARIA', 'GOMEZ');")

    out.append("\nINSERT INTO opcion_de_respuesta (id_opcion, nombre, descripcion) VALUES")
    out.append(",\n".join(f"  ({i}, {q(n)}, NULL)" for i, (n, _) in OPCIONES.items()) + ";")
    item_id = 0
    items_por_opcion = {}
    filas = []
    for i, (_, items) in OPCIONES.items():
        items_por_opcion[i] = []
        for orden, desc in enumerate(items, 1):
            item_id += 1
            items_por_opcion[i].append(item_id)
            filas.append(f"  ({item_id}, {i}, {orden}, {q(desc)})")
    out.append("INSERT INTO item_opcion_de_respuesta (id_item, id_opcion, orden, descripcion) VALUES\n" + ",\n".join(filas) + ";")

    out.append("\nINSERT INTO competencia (id_competencia, codigo, nombre, descripcion, tipo) VALUES")
    out.append(",\n".join(f"  ({i}, {q(c)}, {q(n)}, NULL, {q(t)})" for i, (c, n, t) in COMPETENCIAS.items()) + ";")
    out.append("INSERT INTO factor (id_factor, id_competencia, codigo, nombre, nro_orden) VALUES")
    out.append(",\n".join(f"  ({i}, {c}, {q(cod)}, {q(n)}, {o})" for i, (c, cod, n, o, _) in FACTORES.items()) + ";")

    preg_id = 0
    preg_filas, pond_filas = [], []
    for fid, (_, _, _, _, preguntas) in FACTORES.items():
        for nombre, enunciado, opcion, polaridad in preguntas:
            preg_id += 1
            preg_filas.append(f"  ({preg_id}, {fid}, {opcion}, {q(nombre)}, {q(enunciado)}, NULL)")
            for item, valor in zip(items_por_opcion[opcion], PONDERACIONES[(opcion, polaridad)]):
                pond_filas.append(f"  ({preg_id}, {item}, {valor})")
    out.append("\nINSERT INTO pregunta (id_pregunta, id_factor, id_opcion, nombre, enunciado, descripcion) VALUES\n"
               + ",\n".join(preg_filas) + ";")
    out.append("INSERT INTO ponderacion_item (id_pregunta, id_item, valor) VALUES\n" + ",\n".join(pond_filas) + ";")

    out.append("\nINSERT INTO puesto (id_puesto, id_empresa, codigo, nombre, descripcion) VALUES")
    out.append(",\n".join(f"  ({i}, {e}, {q(c)}, {q(n)}, {q(d)})" for i, (e, c, n, d, _) in PUESTOS.items()) + ";")
    out.append("INSERT INTO caracteristica_puesto (id_puesto, id_competencia, ponderacion_minima) VALUES")
    out.append(",\n".join(f"  ({i}, {c}, {m})" for i, (_, _, _, _, cs) in PUESTOS.items() for c, m in cs.items()) + ";")

    out.append("\nINSERT INTO candidato (id_candidato, nro_candidato, apellido, nombre, tipo_doc, nro_doc, fecha_nacimiento, "
               "genero, nacionalidad, email, escolaridad) VALUES")
    out.append(",\n".join(f"  ({i}, {q(n)}, {q(a)}, {q(no)}, {q(t)}, {q(d)}, {q(f)}, {q(g)}, 'ARGENTINA', {q(e)}, {q(es)})"
                          for i, (n, a, no, t, d, f, g, e, es) in CANDIDATOS.items()) + ";")

    out.append("")
    rc_id = 0
    for eid, (puesto, fecha, cuestionarios) in EVALUACIONES.items():
        out.append(f"INSERT INTO evaluacion (id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio) "
                   f"VALUES ({eid}, {puesto}, 1, {fecha}, 4);")
        for (cid, cand, clave, estado, ini, fin, cambio, ult, acc, res) in cuestionarios:
            out.append(f"INSERT INTO cuestionario (id_cuestionario, id_evaluacion, id_candidato, clave, estado, fecha_inicio, "
                       f"fecha_fin, fecha_cambio_estado, fecha_ultimo_acceso, cant_accesos, accesos_maximos, tiempo_max) "
                       f"VALUES ({cid}, {eid}, {cand}, '{clave}', '{estado}', {ini}, {fin}, {cambio}, {ult}, {acc}, 5, 60);")
            for comp, puntaje in res.items():
                rc_id += 1
                out.append(f"INSERT INTO resultado_competencia (id_resultado_comp, id_cuestionario, id_competencia, "
                           f"puntaje_obtenido) VALUES ({rc_id}, {cid}, {comp}, {puntaje});")
                factores = [f for f, v in FACTORES.items() if v[0] == comp and len(v[4]) >= 2]
                for f in factores:
                    out.append(f"INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) "
                               f"VALUES ({rc_id}, {f}, {puntaje});")

    out.append("")
    for tabla, col in [("empresa", "id_empresa"), ("consultor", "id_consultor"), ("opcion_de_respuesta", "id_opcion"),
                       ("item_opcion_de_respuesta", "id_item"), ("competencia", "id_competencia"), ("factor", "id_factor"),
                       ("pregunta", "id_pregunta"), ("ponderacion_item", "id_ponderacion"), ("puesto", "id_puesto"),
                       ("caracteristica_puesto", "id_caracteristica"), ("candidato", "id_candidato"),
                       ("evaluacion", "id_evaluacion"), ("cuestionario", "id_cuestionario"),
                       ("resultado_competencia", "id_resultado_comp"), ("resultado_factor", "id_resultado_fact")]:
        out.append(f"SELECT setval(pg_get_serial_sequence('{tabla}', '{col}'), (SELECT COALESCE(MAX({col}), 1) FROM {tabla}));")
    out.append("\nCOMMIT;\n")
    SALIDA.write_text("\n".join(out), encoding="utf-8")
    print(f"{SALIDA} generado ({preg_id} preguntas)")


if __name__ == "__main__":
    main()
