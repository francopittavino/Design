package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.EvaluacionDAO;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.Puesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EvaluacionDAOImpl implements EvaluacionDAO {

    @Override
    public void guardar(Evaluacion eval) {
        try (Connection cn = ConexionBD.obtener()) {
            cn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO evaluacion (id_puesto, id_consultor, fecha_creacion, bloque_tamanio) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, eval.getPuesto().getId());
                    ps.setInt(2, eval.getConsultor().getId());
                    ps.setTimestamp(3, ConexionBD.ts(eval.getFechaCreacion()));
                    ps.setInt(4, eval.getBloqueTamanio());
                    ps.executeUpdate();
                    eval.setId(CuestionarioDAOImpl.clave(ps));
                }
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO cuestionario (id_evaluacion, id_candidato, clave, estado, fecha_cambio_estado, "
                                + "cant_accesos, accesos_maximos, tiempo_max) VALUES (?, ?, ?, ?, ?, 0, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    for (Cuestionario q : eval.getCuestionarios()) {
                        ps.setInt(1, eval.getId());
                        ps.setInt(2, q.getCandidato().getId());
                        ps.setString(3, q.getClave());
                        ps.setString(4, q.getNombreEstado());
                        ps.setTimestamp(5, ConexionBD.ts(q.getFechaCambioEstado()));
                        ps.setInt(6, q.getAccesosMaximos());
                        ps.setInt(7, q.getTiempoMax());
                        ps.executeUpdate();
                        q.setId(CuestionarioDAOImpl.clave(ps));
                    }
                }
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al guardar la evaluación", e);
        }
    }

    @Override
    public List<Evaluacion> buscarPorPuesto(int idPuesto) {
        try (Connection cn = ConexionBD.obtener()) {
            Catalogo cat = Catalogo.cargar(cn);
            Puesto puesto = Mapeos.puesto(cn, cat, idPuesto);
            Map<Integer, Evaluacion> evaluaciones = new LinkedHashMap<>();
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT id_evaluacion, id_puesto, id_consultor, fecha_creacion, bloque_tamanio FROM evaluacion "
                            + "WHERE id_puesto = ? ORDER BY fecha_creacion")) {
                ps.setInt(1, idPuesto);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Evaluacion e = Mapeos.evaluacion(rs, puesto);
                        evaluaciones.put(e.getId(), e);
                    }
                }
            }
            if (evaluaciones.isEmpty()) return new ArrayList<>();

            Map<Integer, Cuestionario> cuestionarios = new LinkedHashMap<>();
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT " + Mapeos.COLS_CUESTIONARIO + ", " + Mapeos.COLS_CANDIDATO
                            + " FROM cuestionario q JOIN candidato c ON c.id_candidato = q.id_candidato "
                            + "WHERE q.id_evaluacion IN (" + Mapeos.idsSql(evaluaciones.keySet()) + ") "
                            + "ORDER BY c.apellido, c.nombre");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cuestionario q = Mapeos.cuestionario(rs);
                    q.setCandidato(Mapeos.candidato(rs));
                    Evaluacion e = evaluaciones.get(rs.getInt("id_evaluacion"));
                    q.setEvaluacion(e);
                    e.getCuestionarios().add(q);
                    cuestionarios.put(q.getId(), q);
                }
            }
            Mapeos.resultados(cn, cat, cuestionarios);
            return new ArrayList<>(evaluaciones.values());
        } catch (SQLException e) {
            throw new DatosException("Error al buscar las evaluaciones del puesto", e);
        }
    }
}
