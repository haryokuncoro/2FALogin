package com.project._FALogin.config;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Component
public class RateLimitFilter implements Filter {

    // map ip -> bucket (Guava cache to evict)
    private final Cache<String, Bucket> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(10000)
            .build();

    private Bucket newBucket() {
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1)); // 5 tokens/min
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        Bucket bucket = cache.getIfPresent(ip);
        if (bucket == null) {
            bucket = newBucket();
            cache.put(ip, bucket);
        }
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(429);
            resp.getWriter().write("Too many requests - try later");
        }
    }
}