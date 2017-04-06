package controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionInvocation {
	
	private Object o;
	private Method m;
	private Object[] args;
	private String actionName;
	
	public ActionInvocation(Object o, Method m, Object[] args, String actionName) {
		this.o = o;
		this.m = m;
		this.args = args;
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public Object invoke() {
		try {
			return m.invoke(o, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
