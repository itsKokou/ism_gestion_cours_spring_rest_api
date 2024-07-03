package projet.restapi.security.services;

import projet.core.data.entities.AppRole;
import projet.core.data.entities.AppUser;

import java.util.Optional;

public interface SecurityService {
    AppUser getUserByUsername(String username);
    Optional<AppUser> getUserById(Long id);
    AppUser saveUser(String username, String password);
    AppRole saveRole(String roleName);
    AppRole getRoleByName(String roleName);
    void addRoleToUser(String username,String roleName);
    void removeRoleToUser(String username,String roleName);
}
