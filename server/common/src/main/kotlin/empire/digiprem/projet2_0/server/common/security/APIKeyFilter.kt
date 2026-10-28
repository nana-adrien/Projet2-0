package empire.digiprem.projet2_0.server.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
//@Service
class APIKeyFilter(
   @param:Value("\${app.api_key}") private val apiKey:String
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val reqApiKey=request.getHeader("X_API_KEY")
        if (reqApiKey!=apiKey){
            response.sendError(HttpStatus.UNAUTHORIZED.value(),"Invalid Api key ")

            return
        }

        filterChain.doFilter(request, response);
    }
}