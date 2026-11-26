package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.RegistroAuditoriaDAO;
import capitalhumano.entidades.RegistroAuditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroAuditoriaDAOImpl implements RegistroAuditoriaDAO {

    @Override
    public void guardar(RegistroAuditoria r) {
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO registro_auditoria (usuario, entidad, accion, fecha_hora) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, r.getUsuario());
            ps.setString(2, r.getEntidad());
            ps.setString(3, r.getAccion());
            ps.setTimestamp(4, ConexionBD.ts(r.getFechaHora()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al registrar la auditoría", e);
        }
    }
}
