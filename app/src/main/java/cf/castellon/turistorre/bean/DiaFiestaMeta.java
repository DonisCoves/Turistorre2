package cf.castellon.turistorre.bean;

public class DiaFiestaMeta {
    private String uidDiaFiesta;
    private String tituloDiaFiesta;


    public DiaFiestaMeta(String uidDiaFiesta, String tituloDiaFiesta) {
        this.uidDiaFiesta = uidDiaFiesta;
        this.tituloDiaFiesta = tituloDiaFiesta;
    }

    public String getTituloDiaFiesta() {
        return tituloDiaFiesta;
    }

    public void setTituloDiaFiesta(String tituloDiaFiesta) {
        this.tituloDiaFiesta = tituloDiaFiesta;
    }


    public DiaFiestaMeta() {
    }

    public String getUidDiaFiesta() {
        return uidDiaFiesta;
    }

    public void setUidDiaFiesta(String uidDiaFiesta) {
        this.uidDiaFiesta = uidDiaFiesta;
    }
}
