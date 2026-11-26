package capitalhumano.entidades;

public class PreguntaCuestionario {
    private int id;
    private int nroOrden;
    private int nroBloque;
    private Pregunta pregunta;
    private RespuestaCandidata respuesta;   // 0..1

    public PreguntaCuestionario() {}

    public PreguntaCuestionario(Pregunta pregunta, int nroOrden, int nroBloque) {
        this.pregunta = pregunta;
        this.nroOrden = nroOrden;
        this.nroBloque = nroBloque;
    }

    public void setRespuesta(RespuestaCandidata r) { this.respuesta = r; }
    public RespuestaCandidata getRespuesta() { return respuesta; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNroOrden() { return nroOrden; }
    public void setNroOrden(int nroOrden) { this.nroOrden = nroOrden; }
    public int getNroBloque() { return nroBloque; }
    public void setNroBloque(int nroBloque) { this.nroBloque = nroBloque; }
    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }
}
