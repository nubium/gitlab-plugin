package com.dabsquared.gitlabjenkins;

import hudson.triggers.SCMTrigger;

import java.io.File;
import java.io.IOException;

/**
 * Created by daniel on 6/8/14.
 */
public class GitLabPushCause extends SCMTrigger.SCMTriggerCause {

    private final GitLabPushRequest pushRequest;

    public GitLabPushCause(GitLabPushRequest pushRequest) {
        this.pushRequest=pushRequest;
    }

    public GitLabPushCause(GitLabPushRequest pushRequest, File logFile) throws IOException{
        super(logFile);
        this.pushRequest=pushRequest;
    }

    public GitLabPushCause(GitLabPushRequest pushRequest, String pollingLog) {
        super(pollingLog);
        this.pushRequest=pushRequest;
    }

    public GitLabPushRequest getPushRequest() {
        return pushRequest;
    }

    @Override
    public String getShortDescription() {
        String pushedBy;
        String branch;
        pushedBy = pushRequest.getUser_name();
        branch = getSourceBranch(pushRequest);

        if (pushedBy == null) {
            return "Started by GitLab push";
        } else {
            return String.format("Started by %s, branch: %s", pushedBy, branch);
        }
    }

    private String getSourceBranch(GitLabRequest req) {
        String result = null;
        if (req instanceof GitLabPushRequest) {
            result = ((GitLabPushRequest)req).getRef().replaceAll("refs/heads/", "");
        } else {
            result = ((GitLabMergeRequest)req).getObjectAttribute().getSourceBranch();
        }

        return result;
    }
}
