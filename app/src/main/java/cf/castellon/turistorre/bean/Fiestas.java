package cf.castellon.turistorre.bean;

import java.util.Map;

public class Fiestas {
	private String uidFiestas;
	private String titulo;
	private String descripcion;
	private Map <String, DiaFiestaMeta> diasFiestas;
	public Fiestas(String uidFiestas, String titulo, String descripcion, Map<String, DiaFiestaMeta> diasFiestas) {
		this.uidFiestas = uidFiestas;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.diasFiestas = diasFiestas;
	}
	
	
	public Fiestas() {}


	public String getUidFiestas() {
		return uidFiestas;
	}
	public void setUidFiestas(String uidFiestas) {
		this.uidFiestas = uidFiestas;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Map<String, DiaFiestaMeta> getDiasFiestas() {
		return diasFiestas;
	}
	public void setDiasFiestas(Map<String, DiaFiestaMeta> diasFiestas) {
		this.diasFiestas = diasFiestas;
	}
	
	

	}
