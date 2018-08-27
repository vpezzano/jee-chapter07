package model;

import javax.ejb.Remote;

@Remote
public interface ItemRemote {
	String getRemoteMessage();
}