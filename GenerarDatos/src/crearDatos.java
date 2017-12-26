import java.sql.*;
import java.sql.Date;
import java.util.*;

import alb.util.jdbc.Jdbc;
import alb.util.math.Round;

public class crearDatos {
	static int min_existencias, max_existencias, existencias;
	static int id = 1, codigo = 1000;
	static Connection con;
	static PreparedStatement ps;
	static ResultSet rs;

	public static void main(String[] args) {
		borrarTablasAmpliacion();
		crearTablasNuevas();
		modificarTRepuestos();
		borrarDatosTablasAmpliacion();
		generarTProveedores();
		generarTpedidos(200);
		generarTPedidosPendientes(20);
		generarTSuministros();
		generarTDetalles();
		System.err.println("FIIIIIIIN");
	}

	public static void borrarTablasAmpliacion() {
		try {
			con = Jdbc.getConnection();
			try {
				ps = con.prepareStatement("DROP TABLE TDETALLES");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("No esta creada la tabla TDETALLES");
			}
			try {
				ps = con.prepareStatement("DROP TABLE TSUMINISTROS");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("No esta creada la tabla TSUMINISTROS");
			}
			try {
				ps = con.prepareStatement("DROP TABLE TPEDIDOS");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("No esta creada la tabla TPEDIDOS");
			}
			try {
				ps = con.prepareStatement("DROP TABLE TPROVEEDORES");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("No esta creada la tabla TPROVEEDORES");
			}
			System.out.println(
					"Se borraron correctamente las tablas nuevas de la ampliaci�n");
		} catch (SQLException e) {
			System.err.println(
					"Fallo al borrar las tablas nuevas de la ampliaci�n");
		} finally {
			Jdbc.close(rs, ps, con);
		}
	}

	public static void crearTablasNuevas() {
		try {
			con = Jdbc.getConnection();
			try {
				ps = con.prepareStatement("CREATE TABLE TPROVEEDORES("
						+ "ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"
						+ "NOMBRE VARCHAR(255) NOT NULL,"
						+ "CODIGO VARCHAR(255)" + ")");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("Ya esta creada la tabla TPROVEEDORES");
			}
			try {
				ps = con.prepareStatement("CREATE TABLE TPEDIDOS("
						+ "ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"
						+ "CODIGO VARCHAR(255),"
						+ "FECHA_CREACION DATE NOT NULL,"
						+ "FECHA_RECEPCION DATE," + "ESTADO VARCHAR(255),"
						+ "PROVEEDOR_ID BIGINT,"
						+ "CONSTRAINT TPEDIDOS_TPROVEEDORES_FK FOREIGN KEY(PROVEEDOR_ID) REFERENCES \"TPROVEEDORES\" (ID) on delete cascade on update cascade,"
						+ "CONSTRAINT TPEDIDOS_ESTADO_CK CHECK (ESTADO IN('PEDIDO','RECIBIDO'))"
						+ ")");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("Ya esta creada la tabla TPEDIDOS");
			}
			try {
				ps = con.prepareStatement("CREATE TABLE TDETALLES("
						+ "PEDIDO_ID BIGINT," + "REPUESTO_ID BIGINT,"
						+ "UNIDADES INTEGER NOT NULL,"
						+ "PRECIO DOUBLE NOT NULL,"
						+ "CONSTRAINT TDETALLES_PK PRIMARY KEY(PEDIDO_ID,REPUESTO_ID),"
						+ "CONSTRAINT TDETALLES_TPEDIDO_FK FOREIGN KEY (PEDIDO_ID) REFERENCES \"TPEDIDOS\" (ID) on delete cascade on update cascade,"
						+ "CONSTRAINT TDETALLES_TREPUESTO_FK FOREIGN KEY (REPUESTO_ID) REFERENCES \"TREPUESTOS\" (ID) on delete cascade on update cascade,"
						+ "CONSTRAINT TDETALLES_UNIDADES_CK CHECK (UNIDADES > 0.0),"
						+ "CONSTRAINT TDETALLES_PRECIO_CK CHECK (PRECIO > 0.0)"
						+ ")");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("Ya esta creada la tabla TDETALLES");
			}
			try {
				ps = con.prepareStatement("CREATE TABLE TSUMINISTROS("
						+ "REPUESTO_ID BIGINT," + "PROVEEDOR_ID BIGINT,"
						+ "PRECIO DOUBLE NOT NULL,"
						+ "CONSTRAINT TSUMINISTROS_PK PRIMARY KEY(REPUESTO_ID,PROVEEDOR_ID),"
						+ "CONSTRAINT TSUMINISTROS_TPEDIDO_FK FOREIGN KEY (REPUESTO_ID) REFERENCES \"TREPUESTOS\" (ID) on delete cascade on update cascade,"
						+ "CONSTRAINT TSUMINISTROS_TREPUESTO_FK FOREIGN KEY (PROVEEDOR_ID) REFERENCES \"TPROVEEDORES\" (ID) on delete cascade on update cascade,"
						+ "CONSTRAINT TSUMINISTROS_PRECIO_CK CHECK (PRECIO > 0.0))");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println("Ya esta creada la tabla TSUMINISTROS");
			}
			System.out.println(
					"Se crearon correctamente las tablas nuevas de la ampliaci�n");
		} catch (SQLException e) {
			System.err.println(
					"Fallo al crear las tablas nuevas de la ampliaci�n");
		} finally {
			Jdbc.close(rs, ps, con);
		}

	}

