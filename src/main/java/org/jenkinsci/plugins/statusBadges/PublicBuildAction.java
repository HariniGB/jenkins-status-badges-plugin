/*
 * The MIT License
 *
 * Copyright 2013 Dominik Bartholdi.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.statusBadges;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.UnprotectedRootAction;
import hudson.model.AbstractProject;
import hudson.security.ACL;
import hudson.security.Permission;
import hudson.security.PermissionScope;
import hudson.util.HttpResponses;

import java.io.IOException;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Exposes the build status badge via unprotected URL.
 *
 * http://localhost:8080/status-badges/build/icon?job=[JOBNAME]
 */
@Extension
public class PublicBuildAction implements UnprotectedRootAction {
    private final ImageResolver iconResolver;
    private final BuildStatus buildStatus;
    public static Permission VIEW_STATUS;

    public PublicBuildAction() throws IOException {
        iconResolver = new ImageResolver();
        buildStatus = new BuildStatus();
        VIEW_STATUS = buildStatus.VIEW_STATUS;
    }

    public String getUrlName() {
        return "status-badges/build";
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public HttpResponse doIcon(StaplerRequest req, StaplerResponse rsp, @QueryParameter String job) throws IOException, ServletException {
        AbstractProject<?, ?> project = buildStatus.getProject(job, req, rsp);
        return iconResolver.getImage(project.getIconColor());
    }

}
