package model;

import javax.ejb.Local;

@Local
public interface BMCCacheLocal {
	void addToCache(Long id, Object object);

	void removeFromCache(Long id);
	
	Object getFromCache(Long id);

	String getCountryCode(String country);
}