	public static void modificarTRepuestos() {
		try {
			con = Jdbc.getConnection();
			try {
				ps = con.prepareStatement(
						"ALTER TABLE TREPUESTOS ADD EXISTENCIAS INTEGER");
				ps.executeUpdate();
				ps = con.prepareStatement(
						"ALTER TABLE TREPUESTOS ADD MAX_EXISTENCIAS INTEGER");
				ps.executeUpdate();
				ps = con.prepareStatement(
						"ALTER TABLE TREPUESTOS ADD MIN_EXISTENCIAS INTEGER");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println(
						"No se a�adieron los nuevos campos a Trepuestos");
			}

			ps = con.prepareStatement("SELECT ID FROM TREPUESTOS");
			rs = ps.executeQuery();
			while (rs.next()) {
				String sentencia = "UPDATE TREPUESTOS SET EXISTENCIAS = ";
				generarExistenciasAleatorias();
				sentencia += existencias + ",";
				sentencia += " MAX_EXISTENCIAS = ";
				sentencia += max_existencias + ",";
				sentencia += " MIN_EXISTENCIAS = ";
				sentencia += min_existencias;
				sentencia += " WHERE ID = ";
				sentencia += rs.getInt(1);
				PreparedStatement ps2 = con.prepareStatement(sentencia);
				ps2.executeUpdate();
				ps2.close();
			}
		} catch (SQLException e) {
			System.err.println(
					"Fallo al introducir las existencias en TRepuestos");
		} finally {
			Jdbc.close(rs, ps, con);
		}
	}

	private static void borrarDatosTablasAmpliacion() {
		try {
			con = Jdbc.getConnection();
			try {
				ps = con.prepareStatement("DELETE FROM TDETALLES");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println(
						"No se pudieron borrar los datos de la tabla TDETALLES");
			}
			try {
				ps = con.prepareStatement("DELETE FROM TSUMINISTROS");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println(
						"No se pudieron borrar los datos de la tabla TDETALLES");
			}
			try {
				ps = con.prepareStatement("DELETE FROM TPEDIDOS");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println(
						"No se pudieron borrar los datos de la tabla TDETALLES");
			}
			try {
				ps = con.prepareStatement("DELETE FROM TPROVEEDORES");
				ps.executeUpdate();
			} catch (Exception e) {
				System.err.println(
						"No se pudieron borrar los datos de la tabla TDETALLES");
			}
			System.out.println(
					"Borrado correctamente los datos de la tablas nuevas de la ampilaci�n");
		} catch (SQLException e) {
			System.out.println("No se borraron la tablas");
		} finally {
			Jdbc.close(ps);
			Jdbc.close(con);
		}
	}

