package com.sedooe.actuator.config;

import org.springframework.boot.actuate.trace.TraceProperties;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.actuate.trace.WebRequestTraceFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class RequestTraceFilter extends WebRequestTraceFilter {

    private final String[] excludedEndpoints = new String[]{"/css/**", "/js/**", "/trace"};

    RequestTraceFilter(TraceRepository repository, TraceProperties properties) {
        super(repository, properties);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TeeHttpServletResponse teeResponse = new TeeHttpServletResponse(response);

        filterChain.doFilter(request, teeResponse);

        teeResponse.finish();

        request.setAttribute("body", teeResponse.getOutputBuffer());

        super.doFilterInternal(request, teeResponse, filterChain);
    }

    @Override
    protected Map<String, Object> getTrace(HttpServletRequest request) {
        Map<String, Object> trace = super.getTrace(request);
        byte[] outputBuffer = (byte[]) request.getAttribute("body");

        if (outputBuffer != null) {
            trace.put("body", new String(outputBuffer));
        }

        return trace;
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }
}