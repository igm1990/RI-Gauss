package uo.ri.conf;

import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.implementation.MechanicGatewayImplementation;

public class PersistenceFactory {

	public static MechanicGateway getMechanicGateway() {
		return new MechanicGatewayImplementation();
	}
	
}
