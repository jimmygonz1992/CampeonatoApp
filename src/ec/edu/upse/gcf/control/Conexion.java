package ec.edu.upse.gcf.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	public static Connection conectar() {
		Connection con = null;
 
		try {
			String url = "jdbc:mysql://localhost:3306/sgcf_base?user=root&password=12345678";
			con = DriverManager.getConnection(url);
			if (con != null) {
				System.out.println("Conexion Satisfactoria");
			}
 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return con;
	}
}