	public static void generarTProveedores() {
		try {
			List<String> proveedores = new ArrayList<>();
			proveedores.add(
					"INSERT INTO TPROVEEDORES VALUES(1,'Carrocerias Somonte','33201')");
			proveedores.add(
					"INSERT INTO TPROVEEDORES VALUES(2,'Crastir Carroceros','33420')");
			proveedores
					.add("INSERT INTO TPROVEEDORES VALUES(3,'Lualjo','33851')");
			proveedores.add(
					"INSERT INTO TPROVEEDORES VALUES(4,'El Calibre','69420')");
			proveedores.add(
					"INSERT INTO TPROVEEDORES VALUES(5,'Recambios Meres','32580')");
			con = Jdbc.getConnection();
			for (String sentencia : proveedores) {
				PreparedStatement ps = con.prepareStatement(sentencia);
				ps.executeUpdate();
			}
			System.out.println(
					"Se crearon correctamente las filas de la tabla TPROVEEDORES");
		} catch (SQLException e) {
			System.out
					.println("Fallo al introducir los valores en TPROVEEDORES");
		} finally {
			Jdbc.close(ps);
			Jdbc.close(con);
		}

	}

	public static void generarTSuministros() {
		try {
			int numProveedores = 0;
			con = Jdbc.getConnection();
			ps = con.prepareStatement("SELECT COUNT(*) FROM TPROVEEDORES");
			rs = ps.executeQuery();
			while (rs.next()) {
				numProveedores = rs.getInt(1);
			}
			ps = con.prepareStatement("SELECT * FROM TREPUESTOS");
			rs = ps.executeQuery();
			while (rs.next()) {
				int aux = numProveedores;
				double precioRepuesto = rs.getDouble("Precio");
				int proovedorPrecioJusto = generarAleatorio(numProveedores);
				int id = rs.getInt(1);
				while (aux > 0) {
					if (aux != proovedorPrecioJusto)
						precioRepuesto += generarAleatorio(50);
					String sentencia = "INSERT INTO TSUMINISTROS VALUES(";
					sentencia += id + ",";
					sentencia += aux + ",";
					sentencia += Round.twoCents(precioRepuesto);
					sentencia += "E0)";
					PreparedStatement ps2 = con.prepareStatement(sentencia);
					ps2.executeUpdate();
					ps2.close();
					aux--;
					precioRepuesto = rs.getDouble("Precio");
				}
			}
			System.out.println(
					"Se crearon correctamente las filas de la tabla TSUMINISTROS");
		} catch (SQLException e) {
			System.err.println(
					"Fallo al generar las filas de la tabla TSUMINISTROS");
		} finally {
			Jdbc.close(rs, ps, con);
		}
	}

	private static void generarExistenciasAleatorias() {
		min_existencias = generarAleatorio(5);
		max_existencias = generarAleatorio(20);
		existencias = generarAleatorio(20);
		while (max_existencias < min_existencias)
			max_existencias = generarAleatorio(20);
		while (max_existencias < existencias)
			max_existencias = existencias;
		if (existencias < min_existencias) {
			existencias = min_existencias;
		}
	}

	@SuppressWarnings("deprecation")
	public static void generarTpedidos(int contador) {
		try {
			con = Jdbc.getConnection();
			java.sql.Date creacion = generarFechaAleatoria();
			while (contador > 0) {
				String sentencia = "INSERT INTO TPEDIDOS VALUES(";
				sentencia += id;
				id++;
				sentencia += ",'" + codigo + "','";
				codigo += 10;
				while (creacion.after(new java.sql.Date(
						GregorianCalendar.getInstance().getTimeInMillis())))
					creacion = generarFechaAleatoria();
				sentencia += creacion + "',";
				creacion.setDate(creacion.getDate() + generarAleatorio(5));
				sentencia += "'" + creacion + "',";
				sentencia += "'RECIBIDO'";
				sentencia += "," + (generarAleatorio(5));
				sentencia = sentencia.toUpperCase() + ")";
				PreparedStatement ps = con.prepareStatement(sentencia);
				ps.executeUpdate();
				contador--;
			}
			System.out.println(
					"Se crearon correctamente las filas de la tabla TPEDIDOS");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err
					.println("Fallo al generar las filas de la tabla TPEDIDOS");
		} finally {
			Jdbc.close(ps);
			Jdbc.close(con);
		}

	}

