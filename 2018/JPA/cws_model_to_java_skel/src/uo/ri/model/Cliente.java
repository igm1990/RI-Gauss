package uo.ri.model;

import java.util.HashSet;
import java.util.Set;

import uo.ri.model.types.Address;

public class Cliente {

	private String dni;
	private String nombre;
	private String apellidos;
	private String email;
	private String phone;
	private Address address;

	private Set<Vehiculo> vehiculos = new HashSet<>();
	private Set<MedioPago> mediosPago = new HashSet<>();

	public Cliente(String dni) {
		this.dni = dni;
	}

	public Cliente(String dni, String name, String surname) {
		this(dni);
		this.nombre = name;
		this.apellidos = surname;
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public Address getAddress() {
		return address;
	}

	public String getDni() {
		return dni;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
		Cliente other = (Cliente) obj;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente [dni=" + dni + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", address=" + address + "]";
	}

	Set<Vehiculo> _getVehiculos() {
		return vehiculos;
	}

	public Set<Vehiculo> getVehiculos() {
		return new HashSet<>(vehiculos);
	}

	Set<MedioPago> _getMedioPago() {
		return mediosPago;
	}

	public Set<MedioPago> getMediosPago() {
		return new HashSet<>(mediosPago);
	}
}
