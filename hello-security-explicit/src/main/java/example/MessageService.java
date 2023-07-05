package example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

	@PreAuthorize("authenticated")
	public String findMessage() {
		return "Hello Authenticated!";
	}
	
	@PreAuthorize("hasRole('USER')")
	public String findUserMessage() {
		return "Hello User!";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public String findAdminMessage() {
		return "Hello Admin!";
	}

}
