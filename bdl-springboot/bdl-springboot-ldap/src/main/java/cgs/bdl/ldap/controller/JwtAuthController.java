package cgs.bdl.ldap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@RestController
@RequestMapping("/auth")
public class JwtAuthController {

	@Value("${jwt.key}")
	private String jwtKey;

	@Value("${ldap.domainName}")
	private String ldapDomainName;

	@Autowired
	private LdapTemplate ldapTemplate;


}
