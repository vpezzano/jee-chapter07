package numbergeneration;

import java.util.Random;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/*
 * @Dependent can be omitted, as it's the default scope.
 */
@Dependent
public class IsbnGenerator implements NumberGenerator {
	// LoggingProducer is creating this Logger
	@Inject
	private Logger logger;
	
	@Override
	public String generateNumber() {
		String isbn = "13-84356-" + Math.abs(new Random().nextInt());
		logger.info("Generated ISBN: " + isbn);
		return isbn;
	}
}