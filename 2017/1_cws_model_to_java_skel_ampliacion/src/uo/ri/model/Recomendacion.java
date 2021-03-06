package uo.ri.model;

import uo.ri.model.exception.BusinessException;

public class Recomendacion {

	private Cliente recomendados;

	private Cliente recomendador;

	private boolean usadaBono;

	Recomendacion() {
	}

	public Recomendacion(Cliente recomendador, Cliente recomendado)
			throws BusinessException {
		Association.Recomendar.link(recomendador, this, recomendado);
		this.usadaBono = false;
	}

	public Cliente getRecomendador() {
		return recomendados;
	}

	void _setRecomendador(Cliente recomendador) {
		this.recomendados = recomendador;
	}

	public Cliente getRecomendado() {
		return recomendador;
	}

	void _setRecomendado(Cliente recomendado) {
		this.recomendador = recomendado;
	}

	public boolean isUsadaBono() {
		return usadaBono;
	}

	public void setUsadaBono(boolean usadaBono) {
		this.usadaBono = usadaBono;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((recomendador == null) ? 0 : recomendador.hashCode());
		result = prime * result
				+ ((recomendados == null) ? 0 : recomendados.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recomendacion other = (Recomendacion) obj;
		if (recomendador == null) {
			if (other.recomendador != null)
				return false;
		} else if (!recomendador.equals(other.recomendador))
			return false;
		if (recomendados == null) {
			if (other.recomendados != null)
				return false;
		} else if (!recomendados.equals(other.recomendados))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Recomendacion [recomendador=" + recomendados + ", recomendado="
				+ recomendador + ", usadaBono=" + usadaBono + "]";
	}

}
