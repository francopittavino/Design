package capitalhumano.dao.impl;

import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.CaracteristicaPuesto;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Empresa;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.Genero;
import capitalhumano.entidades.PreguntaCuestionario;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.RespuestaCandidata;
import capitalhumano.entidades.ResultadoCompetencia;
import capitalhumano.entidades.ResultadoFactor;
import capitalhumano.entidades.TipoDocumento;
import capitalhumano.entidades.estados.EstadoCuestionarioState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Lectura de filas a entidades, compartida por las implementaciones de los DAO. */
final class Mapeos {
    private Mapeos() {}

    static final String COLS_CANDIDATO = "c.id_candidato, c.nro_candidato, c.apellido, c.nombre, c.tipo_doc, c.nro_doc, "
            + "c.fecha_nacimiento, c.genero, c.nacionalidad, c.email, c.escolaridad, c.activo";

    static Candidato candidato(ResultSet rs) throws SQLException {
        Candidato c = new Candidato();
        c.setId(rs.getInt("id_candidato"));
        c.setNroCandidato(rs.getString("nro_candidato"));
        c.setApellido(rs.getString("apellido"));
        c.setNombre(rs.getString("nombre"));
        c.setTipoDoc(TipoDocumento.valueOf(rs.getString("tipo_doc")));
        c.setNroDoc(rs.getString("nro_doc"));
        if (rs.getDate("fecha_nacimiento") != null) c.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        if (rs.getString("genero") != null) c.setGenero(Genero.valueOf(rs.getString("genero")));
        c.setNacionalidad(rs.getString("nacionalidad"));
        c.setEmail(rs.getString("email"));
        c.setEscolaridad(rs.getString("escolaridad"));
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }

