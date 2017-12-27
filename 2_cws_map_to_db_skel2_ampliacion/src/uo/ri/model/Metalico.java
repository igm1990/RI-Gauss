package uo.ri.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import uo.ri.model.exception.BusinessException;

@Entity
@Table(name = "TMETALICOS")
public class Metalico extends MedioPago {

	Metalico() {
	}

	public Metalico(Cliente cliente) throws BusinessException {
		Association.Pagar.link(this, cliente);
	}
}
