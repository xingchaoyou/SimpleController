package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bean.UserBean;
import hibernate.xml.Conversation;

public class UserDao {
	
	private Connection connection = null;// 数据库链接
	private PreparedStatement ps =null; //sql语句
	private ResultSet rs = null; //返回结果集
	
	private String URL = "jdbc:mysql://localhost:3306/j2ee";
	private String USER = "root";
	private String PASSWORD = "123456";
	
	/**
	 * 获取数据库链接
	 * @return
	 */
	public Connection openDBConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 关闭链接
	 * @return
	 */
	public boolean closeDBConnection(){
		boolean b = false;
		try {
			if(rs!=null){
				rs.close();
				rs=null;
			}
			if(ps!=null){
				ps.close();
				ps=null;
			}
			if(connection!=null){
				connection.close();
				connection=null;
			}
			b = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
	//查询用户
	public UserBean queryUser(String userName) {
		Conversation conv = new Conversation();
		conv.openDBConnection();
		UserBean userBean = conv.queryUser("Name", userName);
		conv.closeDBConnection();
		return userBean;
		
//		UserBean userBean = null;
//		try {
//			connection = this.openDBConnection();
//			ps = connection.prepareStatement("select * from user where name='"+userName+"'");
//			rs = ps.executeQuery();
//			if(rs.next()){
//				//存在该用户
//				userBean = new UserBean();
//				userBean.setName(userName);
//				userBean.setPassword(rs.getString("password"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			this.closeDBConnection();
//		}
//		return userBean;
	}
	
	//添加用户
	public boolean insertUser(UserBean u) {
		
		Conversation conv = new Conversation();
		conv.openDBConnection();
		Boolean result = conv.insertUser(u);
		conv.closeDBConnection();
		return result;
		
//		boolean b = false;
//		String sql = "insert into user (" + a + ") values (" + b + ")";
//		//添加之前判断该用户是否已经存在
//		UserBean userBean = this.queryUser(u.getName());
//		if (userBean == null) {
//			try {
//				connection = this.openDBConnection();
//				ps = connection.prepareStatement(sql);
//				ps.setString(1, u.getName());
//				ps.setString(2, u.getPassword());
//				System.out.println("sql:"+sql);
//				int a = ps.executeUpdate();
//				if(a==1){
//					//插入成功
//					b = true;
//				}
//				//public int executeUpdate();该方法返回：1、返回INSERT、UPDATE或者DELETE语句执行后的更新行数；2、返回0表示SQL语句没有执行成功。
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} finally {
//				this.closeDBConnection();
//			}
//		}
//		return b;
	}
}
