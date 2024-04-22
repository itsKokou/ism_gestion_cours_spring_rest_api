package projet.restapi.security.data.reporitories;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.core.data.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
      AppUser findByLogin(String login);
}
