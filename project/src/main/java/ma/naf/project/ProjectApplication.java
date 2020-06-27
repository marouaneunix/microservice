package ma.naf.project;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}

@RestController
@RequestMapping("/projects")
@Slf4j
class ProjectResource {

	@GetMapping("/name")
	public List<String> getAllNames() {
		log.info("get All Names");
		return asList("CINATIS", "AFB", "ALG", "AS24");
	}

}