package capitalhumano.dto;

public class CaracteristicaPuestoDTO {
    private String competencia;
    private double ponderacionMinima;
    private boolean evaluable;

    public CaracteristicaPuestoDTO(String competencia, double ponderacionMinima, boolean evaluable) {
        this.competencia = competencia;
        this.ponderacionMinima = ponderacionMinima;
        this.evaluable = evaluable;
    }

    public String getCompetencia() { return competencia; }
    public double getPonderacionMinima() { return ponderacionMinima; }
    public boolean isEvaluable() { return evaluable; }
}
