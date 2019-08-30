/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SkipPathRequestMatcher
 * 
 * @author vladimir.stankovic
 *
 * Aug 19, 2016
 */
public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private List<RequestMatcher> processingMatchers = new ArrayList<>();
    
    public SkipPathRequestMatcher(List<String> pathsToSkip, List<String> processingPaths) {
        Assert.notNull(pathsToSkip, "Path to skip shouldn't be null");
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        for (String processingPath: processingPaths) {
            processingMatchers.add(new AntPathRequestMatcher(processingPath));
        }
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        for (RequestMatcher processingMatcher: processingMatchers) {
            if (processingMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}
