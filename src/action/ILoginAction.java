package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILoginAction {
	public String login(HttpServletRequest req, HttpServletResponse resp);
}
