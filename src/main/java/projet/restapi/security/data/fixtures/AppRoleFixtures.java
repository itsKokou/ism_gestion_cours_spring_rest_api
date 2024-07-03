package projet.restapi.security.data.fixtures;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projet.restapi.security.services.SecurityService;

//@Component
@RequiredArgsConstructor

public class AppRoleFixtures implements CommandLineRunner {
   private final SecurityService securityService;
    @Override
    public void run(String... args) throws Exception {
          securityService.saveRole("ROLE_PROFESSEUR");
          securityService.saveRole("ROLE_ETUDIANT");
          securityService.saveRole("ROLE_ADMIN");
          securityService.saveRole("ROLE_RP");
          securityService.saveRole("ROLE_AC");

    }
}