	@SuppressWarnings("deprecation")
	public static void generarTPedidosPendientes(int contador) {
		try {
			con = Jdbc.getConnection();
			while (contador > 0) {
				String sentencia = "INSERT INTO TPEDIDOS VALUES(";
				sentencia += id;
				id++;
				sentencia += ",'" + codigo + "','";
				codigo += 10;
				java.sql.Date creacion = generarFechaAleatoria();
				java.sql.Date hoy = new java.sql.Date(
						GregorianCalendar.getInstance().getTimeInMillis());
				while (hoy.getTime() - creacion.getTime() > 1814400000
						|| creacion.after(hoy))
					creacion = generarFechaAleatoria();
				sentencia += creacion + "',";
				creacion.setDate(creacion.getDate() + generarAleatorio(5));
				sentencia += "'" + creacion + "',";
				sentencia += "'PEDIDO'";
				sentencia += "," + (generarAleatorio(5));
				sentencia = sentencia.toUpperCase() + ")";
				PreparedStatement ps = con.prepareStatement(sentencia);
				ps.executeUpdate();
				contador--;
			}
			System.out.println(
					"Se crearon correctamente las filas de la tabla TPEDIDOS pendientes");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err
					.println("Fallo al generar las filas de la tabla TPEDIDOS");
		} finally {
			Jdbc.close(ps);
			Jdbc.close(con);
		}

	}

	public static void generarTDetalles() {
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement("SELECT ID FROM TPEDIDOS");
			rs = ps.executeQuery();
			while (rs.next()) {
				int numeroRepuesto = generarAleatorio(10);
				List<Integer> listaRepuestosActual = new ArrayList<>();
				while (numeroRepuesto > 0) {
					int repuesto = generarAleatorio(25);
					int unidades = generarAleatorio(10);
					if (!listaRepuestosActual.contains(repuesto)) {
						PreparedStatement ps2 = con
								.prepareStatement("SELECT s.PRECIO "
										+ "FROM TPEDIDOS p, TPROVEEDORES pr, TSUMINISTROS s "
										+ "WHERE p.PROVEEDOR_ID = pr.ID "
										+ "AND pr.ID = s.PROVEEDOR_ID "
										+ "AND s.REPUESTO_ID = ?");
						ps2.setLong(1, repuesto);
						ResultSet rs2 = ps2.executeQuery();
						double precioUnidad = 0;
						while (rs2.next()) {
							precioUnidad = rs2.getDouble(1);
						}
						String sentencia = "INSERT INTO TDETALLES VALUES(";
						sentencia += rs.getInt(1) + ",";
						sentencia += repuesto + ",";
						sentencia += unidades + ",";
						sentencia += Round.twoCents(precioUnidad);
						sentencia = sentencia + ")";
						ps2 = con.prepareStatement(sentencia);
						ps2.executeUpdate();
						Jdbc.close(rs2, ps2);
						listaRepuestosActual.add(repuesto);
						numeroRepuesto--;
					} else
						continue;
				}
			}
			System.out.println(
					"Se crearon correctamente las filas de la tabla TDETALLES");
		} catch (SQLException e) {
			System.out.println("Fallo al introducir los valores en TDETALLES");
		} finally {
			Jdbc.close(rs, ps, con);
		}
	}

	private static Date generarFechaAleatoria() {
		Calendar unaFecha = GregorianCalendar.getInstance();
		unaFecha.set(generarAleatorio(4) + 2012, generarAleatorio(12) + 1,
				generarAleatorio(28) + 1);
		return new java.sql.Date(unaFecha.getTimeInMillis());
	}

	private static int generarAleatorio(int maximo) {
		Random random = new Random();
		return (int) (random.nextDouble() * maximo) + 1;
	}

}