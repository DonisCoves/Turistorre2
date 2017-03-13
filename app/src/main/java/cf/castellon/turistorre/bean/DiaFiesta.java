package cf.castellon.turistorre.bean;

import java.util.Map;

public class DiaFiesta {
    private String uidDiaFiesta;
    private String titulo;
    private Map<String, Evento> eventos;

    public DiaFiesta(String uidDiaFiesta, String titulo, Map<String, Evento> eventos) {
        this.uidDiaFiesta = uidDiaFiesta;
        this.titulo = titulo;
        this.eventos = eventos;
    }

    public DiaFiesta() {
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUidDiaFiesta() {
        return uidDiaFiesta;
    }

    public void setUidDiaFiesta(String uidDiaFiesta) {
        this.uidDiaFiesta = uidDiaFiesta;
    }

    public Map<String, Evento> getEventos() {
        return eventos;
    }

    public void setEventos(Map<String, Evento> eventos) {
        this.eventos = eventos;
    }

}
