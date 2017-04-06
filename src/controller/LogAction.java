package controller;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action")
public class LogAction {
	private String name;
	private String s_time;
	private String e_time;
	private String result;
	
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "s-time")
	public String getS_time() {
		return s_time;
	}
	public void setS_time(String string) {
		this.s_time = string;
	}
	
	@XmlElement(name = "e-time")
	public String getE_time() {
		return e_time;
	}
	public void setE_time(String string) {
		this.e_time = string;
	}
	
	@XmlElement
	public String getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = (String) result;
	}
	@Override
	public String toString() {
		return "LogAction [name=" + name + ", s_time=" + s_time + ", e_time=" + e_time + ", result=" + result + "]";
	}
	
	
	

}
