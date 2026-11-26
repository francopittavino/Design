package capitalhumano.dao.impl;

import capitalhumano.dao.CuestionarioDAO;
import capitalhumano.dao.DatosException;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.PreguntaCuestionario;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.RespuestaCandidata;
import capitalhumano.entidades.ResultadoCompetencia;
import capitalhumano.entidades.ResultadoFactor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;

public class CuestionarioDAOImpl implements CuestionarioDAO {

    @Override
    public Cuestionario buscarPorCandidato(int idCandidato) {
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(
                "SELECT id_cuestionario FROM cuestionario WHERE id_candidato = ? "
                        + "AND estado IN ('ACTIVO','EN PROCESO') ORDER BY id_cuestionario DESC LIMIT 1")) {
            ps.setInt(1, idCandidato);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? cargar(cn, rs.getInt(1)) : null;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el cuestionario del candidato", e);
        }
    }

    @Override
    public Cuestionario buscarPorId(int id) {
        try (Connection cn = ConexionBD.obtener()) {
            return cargar(cn, id);
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el cuestionario", e);
        }
    }

    private Cuestionario cargar(Connection cn, int id) throws SQLException {
        Catalogo cat = Catalogo.cargar(cn);
        Cuestionario q;
        int idEvaluacion;
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT " + Mapeos.COLS_CUESTIONARIO + ", " + Mapeos.COLS_CANDIDATO
                        + " FROM cuestionario q JOIN candidato c ON c.id_candidato = q.id_candidato WHERE q.id_cuestionario = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                q = Mapeos.cuestionario(rs);
                q.setCandidato(Mapeos.candidato(rs));
                idEvaluacion = rs.getInt("id_evaluacion");
            }
        }
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio FROM evaluacion WHERE id_evaluacion = ?")) {
            ps.setInt(1, idEvaluacion);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int idPuesto = rs.getInt("id_puesto");
                Puesto puesto = Mapeos.puesto(cn, cat, idPuesto);
                Evaluacion e = Mapeos.evaluacion(rs, puesto);
                q.setEvaluacion(e);
            }
        }
        Mapeos.preguntas(cn, cat, q);
        Mapeos.resultados(cn, cat, Map.of(q.getId(), q));
        return q;
    }

    @Override
    public void guardar(Cuestionario q) {
        try (Connection cn = ConexionBD.obtener()) {
            cn.setAutoCommit(false);
            try {
                actualizarFila(cn, q);
                insertarPreguntasYRespuestas(cn, q);
                insertarResultados(cn, q);
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al guardar el cuestionario", e);
        }
    }

    private void actualizarFila(Connection cn, Cuestionario q) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE cuestionario SET estado = ?, fecha_inicio = ?, fecha_fin = ?, fecha_cambio_estado = ?, "
                        + "fecha_ultimo_acceso = ?, cant_accesos = ? WHERE id_cuestionario = ?")) {
            ps.setString(1, q.getNombreEstado());
            ps.setTimestamp(2, ConexionBD.ts(q.getFechaInicio()));
            ps.setTimestamp(3, ConexionBD.ts(q.getFechaFin()));
            ps.setTimestamp(4, ConexionBD.ts(q.getFechaCambioEstado()));
            ps.setTimestamp(5, ConexionBD.ts(q.getFechaUltimoAcceso()));
            ps.setInt(6, q.getCantAccesos());
            ps.setInt(7, q.getId());
            ps.executeUpdate();
        }
    }

    private void insertarPreguntasYRespuestas(Connection cn, Cuestionario q) throws SQLException {
        try (PreparedStatement insPc = cn.prepareStatement(
                "INSERT INTO pregunta_cuestionario (id_cuestionario, id_pregunta, nro_orden, nro_bloque) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insResp = cn.prepareStatement(
                     "INSERT INTO respuesta_candidata (id_pregunta_cuest, id_item, ponderacion) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            for (PreguntaCuestionario pc : q.getPreguntas()) {
                if (pc.getId() == 0) {
                    insPc.setInt(1, q.getId());
                    insPc.setInt(2, pc.getPregunta().getId());
                    insPc.setInt(3, pc.getNroOrden());
                    insPc.setInt(4, pc.getNroBloque());
                    insPc.executeUpdate();
                    pc.setId(clave(insPc));
                }
                RespuestaCandidata r = pc.getRespuesta();
                if (r != null && r.getId() == 0) {
                    insResp.setInt(1, pc.getId());
                    insResp.setInt(2, r.getItemOpcionDeRespuesta().getId());
                    insResp.setDouble(3, r.getPonderacion());
                    insResp.executeUpdate();
                    r.setId(clave(insResp));
                }
            }
        }
    }

    private void insertarResultados(Connection cn, Cuestionario q) throws SQLException {
        try (PreparedStatement insRc = cn.prepareStatement(
                "INSERT INTO resultado_competencia (id_cuestionario, id_competencia, puntaje_obtenido) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insRf = cn.prepareStatement(
                     "INSERT INTO resultado_factor (id_resultado_comp, id_factor, puntaje_obtenido) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            for (ResultadoCompetencia rc : q.getResultados()) {
                if (rc.getId() != 0) continue;
                insRc.setInt(1, q.getId());
                insRc.setInt(2, rc.getCompetencia().getId());
                insRc.setDouble(3, rc.getPuntajeObtenido());
                insRc.executeUpdate();
                rc.setId(clave(insRc));
                for (ResultadoFactor rf : rc.getResultadosFactor()) {
                    insRf.setInt(1, rc.getId());
                    insRf.setInt(2, rf.getFactor().getId());
                    insRf.setDouble(3, rf.getPuntajeObtenido());
                    insRf.executeUpdate();
                    rf.setId(clave(insRf));
                }
            }
        }
    }

    @Override
    public void actualizarEstado(int id, String nombreEstado) {
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(
                "UPDATE cuestionario SET estado = ?, fecha_cambio_estado = ? WHERE id_cuestionario = ?")) {
            ps.setString(1, nombreEstado);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al actualizar el estado del cuestionario", e);
        }
    }

    static int clave(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
