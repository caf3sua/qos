package com.viettelperu.qos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.viettelperu.qos.util.EncryptionUtil;

public class EncryptionApp {

	public static void main(String[] args) {
		// Get argurment
//		try {
//	        String data = args[0];
//	        if (StringUtils.isNotEmpty(data)) {
//	        	String dataEncrypted = EncryptionUtil.encrypt(data);
//	        	System.out.println(dataEncrypted);
//	        }
//	    }
//	    catch (ArrayIndexOutOfBoundsException e){
//	        System.out.println("ArrayIndexOutOfBoundsException caught");
//	    }
//	    finally {
//
//	    }
		
		String jdbcUrl = args[0];
        String username = args[1];
        String password = args[2];
//		String jdbcUrl = "jdbc:oracle:thin:@118.71.224.225:1521:orcldb";
//        String username = "qos";
//        String password = "12345678";
        
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					jdbcUrl, username,
					password);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

}
