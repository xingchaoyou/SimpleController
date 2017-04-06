package bean;

import dao.UserDao;

public class UserBean {
	
	private String name=null;
	private String password = null;
	private Integer ID = null;
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean checkLogin(String name, String password) {
		UserDao userDao = new UserDao();
		UserBean userBean = userDao.queryUser(name);
		if(userBean!=null&&password.equals(userBean.getPassword())){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkRegister(String name, String password) {
		UserBean userBean = new UserBean();
		userBean.setName(name);
		userBean.setPassword(password);
		UserDao userDao = new UserDao();
		boolean b = userDao.insertUser(userBean);
		return b;
	}
}
