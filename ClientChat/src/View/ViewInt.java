package View;

import DataTransferObject.*;

public interface ViewInt{

	void addToContactList(User userObj);
	void rejectAddRequest();
	void notify(String email);
	void changeStatusIcon(String email,String status);

}
