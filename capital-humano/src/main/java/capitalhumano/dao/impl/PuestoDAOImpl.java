package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.PuestoDAO;
import capitalhumano.entidades.CaracteristicaPuesto;
import capitalhumano.entidades.Empresa;
import capitalhumano.entidades.Puesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PuestoDAOImpl implements PuestoDAO {

    @Override
    public Puesto buscarPorId(int id) {
        try (Connection cn = ConexionBD.obtener()) {
            return Mapeos.puesto(cn, Catalogo.cargar(cn), id);
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el puesto", e);
        }
    }

    /** Sin las características: alcanza para las grillas de búsqueda. */
    @Override
    public List<Puesto> buscarPorFiltros(Map<String, String> filtros) {
        String sql = "SELECT p.id_puesto, p.codigo, p.nombre, p.descripcion, e.id_empresa, e.nombre AS empresa "
                + "FROM puesto p JOIN empresa e ON e.id_empresa = p.id_empresa "
                + "WHERE p.activo AND p.codigo ILIKE ? AND p.nombre ILIKE ? AND e.nombre ILIKE ? "
                + "ORDER BY p.nombre, e.nombre";
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, Mapeos.contiene(filtros.get("codigo")));
            ps.setString(2, Mapeos.contiene(filtros.get("nombre")));
            ps.setString(3, Mapeos.contiene(filtros.get("empresa")));
            List<Puesto> lista = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Puesto p = new Puesto();
                    p.setId(rs.getInt("id_puesto"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setEmpresa(new Empresa(rs.getInt("id_empresa"), rs.getString("empresa")));
                    lista.add(p);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DatosException("Error al buscar puestos", e);
        }
    }

    @Override
    public List<CaracteristicaPuesto> buscarCaracteristicas(int idPuesto) {
        try (Connection cn = ConexionBD.obtener()) {
            return Mapeos.caracteristicas(cn, Catalogo.cargar(cn), idPuesto);
        } catch (SQLException e) {
            throw new DatosException("Error al buscar las características del puesto", e);
        }
    }

    @Override
    public boolean existePuestoConCompetencia(int idCompetencia) {
        return contar("SELECT COUNT(*) FROM caracteristica_puesto cp JOIN puesto p ON p.id_puesto = cp.id_puesto "
                + "WHERE p.activo AND cp.id_competencia = ?", idCompetencia) > 0;
    }

    @Override
    public int contarCandidatos(int idPuesto) {
        return contar("SELECT COUNT(DISTINCT q.id_candidato) FROM cuestionario q "
                + "JOIN evaluacion e ON e.id_evaluacion = q.id_evaluacion WHERE e.id_puesto = ?", idPuesto);
    }

    @Override
    public int contarEvaluacionesCompletas(int idPuesto) {
        return contar("SELECT COUNT(*) FROM cuestionario q JOIN evaluacion e ON e.id_evaluacion = q.id_evaluacion "
                + "WHERE e.id_puesto = ? AND q.estado = 'COMPLETO'", idPuesto);
    }

    private int contar(String sql, int id) {
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatosException("Error al contar", e);
        }
    }
}
