/**
 * 
 */
package org.mitre.openid.connect.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;

/**
 * 
 * Simple mapper that adds ROLE_USER to the authorities map for all queries,
 * plus adds ROLE_ADMIN if the subject and issuer pair are found in the
 * configurable "admins" set.
 * 
 * @author jricher
 * 
 */
public class NamedAdminAuthoritiesMapper implements GrantedAuthoritiesMapper {
	
    private static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
    private static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");

	private Set<SubjectIssuerGrantedAuthority> admins = new HashSet<SubjectIssuerGrantedAuthority>();

	private GrantedAuthoritiesMapper chain = new NullAuthoritiesMapper();
	
	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		
		Set<GrantedAuthority> out = new HashSet<GrantedAuthority>();
		out.addAll(authorities);
		
		for (GrantedAuthority authority : authorities) {
			if (admins.contains(authority)) {
				out.add(ROLE_ADMIN);
			}
        }
		
		// everybody's a user by default
		out.add(ROLE_USER);

		return chain.mapAuthorities(out);
	}

	/**
	 * @return the admins
	 */
	public Set<SubjectIssuerGrantedAuthority> getAdmins() {
		return admins;
	}

	/**
	 * @param admins the admins to set
	 */
	public void setAdmins(Set<SubjectIssuerGrantedAuthority> admins) {
		this.admins = admins;
	}

	/**
	 * @return the chain
	 */
	public GrantedAuthoritiesMapper getChain() {
		return chain;
	}

	/**
	 * @param chain the chain to set
	 */
	public void setChain(GrantedAuthoritiesMapper chain) {
		this.chain = chain;
	}

}
