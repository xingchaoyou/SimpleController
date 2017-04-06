package controller;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class MyInvocationHandler implements InvocationHandler {
	
	private Object realObject;
	private String actionName;
	private String interceptorName;
	
	public MyInvocationHandler(Object realObject, String actionName, String interceptorName) {
		this.realObject = realObject;
		this.actionName = actionName;
		this.interceptorName = interceptorName;
	}

	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
//		System.out.println(actionName + interceptorName);
		
		ActionInvocation actionInvocation = new ActionInvocation(realObject, arg1, arg2, actionName);
		ActionController a = null;
		try {
			File file = new File(MyInvocationHandler.class.getResource("/controller.xml").getPath());
			JAXBContext jaxbContext = JAXBContext.newInstance(ActionController.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			a = (ActionController) jaxbUnmarshaller.unmarshal(file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		List<Interceptor> interceptors = a.getInterceptor();
		Interceptor rightInterceptor = null;
		for (int i = 0; i < interceptors.size(); i++) {
			Interceptor tempInterceptor = interceptors.get(i);
			if (tempInterceptor.getName().equals(interceptorName)) {
				rightInterceptor = tempInterceptor;
			}
		}
		
//		System.out.println(rightInterceptor.getInterceptorClass().getName());
//		System.out.println(rightInterceptor.getInterceptorClass().getMethod());
		
		try {
			Class c = Class.forName(rightInterceptor.getInterceptorClass().getName());
			Object o = c.newInstance();
			Method m = c.getMethod(rightInterceptor.getInterceptorClass().getMethod(), ActionInvocation.class);
			return m.invoke(o, actionInvocation);			
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
		return null;
	}
}
