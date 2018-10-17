package uo.ri.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import uo.ri.util.exception.BusinessException;

@Entity
@Table(name = "TMETALICOS")
public class Metalico extends MedioPago {

	Metalico() {
	}

	public Metalico(Cliente cliente)  {
		Association.Pagar.link(cliente, this);
	}

	@Override
	public void pagar(double importe) throws BusinessException {
		this.acumulado += importe;
	}

}