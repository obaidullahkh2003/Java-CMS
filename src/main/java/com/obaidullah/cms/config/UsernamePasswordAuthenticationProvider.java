package com.obaidullah.cms.config;

import com.obaidullah.cms.model.Roles;
import com.obaidullah.cms.model.Users;
import com.obaidullah.cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<Users> usersList=userRepository.findByEmail(username);
        if(!usersList.isEmpty()){
            if(passwordEncoder.matches(password, usersList.get(0).getPassword())){
                return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(usersList.get(0).getRoles()));
            }else{
                throw new BadCredentialsException("Invalid password");
            }
        }else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private List<GrantedAuthority>getGrantedAuthorities(Set<Roles>roles){
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        for(Roles role:roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
