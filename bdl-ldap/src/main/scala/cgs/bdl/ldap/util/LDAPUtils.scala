package cgs.bdl.ldap.util

import java.util
import javax.naming.Context
import javax.naming.ldap.InitialLdapContext

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object LDAPUtils {
	
	val ldapUrl: String =  "ldap://ads.homedo.com:389/"
	val baseDN: String = "dc=homedo,dc=com"
	def login(userID: String, password: String): Unit = {
		ldapConfigCheck(ldapUrl, baseDN)
		val ctx: util.Hashtable[String, String] = initLdapContext(userID, password, ldapUrl, baseDN)
		new InitialLdapContext(ctx, null)
		println(s"user $userID login success.")
	}
	
	private def initLdapContext(userID: String, password: String, url: String, dn: String): util.Hashtable[String, String] = {
		val env: util.Hashtable[String, String] = new util.Hashtable[String, String]()
		env.put(Context.SECURITY_AUTHENTICATION, "simple")
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory")
		env.put(Context.PROVIDER_URL, url + dn)
		if (userID.contains("@")) {
			env.put(Context.SECURITY_PRINCIPAL, userID)
		} else {
			env.put(Context.SECURITY_PRINCIPAL, s"$userID@${concatDomain(dn)}")
		}
		env.put(Context.SECURITY_CREDENTIALS, password)
		env
	}
	
	private def concatDomain(dn: String): String = {
		val domains: Array[String] = dn.trim.split(",")
		val domain: String = domains.map { e =>
			e.trim.split("\\s*=\\s*")(1).trim
		}.mkString(".")
		println(s"=====> concat domain: [${domain}] from baseDN: [$dn]")
		domain
	}
	
	private def ldapConfigCheck(url:String, dn: String): Unit = {
		require(url.endsWith("/"), s"===> ldap url: [$url] 配置有误, 必须以'/'结尾  ")
		require((dn.contains(",") && dn.split("\\s*,\\s*").length == 2), s"===> ldap baseDN: [$dn] 配置有误")
	}
	
	def main(args: Array[String]): Unit = {
		login("caiyi", "7739277328")
	}
}
