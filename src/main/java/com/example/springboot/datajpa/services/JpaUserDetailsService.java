package com.example.springboot.datajpa.services;

import com.example.springboot.datajpa.dao.UserDao;
import com.example.springboot.datajpa.domain.Role;
import com.example.springboot.datajpa.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailService")
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            logger.error("Login error: the user does not exist '" + username + "' in the system!");
            throw new UsernameNotFoundException("Username: " + username + " does not exist in the system!");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Role role: user.getRoles()) {
            logger.info("Role: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        if(authorities.isEmpty()) {
            logger.error("Login error: User '" + username + "' has no assigned roles!");
            throw new UsernameNotFoundException("Login error: user '" + username + "' has no assigned roles!");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
    }
}
