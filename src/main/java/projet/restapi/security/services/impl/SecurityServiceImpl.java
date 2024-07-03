package projet.restapi.security.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projet.core.data.entities.AppRole;
import projet.core.data.entities.AppUser;
import projet.core.exceptions.EntityNotFoundException;
import projet.restapi.security.data.reporitories.AppRoleRepository;
import projet.restapi.security.data.reporitories.AppUserRepository;
import projet.restapi.security.services.SecurityService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService, UserDetailsService {
   private final AppUserRepository appUserRepository;
   private final AppRoleRepository appRoleRepository;
   private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByLogin(username);
    }

    @Override
    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    @Override
    public AppUser saveUser(String username, String password) {
        AppUser user = appUserRepository.findByLogin(username);
        if (user != null) throw new RuntimeException("User exist in app ");
        user=new AppUser(username, passwordEncoder.encode(password));
        return  appUserRepository.save(user);
    }

    @Override
    public AppRole saveRole(String roleName) {
         AppRole role= appRoleRepository.getByRoleName(roleName);
        if (role != null) throw new RuntimeException("Role exist");
        role= new AppRole(roleName,null);
        return appRoleRepository.save(role);
    }

    @Override
    public AppRole getRoleByName(String roleName) {
        AppRole role= appRoleRepository.getByRoleName(roleName);
        if (role == null) throw new EntityNotFoundException("Role not found ");
        return role;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser user = appUserRepository.findByLogin(username);
        if (user == null) throw new EntityNotFoundException("User not found ");
        AppRole role= appRoleRepository.getByRoleName(roleName);
        if (role == null) throw new EntityNotFoundException("Role not found ");
        user.getRoles().add(role);
        appUserRepository.save(user);
    }

    @Override
    public void removeRoleToUser(String username, String roleName) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByLogin(username);
        if (appUser == null) throw new RuntimeException("User not found ");
        List<SimpleGrantedAuthority> authorities = appUser.getRoles()
                .stream()
                .map(appRole -> new SimpleGrantedAuthority(appRole.getRoleName())).toList();
           return new User(appUser.getLogin(), appUser.getPassword(),authorities);
    }
}
