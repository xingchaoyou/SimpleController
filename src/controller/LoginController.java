package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import action.LoginAction;
import action.RegisterAction;
import bean.UserBean;

public class LoginController extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionName = req.getServletPath().substring(1).split("\\.")[0];
		ActionController a = null;
		try {
			File file = new File(LoginController.class.getResource("/controller.xml").getPath());
			JAXBContext jaxbContext = JAXBContext.newInstance(ActionController.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			a = (ActionController) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		List<Action> actions = a.getActionlist();
		Action rightAction = null;
		for (int i = 0; i < actions.size(); i++) {
			Action tempAction = actions.get(i);
			if (tempAction.getName().equals(actionName)) {
				rightAction = tempAction;
			}
		}
		if (rightAction == null) {
			req.getRequestDispatcher("/pages/no_action.html").forward(req, resp);
			return;
		}
		
		InterceptorRef InterceptorRef = rightAction.getInterceptorref();
		String result = "";
		try {
			Class c = Class.forName(rightAction.getActionclass().getName()).getInterfaces()[0];
			Class realc = Class.forName(rightAction.getActionclass().getName());
			Object realo = Class.forName(rightAction.getActionclass().getName()).newInstance();
			Method m = c.getMethod(rightAction.getActionclass().getMethod(), HttpServletRequest.class,
					HttpServletResponse.class);
			Object proxyo = Proxy.newProxyInstance(c.getClassLoader(), Class.forName(rightAction.getActionclass().getName()).getInterfaces(), new MyInvocationHandler(realo, rightAction.getName(), rightAction.getInterceptorref().getName()));
			result = (String) m.invoke(proxyo, req, resp);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
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
			e.getTargetException().printStackTrace();
		}
		
		List<Result> results = rightAction.getResultList();
		for (int j = 0; j < results.size(); j++) {
			Result rightResult = results.get(j);
			if (rightResult.getName().equals(result)) {
				if (rightResult.getType().equals("forward")) {
					if (rightResult.getValue().endsWith("xml")) {
						xml2Html(rightResult.getValue(), req, resp);
					} else {
						req.getRequestDispatcher(rightResult.getValue()).forward(req, resp);				
					}
				} else if (rightResult.getType().equals("redirect")) {
					resp.sendRedirect(rightResult.getValue());
				}
				return;
			}
		}
		req.getRequestDispatcher("/pages/no_result.html").forward(req, resp);
		return;
	}

	private void xml2Html(String xmlPath, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String rootPath = req.getSession().getServletContext().getRealPath("/");
			TransformerFactory factory = TransformerFactory.newInstance();
			Templates template = factory.newTemplates(new StreamSource(new FileInputStream(rootPath + "pages/success_view.xsl")));
	        Transformer xformer = template.newTransformer();
	        Source source = new StreamSource(new FileInputStream(rootPath + xmlPath));
	        StreamResult result = new StreamResult(resp.getOutputStream());
	        xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}


