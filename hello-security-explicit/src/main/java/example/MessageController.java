package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@GetMapping("/message")
	public String message() {
		return this.messageService.findMessage();
	}

	@GetMapping("/user")
	public String user() {
		return this.messageService.findUserMessage();
	}

	@GetMapping("/admin")
	public String admin() {
		return this.messageService.findAdminMessage();
	}

}
