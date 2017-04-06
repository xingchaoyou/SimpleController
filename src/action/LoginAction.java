package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;
import dao.UserDao;

public class LoginAction implements ILoginAction {
	public String login(HttpServletRequest req, HttpServletResponse resp){
		String name = req.getParameter("name");
		String pw = req.getParameter("pw");
		
		UserBean userBean = new UserBean();
		//以下实现不可识别的请求
		if (name.equals("noresult")) {
			return "noresult";
		}
		
		
		if (userBean.checkLogin(name, pw)) {
			return "success";
		} else {
			return "fail";
		}
	}
}
