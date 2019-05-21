public class CrudMethods {

    private int id;
    private String nome_motorista;
    private String placa;
    private String destino;

    public CrudMethods(int id, String nome_motorista, String placa, String destino) {
        this.id = id;
        this.nome_motorista = nome_motorista;
        this.placa = placa;
        this.destino = destino;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_motorista() {
        return nome_motorista;
    }

    public void setNome_motorista(String nome_motorista) {
        this.nome_motorista = nome_motorista;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