    /** Puesto con empresa y características enlazadas a las competencias del catálogo. */
    static Puesto puesto(Connection cn, Catalogo cat, int idPuesto) throws SQLException {
        Puesto p = null;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT p.id_puesto, p.codigo, p.nombre, p.descripcion, p.activo, p.fecha_baja, e.id_empresa, e.nombre AS empresa "
                        + "FROM puesto p JOIN empresa e ON e.id_empresa = p.id_empresa WHERE p.id_puesto = ?")) {
            ps.setInt(1, idPuesto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Puesto();
                    p.setId(rs.getInt("id_puesto"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setActivo(rs.getBoolean("activo"));
                    if (rs.getDate("fecha_baja") != null) p.setFechaBaja(rs.getDate("fecha_baja").toLocalDate());
                    p.setEmpresa(new Empresa(rs.getInt("id_empresa"), rs.getString("empresa")));
                }
            }
        }
        if (p != null) p.setCaracteristicas(caracteristicas(cn, cat, idPuesto));
        return p;
    }

    static List<CaracteristicaPuesto> caracteristicas(Connection cn, Catalogo cat, int idPuesto) throws SQLException {
        List<CaracteristicaPuesto> lista = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT cp.id_caracteristica, cp.id_competencia, cp.ponderacion_minima FROM caracteristica_puesto cp "
                        + "JOIN competencia c ON c.id_competencia = cp.id_competencia "
                        + "WHERE cp.id_puesto = ? ORDER BY c.nombre")) {
            ps.setInt(1, idPuesto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CaracteristicaPuesto cp = new CaracteristicaPuesto();
                    cp.setId(rs.getInt(1));
                    cp.setCompetencia(cat.competencias.get(rs.getInt(2)));
                    cp.setPonderacionMinima(rs.getDouble(3));
                    lista.add(cp);
                }
            }
        }
        return lista;
    }

    /** Evaluación (sin cuestionarios) con su puesto y consultor. */
    static Evaluacion evaluacion(ResultSet rs, Puesto puesto) throws SQLException {
        Evaluacion e = new Evaluacion();
        e.setId(rs.getInt("id_evaluacion"));
        e.setFechaCreacion(ConexionBD.ldt(rs.getTimestamp("fecha_creacion")));
        e.setBloqueTamanio(rs.getInt("bloque_tamanio"));
        e.setPuesto(puesto);
        Consultor co = new Consultor();
        co.setId(rs.getInt("id_consultor"));
        e.setConsultor(co);
        return e;
    }

    static final String COLS_CUESTIONARIO = "q.id_cuestionario, q.id_evaluacion, q.clave, q.estado, q.fecha_inicio, q.fecha_fin, "
            + "q.fecha_cambio_estado, q.fecha_ultimo_acceso, q.cant_accesos, q.accesos_maximos, q.tiempo_max";

    static Cuestionario cuestionario(ResultSet rs) throws SQLException {
        Cuestionario q = new Cuestionario();
        q.setId(rs.getInt("id_cuestionario"));
        q.setClave(rs.getString("clave"));
        q.setEstadoActual(EstadoCuestionarioState.desdeNombre(rs.getString("estado")));
        q.setFechaInicio(ConexionBD.ldt(rs.getTimestamp("fecha_inicio")));
        q.setFechaFin(ConexionBD.ldt(rs.getTimestamp("fecha_fin")));
        q.setFechaCambioEstado(ConexionBD.ldt(rs.getTimestamp("fecha_cambio_estado")));
        q.setFechaUltimoAcceso(ConexionBD.ldt(rs.getTimestamp("fecha_ultimo_acceso")));
        q.setCantAccesos(rs.getInt("cant_accesos"));
        q.setAccesosMaximos(rs.getInt("accesos_maximos"));
        q.setTiempoMax(rs.getInt("tiempo_max"));
        return q;
    }

    /** Carga los resultados (competencia y factores) de los cuestionarios indicados. */
    static void resultados(Connection cn, Catalogo cat, Map<Integer, Cuestionario> cuestionarios) throws SQLException {
        if (cuestionarios.isEmpty()) return;
        String ids = idsSql(cuestionarios.keySet());
        Map<Integer, ResultadoCompetencia> porId = new LinkedHashMap<>();
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_resultado_comp, id_cuestionario, id_competencia, puntaje_obtenido FROM resultado_competencia "
                        + "WHERE id_cuestionario IN (" + ids + ") ORDER BY id_resultado_comp");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ResultadoCompetencia rc = new ResultadoCompetencia(cat.competencias.get(rs.getInt(3)));
                rc.setId(rs.getInt(1));
                rc.setPuntajeObtenido(rs.getDouble(4));
                porId.put(rc.getId(), rc);
                cuestionarios.get(rs.getInt(2)).agregarResultado(rc);
            }
        }
        if (porId.isEmpty()) return;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_resultado_fact, id_resultado_comp, id_factor, puntaje_obtenido FROM resultado_factor "
                        + "WHERE id_resultado_comp IN (" + idsSql(porId.keySet()) + ")");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ResultadoFactor rf = new ResultadoFactor(cat.factores.get(rs.getInt(3)), rs.getDouble(4));
                rf.setId(rs.getInt(1));
                porId.get(rs.getInt(2)).agregarResultadoFactor(rf);
            }
        }
    }

    /** Carga las preguntas del cuestionario con su respuesta (si la tiene). */
    static void preguntas(Connection cn, Catalogo cat, Cuestionario q) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT pc.id_pregunta_cuest, pc.id_pregunta, pc.nro_orden, pc.nro_bloque, "
                        + "r.id_respuesta, r.id_item, r.ponderacion "
                        + "FROM pregunta_cuestionario pc LEFT JOIN respuesta_candidata r ON r.id_pregunta_cuest = pc.id_pregunta_cuest "
                        + "WHERE pc.id_cuestionario = ? ORDER BY pc.nro_orden")) {
            ps.setInt(1, q.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PreguntaCuestionario pc = new PreguntaCuestionario(cat.preguntas.get(rs.getInt(2)), rs.getInt(3), rs.getInt(4));
                    pc.setId(rs.getInt(1));
                    int idResp = rs.getInt(5);
                    if (!rs.wasNull()) {
                        RespuestaCandidata r = new RespuestaCandidata(cat.items.get(rs.getInt(6)), rs.getDouble(7));
                        r.setId(idResp);
                        pc.setRespuesta(r);
                    }
                    q.agregarPregunta(pc);
                }
            }
        }
    }

    static String idsSql(Iterable<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : ids) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(id.intValue());
        }
        return sb.toString();
    }

    /** "contiene" sin distinguir mayúsculas: '%texto%' escapando comodines. */
    static String contiene(String texto) {
        String t = texto == null ? "" : texto.trim();
        return "%" + t.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
    }
}
