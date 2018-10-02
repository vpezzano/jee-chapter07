package logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/*
 * @ApplicationScoped means we will have one instance of LoggingProducer for the entire application.
 */
@ApplicationScoped
public class LoggingProducer {
	@Produces
	private Logger createLogger(InjectionPoint injectionPoint) {
		System.out.println("Type: " + injectionPoint.getType());
		System.out.println("Qualifiers: " + injectionPoint.getQualifiers());
		System.out.println("Bean: " + injectionPoint.getBean());
		System.out.println("Member: " + injectionPoint.getMember());
		System.out.println("Annotated: " + injectionPoint.getAnnotated());
		System.out.println("Delegate: " + injectionPoint.isDelegate());
		System.out.println("Transient: " + injectionPoint.isTransient());
		Logger logger = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
		logger.setLevel(Level.ALL);
		// Without the Handler, the fine and beyond levels won't be logged
		Handler handler = new ConsoleHandler();
		logger.addHandler(handler);
		handler.setLevel(Level.ALL);
		// Now we get double logging; to prevent this, we disable the default handler
		logger.setUseParentHandlers(false);
		logger.info("Logger created from class " + injectionPoint.getMember().getDeclaringClass().getName() + ", hashcode = " + logger.hashCode());
		return logger;
	}
	
	@SuppressWarnings("unused") 
	private void disposeLogger(@Disposes Logger logger) {
		logger.info("Logger disposed");
	}
}
