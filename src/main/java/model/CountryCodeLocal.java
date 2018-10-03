package model;

import javax.ejb.Local;

@Local
public interface CountryCodeLocal {
	String getByCountry(String country);
}
