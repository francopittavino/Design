package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.RegistroEjecucionBatchDAO;
import capitalhumano.entidades.RegistroEjecucionBatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroEjecucionBatchDAOImpl implements RegistroEjecucionBatchDAO {

    @Override
    public void guardar(RegistroEjecucionBatch r) {
        try (Connection cn = ConexionBD.obtener(); PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO registro_ejecucion_batch (fecha_hora, accion) VALUES (?, ?)")) {
            ps.setTimestamp(1, ConexionBD.ts(r.getFechaHora()));
            ps.setString(2, r.getAccion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al registrar la ejecución del proceso batch", e);
        }
    }
}
