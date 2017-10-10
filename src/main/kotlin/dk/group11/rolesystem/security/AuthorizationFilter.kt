package dk.group11.rolesystem.security

import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(authenticationManager: AuthenticationManager?) : BasicAuthenticationFilter(authenticationManager) {


    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {


        val header = req.getHeader(HEADER_STRING)

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }

        val authentication: UsernamePasswordAuthenticationToken = getAuthentication(req)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val token = request.getHeader(HEADER_STRING)
        // if (token != null) {
        // parse the token.
        val user = Jwts.parser()
                .setSigningKey(SECRET.toByteArray())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .body
                .subject

//            if (user != null) {
        return UsernamePasswordAuthenticationToken(user, null, ArrayList())
//            }
//            return null
//        }
//        return null

    }


}