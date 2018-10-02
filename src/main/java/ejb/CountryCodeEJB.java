package ejb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

/*
 * @Singleton instructs the container to produce a single instance of a stateless bean.
 * @Startup is not required here, because CacheEJB is annotated with @Startup and, 
 * as this is a dependency for CacheEJB, it will be initialized before CacheEJB, because
 * the init method is annotated with @PostConstruct.
 */
@Singleton
public class CountryCodeEJB {
	private Map<String, String> countryCodes = new ConcurrentHashMap<>();

	public String getByCountry(String country) {
		return countryCodes.get(country);
	}

	@PostConstruct
	private void init() {
		countryCodes.put("NL", "31");
		countryCodes.put("BE", "32");
		countryCodes.put("LU", "352");
	}
}
