package model;

import javax.ejb.Remote;

@Remote
public interface BookRemote {
	String getRemoteMessage();
}