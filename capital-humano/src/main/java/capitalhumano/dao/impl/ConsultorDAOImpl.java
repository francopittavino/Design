package capitalhumano.dao.impl;

import capitalhumano.dao.ConsultorDAO;
import capitalhumano.dao.DatosException;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultorDAOImpl implements ConsultorDAO {

    @Override
    public Consultor buscarPorUsuario(String usuario) {
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(
                "SELECT c.id_consultor, c.usuario, c.nombre, c.apellido, c.activo, e.id_empresa, e.nombre AS empresa "
                        + "FROM consultor c JOIN empresa e ON e.id_empresa = c.id_empresa WHERE LOWER(c.usuario) = LOWER(?)")) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Consultor c = new Consultor();
                c.setId(rs.getInt("id_consultor"));
                c.setUsuario(rs.getString("usuario"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setActivo(rs.getBoolean("activo"));
                c.setEmpresa(new Empresa(rs.getInt("id_empresa"), rs.getString("empresa")));
                return c;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el consultor", e);
        }
    }
}
