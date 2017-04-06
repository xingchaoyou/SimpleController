package test;

import java.sql.Connection;

import dao.UserDao;

public class test {
	public static void main(String args[]){
		UserDao userDao = new UserDao();
		Connection ct= userDao.openDBConnection();
		if(ct!=null){
			System.out.println("OK");
		}
		boolean b = userDao.closeDBConnection();
		System.out.println(b);
	}
}
