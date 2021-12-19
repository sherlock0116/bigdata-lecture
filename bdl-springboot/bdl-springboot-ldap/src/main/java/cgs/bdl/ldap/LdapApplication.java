package cgs.bdl.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@SpringBootApplication
public class LdapApplication {

	@Value("${ldap.url}")
	private String ldapUrl;

	@Value("${ldap.base}")
	private String ldapBase;

	@Value("${ldap.userDn}")
	private String ldapUserDN;

	@Value("${ldap.userPwd}")
	private String ldapUserPwd;

	@Bean
	public LdapTemplate ldapTemplate () {
		return new LdapTemplate(ldapContextSource());
	}

	@Bean
	public LdapContextSource ldapContextSource() {
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl(ldapUrl);
		ldapContextSource.setBase(ldapBase);
		ldapContextSource.setUserDn(ldapUserDN);
		ldapContextSource.setPassword(ldapUserPwd);
		return ldapContextSource;
	}

	public static void main (String[] args) {
		SpringApplication.run(LdapApplication.class, args);
	}

}
