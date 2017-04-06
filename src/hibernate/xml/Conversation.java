package hibernate.xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bean.UserBean;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Conversation {
	private Connection connection = null;// 数据库链接
	private PreparedStatement ps =null; //sql语句
	private ResultSet rs = null; //返回结果集
	private OR_Mapping or_mapping;
	
	public Connection openDBConnection(){
		Configuration config = new Configuration();
		or_mapping = config.getOr_mapping();
		try {
			Property tempProperty = null;
			String driver_class = null;
			String url_path = null;
			String db_username = null;
			String db_userpassword = null;
			for(int i=0; i < or_mapping.getJdbc().getPropertyList().size(); i++) {
				tempProperty = or_mapping.getJdbc().getPropertyList().get(i);
				if (tempProperty.getName().equals("driver_class")){
					driver_class = tempProperty.getValue();
				}
				if (tempProperty.getName().equals("url_path")){
					url_path = tempProperty.getValue();
				}
				if (tempProperty.getName().equals("db_username")){
					db_username = tempProperty.getValue();
				}
				if (tempProperty.getName().equals("db_userpassword")){
					db_userpassword = tempProperty.getValue();
				}
			}
			Class.forName(driver_class);
			connection = DriverManager.getConnection(url_path,db_username,db_userpassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
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
	
	public UserBean queryUser(String columnName,String value) {
		UserBean userBean = null;

		
		String className = UserBean.class.getSimpleName(); //UserBean这个类是在包bean下，这样写是可以只拿到“UserBean”
		//System.out.println(UserBean.class.getSimpleName());打印结果是UserBean
		//System.out.println(UserBean.class.getName());打印结果是bean.UserBean，包名.类名
		OrClass tempClass = null;
		OrClass rightClass = null;
		for(int i=0; i<or_mapping.getClassList().size(); i++) {
			tempClass = or_mapping.getClassList().get(i);
			if(tempClass.getName().equals(className)){ 
				rightClass = tempClass;
			}
		}
		
		
		try {
			connection = this.openDBConnection();
			String sql = "select * from "+ rightClass.getTable() + " where " + columnName + " = '" + value + "'";
//			System.out.println(sql);
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()){
				Class<?> c = UserBean.class;
				Object o = c.newInstance();
				for (Property property:rightClass.getPropertyList()) {
					String name = property.getName();
					Method m = c.getMethod("set"+name.substring(0, 1).toUpperCase()+name.substring(1),Class.forName("java.lang."+property.getType()));					
					m.invoke(o,rs.getObject(property.getColumn()));
				}//注意：之前是select password from ... 现在是select * from ...所以结果集rs里不只有password，还有username,所以可以每次执行m.invoke这句话是可以的。
				return (UserBean) o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeDBConnection();
		}
		return userBean;
	}
	
	public boolean insertUser(UserBean u) {
		boolean l = false;
		
		String className = UserBean.class.getSimpleName(); 
		OrClass tempClass = null;
		OrClass rightClass = null;
		for(int i=0; i<or_mapping.getClassList().size(); i++) {
			tempClass = or_mapping.getClassList().get(i);
			if(tempClass.getName().equals(className)){ 
				rightClass = tempClass;
			}
		}
		String a = null;
		String b = "";
		String c = "";
		for (Property property:rightClass.getPropertyList()) {
			if(property.getName().equals(rightClass.getId().getName())) continue;
			a = property.getColumn();
			b = b + a + ",";
			c = c + "?" + ",";
		}
		String sql = "insert into " + rightClass.getTable() + "(" + b.substring(0, b.length()-1) + ") values (" + c.substring(0, c.length()-1) + ")";
//		System.out.println(sql);
		//添加之前判断该用户是否已经存在
		UserBean userBean = this.queryUser("Name", u.getName());
		if (userBean == null) {
			try {
				connection = this.openDBConnection();
				ps = connection.prepareStatement(sql);
				Class<?> d = u.getClass();
				for (int i=0; i<rightClass.getPropertyList().size(); i++){
					if(rightClass.getPropertyList().get(i).getName().equals(rightClass.getId().getName())) continue;
					String name = rightClass.getPropertyList().get(i).getName();
					Method m = d.getMethod("get"+name.substring(0, 1).toUpperCase()+name.substring(1));	
//					System.out.println(m.getName());
					Object answer = m.invoke(u);
					ps.setObject(i, answer);
//					System.out.println(i + ":" + answer);
				}
				int k = ps.executeUpdate();
				if(k==1){
					//插入成功
					l = true;
				}                     //public int executeUpdate();该方法返回：1、返回INSERT、UPDATE或者DELETE语句执行后的更新行数；2、返回0表示SQL语句没有执行成功。
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} finally {
				this.closeDBConnection();
			}
		}
		return l;
	}
}
