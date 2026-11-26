package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.PreguntaDAO;
import capitalhumano.entidades.PonderacionItem;
import capitalhumano.entidades.Pregunta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PreguntaDAOImpl implements PreguntaDAO {

    @Override
    public Pregunta buscarPorId(int id) {
        return catalogo().preguntas.get(id);
    }

    @Override
    public List<Pregunta> buscarPorFactor(int idFactor) {
        return catalogo().preguntas.values().stream()
                .filter(p -> p.isActivo() && p.getFactor().getId() == idFactor).toList();
    }

    @Override
    public List<Pregunta> buscarPorCriterios(int idCompetencia, int idFactor, String nombre) {
        // Para validar duplicados (CU021 2.C) se traen todas las preguntas activas de esa competencia y factor;
        // la comparación de nombre y enunciado la hace el gestor.
        return catalogo().preguntas.values().stream()
                .filter(p -> p.isActivo() && p.getFactor().getId() == idFactor
                        && p.getFactor().getCompetencia().getId() == idCompetencia)
                .toList();
    }

    @Override
    public List<PonderacionItem> buscarPonderaciones(int idPregunta) {
        Pregunta p = buscarPorId(idPregunta);
        return p == null ? List.of() : p.getPonderaciones();
    }

    @Override
    public boolean existePreguntaConOpcion(int idOpcion) {
        return catalogo().preguntas.values().stream()
                .anyMatch(p -> p.isActivo() && p.getOpcionDeRespuesta().getId() == idOpcion);
    }

    @Override
    public void guardar(Pregunta pregunta) {
        try (Connection cn = ConexionBD.obtener()) {
            cn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO pregunta (id_factor, id_opcion, nombre, enunciado, descripcion, activo) "
                                + "VALUES (?, ?, ?, ?, ?, TRUE)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, pregunta.getFactor().getId());
                    ps.setInt(2, pregunta.getOpcionDeRespuesta().getId());
                    ps.setString(3, pregunta.getNombre());
                    ps.setString(4, pregunta.getEnunciado());
                    ps.setString(5, pregunta.getDescripcion());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        pregunta.setId(rs.getInt(1));
                    }
                }
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO ponderacion_item (id_pregunta, id_item, valor) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    for (PonderacionItem pi : pregunta.getPonderaciones()) {
                        ps.setInt(1, pregunta.getId());
                        ps.setInt(2, pi.getItemOpcionDeRespuesta().getId());
                        ps.setDouble(3, pi.getValor());
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            rs.next();
                            pi.setId(rs.getInt(1));
                        }
                    }
                }
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al guardar la pregunta", e);
        }
    }

    private Catalogo catalogo() {
        try (Connection cn = ConexionBD.obtener()) {
            return Catalogo.cargar(cn);
        } catch (SQLException e) {
            throw new DatosException("Error al leer las preguntas", e);
        }
    }
}
