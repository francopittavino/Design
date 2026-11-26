package capitalhumano.dao.impl;

import capitalhumano.dao.CandidatoDAO;
import capitalhumano.dao.DatosException;
import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.Cuestionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidatoDAOImpl implements CandidatoDAO {

    @Override
    public Candidato buscarPorId(int id) {
        return buscarUno("c.id_candidato = ?", ps -> ps.setInt(1, id));
    }

    @Override
    public Candidato buscarPorNroCandidato(String nro) {
        return buscarUno("c.nro_candidato = ?", ps -> ps.setString(1, nro));
    }

    @Override
    public Candidato buscarPorDocumento(String tipo, String nro) {
        return buscarUno("c.tipo_doc = ? AND c.nro_doc = ?", ps -> {
            ps.setString(1, tipo);
            ps.setString(2, nro);
        });
    }

    @Override
    public List<Candidato> buscarPorCriterios(String apellido, String nombre, String nro) {
        String sql = "SELECT " + Mapeos.COLS_CANDIDATO + " FROM candidato c WHERE c.activo "
                + "AND c.apellido ILIKE ? AND c.nombre ILIKE ? AND c.nro_candidato ILIKE ? "
                + "ORDER BY c.apellido, c.nombre";
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, Mapeos.contiene(apellido));
            ps.setString(2, Mapeos.contiene(nombre));
            ps.setString(3, Mapeos.contiene(nro));
            List<Candidato> lista = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(Mapeos.candidato(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DatosException("Error al buscar candidatos", e);
        }
    }

    private interface Parametros { void set(PreparedStatement ps) throws SQLException; }

    private Candidato buscarUno(String where, Parametros params) {
        String sql = "SELECT " + Mapeos.COLS_CANDIDATO + " FROM candidato c WHERE " + where;
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            params.set(ps);
            Candidato c;
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                c = Mapeos.candidato(rs);
            }
            cargarCuestionarios(cn, c);
            return c;
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el candidato", e);
        }
    }

    /** Cuestionarios del candidato con su estado y clave (sin preguntas). */
    private void cargarCuestionarios(Connection cn, Candidato c) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT " + Mapeos.COLS_CUESTIONARIO + " FROM cuestionario q WHERE q.id_candidato = ? ORDER BY q.id_cuestionario")) {
            ps.setInt(1, c.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cuestionario q = Mapeos.cuestionario(rs);
                    q.setCandidato(c);
                    c.getCuestionarios().add(q);
                    if (q.estaActivo() || q.estaEnProceso()) c.setClaveAcceso(q.getClave());
                }
            }
        }
    }
}
