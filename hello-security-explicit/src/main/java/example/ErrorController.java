package example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ErrorController {
	
	@GetMapping("/error/access-denied")
	public String accessDenied() {
		log.info("accessDenied");
		return "error";
	}

}
