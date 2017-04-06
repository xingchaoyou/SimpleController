package interceptor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import controller.ActionInvocation;
import controller.Log;
import controller.LogAction;

public class LogWriter {
	public Object log(ActionInvocation ai) {
		Date begin = new Date();
		Object result = ai.invoke();
		Date end = new Date();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//		System.out.println(format.format(begin));
//		System.out.println(format.format(end));
//		System.out.println(result);
//		System.out.println(ai.getActionName());
	
		Log log = new Log();
		List<LogAction> logActions = log.getLogAction();
		LogAction logAction = new LogAction();
		logAction.setName(ai.getActionName());
		logAction.setS_time(format.format(begin));
		logAction.setE_time(format.format(end));
		logAction.setResult(result);
		
		try {
			File file = new File("C:\\workspace\\log.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Log.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			log = (Log) jaxbUnmarshaller.unmarshal(file);
			
			log.getLogAction().add(logAction);
			
			file = new File("C:\\workspace\\log.xml");
			jaxbContext = JAXBContext.newInstance(Log.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(log, file);
//			jaxbMarshaller.marshal(log, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return result;
	}
}
