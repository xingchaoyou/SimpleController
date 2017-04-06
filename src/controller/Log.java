package controller;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "log")
public class Log {
	private List<LogAction> logAction;

	@XmlElement(name = "action")
	public List<LogAction> getLogAction() {
		return logAction;
	}

	public void setLogAction(List<LogAction> logAction) {
		this.logAction = logAction;
	}

	@Override
	public String toString() {
		return "Log [logAction=" + logAction + "]";
	}
}
