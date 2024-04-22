package projet.restapi.security.data.reporitories;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.core.data.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole getByRoleName(String roleName);
}
