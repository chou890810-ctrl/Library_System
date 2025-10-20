package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
		
	//資料庫連線設定
	private static final String url=
			"jdbc:mysql://localhost:3306/library_system?useSSL=false&serverTimezone=Asia%2FTaipei&characterEncoding=utf8";
	
	private static final String user="root";
	private static final String password="1234";
	
	//建構子設為private,避免被建立物件
	
	private DbConnection() {}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url,user,password);
		
	}
}

