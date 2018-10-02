package model;

import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface BMCCacheRemote {
	void addToCache(Long id, Object object);

	void removeFromCache(Long id);
	
	Object getFromCache(Long id);

	String getCountryCode(String country);
	
	Map<Long, Object> getCache();
}
