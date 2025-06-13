package spring.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

@Component
public class StaticFileFilter implements Filter {

    // Patterns for React async chunks that are requested from root
    private static final Pattern ASYNC_JS_PATTERN = Pattern.compile("^/[0-9]+\\.[a-f0-9]+\\.async\\.js$");
    private static final Pattern PAGE_JS_PATTERN = Pattern.compile("^/p__.*\\.[a-f0-9]+\\.async\\.js$");
    private static final Pattern TEMPLATE_JS_PATTERN = Pattern.compile("^/t__.*\\.[a-f0-9]+\\.async\\.js$");
    private static final Pattern CHUNK_CSS_PATTERN = Pattern.compile("^/t__.*\\.[a-f0-9]+\\.chunk\\.css$");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // Check if this is a static file request that should be served from /static
        if (isStaticFileRequest(requestURI)) {
            serveStaticFile(requestURI, httpResponse);
            return;
        }
        
        // Continue with normal filter chain
        chain.doFilter(request, response);
    }

    private boolean isStaticFileRequest(String uri) {
        return ASYNC_JS_PATTERN.matcher(uri).matches() ||
               PAGE_JS_PATTERN.matcher(uri).matches() ||
               TEMPLATE_JS_PATTERN.matcher(uri).matches() ||
               CHUNK_CSS_PATTERN.matcher(uri).matches();
    }

    private void serveStaticFile(String uri, HttpServletResponse response) throws IOException {
        // Remove leading slash to get filename
        String filename = uri.substring(1);
        
        ClassPathResource resource = new ClassPathResource("static/" + filename);
        
        if (resource.exists()) {
            // Set appropriate content type
            if (filename.endsWith(".js")) {
                response.setContentType("application/javascript");
            } else if (filename.endsWith(".css")) {
                response.setContentType("text/css");
            }
            
            // Copy file content to response
            try (InputStream inputStream = resource.getInputStream()) {
                inputStream.transferTo(response.getOutputStream());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
} 