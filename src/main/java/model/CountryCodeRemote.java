package model;

import javax.ejb.Remote;

@Remote
public interface CountryCodeRemote {
	String getByCountry(String country);
}
