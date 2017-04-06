package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;

public class RegisterAction implements IRegisterAction{
	public String register(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter("name");
		String pw = req.getParameter("pw");
	
		UserBean userBean = new UserBean();
		if (userBean.checkRegister(name, pw)) {
			return "success";
		} else {
			return "fail";
		}
	}

}
