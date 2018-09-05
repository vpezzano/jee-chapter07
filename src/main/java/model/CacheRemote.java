package model;

import javax.ejb.Remote;

@Remote
public interface CacheRemote {
	void addToCache(Long id, Object object);

	void removeFromCache(Long id);
	
	Object getFromCache(Long id);

	String getCountryCode(String country);
}
