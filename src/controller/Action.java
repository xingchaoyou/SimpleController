package controller;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.omg.PortableInterceptor.IORInterceptor;

@XmlRootElement(name = "action")
public class Action {
	private String name;
	private ActionClass actionClass;
	private List<Result> resultList;
	private InterceptorRef interceptorref;
	
	@XmlElement(name = "interceptor-ref")
	public InterceptorRef getInterceptorref() {
		return interceptorref;
	}
	public void setInterceptorref(InterceptorRef interceptorref) {
		this.interceptorref = interceptorref;
	}
	@XmlElement
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "class")
	public ActionClass getActionclass() {
		return actionClass;
	}
	public void setActionclass(ActionClass actionclass) {
		this.actionClass = actionclass;
	}
	
	@XmlElement(name = "result")
	public List<Result> getResultList() {
		return resultList;
	}
	public void setResultList(List<Result> resultList) {
		this.resultList = resultList;
	}
	
	@Override
	public String toString() {
		return "Action [name=" + name + ", actionClass=" + actionClass + ", resultList=" + resultList
				+ ", Interceptorref=" + interceptorref + "]";
	}
	
	
}
