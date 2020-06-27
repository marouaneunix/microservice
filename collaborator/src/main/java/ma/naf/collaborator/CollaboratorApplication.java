package ma.naf.collaborator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
public class CollaboratorApplication {

	@Bean
	CommandLineRunner commandLineRunner(CollaboratorRepository collaboratorRepository) {
		return strings -> {
			Stream.of("marouane", "ayoub", "fati", "khaoula", "chaimae")
					.forEach(firstName -> collaboratorRepository.save(new Collaborator(firstName, "last_"+firstName)));
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(CollaboratorApplication.class, args);
	}

}

@RestController
@RequestMapping(value = "/collaborators")
class CollaboratorResource {

	private CollaboratorRepository collaboratorRepository;

	private ProjectClient projectClient;

	CollaboratorResource(CollaboratorRepository collaboratorRepository, ProjectClient projectClient) {
		this.collaboratorRepository = collaboratorRepository;
		this.projectClient = projectClient;
	}

	@GetMapping()
	public ResponseEntity<List<Collaborator>> getCollaborators() {
		return ResponseEntity.ok(collaboratorRepository.findAll());
	}

	@GetMapping("names")
	public List<String> getProjects() {
		return this.projectClient.getProjectsName();
	}
}

@ToString
@Setter
@Getter
@Entity
class Collaborator {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String firstName;
	private String lastName;

	public Collaborator() {
	}

	public Collaborator(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}


}

@Repository
interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

}

@FeignClient(value = "project-service", fallback = HystrixProjectFallback.class)
interface ProjectClient {

	@GetMapping("/projects/name")
	List<String> getProjectsName();
}

@Component
class HystrixProjectFallback implements ProjectClient {

	@Override
	public List<String> getProjectsName() {
		return Collections.emptyList();
	}
}
