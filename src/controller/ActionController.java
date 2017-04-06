package controller;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.omg.PortableInterceptor.IORInterceptor;

@XmlRootElement(name = "action-controller")
public class ActionController {
	private List<Action> actionList;
	private List<Interceptor> interceptor;

	@XmlElement(name = "interceptor")
	public List<Interceptor> getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(List<Interceptor> interceptor) {
		this.interceptor = interceptor;
	}

	@XmlElement(name = "action")
	public List<Action> getActionlist() {
		return actionList;
	}

	

	public void setActionlist(List<Action> actionlist) {
		this.actionList = actionlist;
	}

	@Override
	public String toString() {
		return "ActionController [actionList=" + actionList + ", interceptor=" + interceptor + "]";
	}
	
	

}
