package jgit;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;

/**
 * Created by annguyen on 10/14/16.
 */
public class Main {
    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
//        jgit.JGitCMD.initRepository(String.valueOf(currentTimeMillis()));
        Repository repoAT = JGitCMD.openRepository(Constants.GIT_HOME, "CI-0.1");

        File newFile = new File(Constants.GIT_HOME +  "hihi");

        Status status = JGitCMD.getStatus(repoAT);
        String strStatus = "";
        if (!status.getAdded().isEmpty()) {
            strStatus += "Added: " + status.getAdded() + "\n";
        }
        if (!status.getChanged().isEmpty()) {
            strStatus += "Changed: " + status.getChanged() + "\n";
        }
        if (!status.getMissing().isEmpty()) {
            strStatus += "Missing: " + status.getMissing() + "\n";
        }
        if (!status.getModified().isEmpty()) {
            strStatus += "Modified: " + status.getModified() + "\n";
        }
        if (!status.getUntracked().isEmpty()) {
            strStatus += "Untracked: " + status.getUntracked() + "\n";
        }
        logger.info(strStatus);

        JGitCMD.getUserConfig(repoAT);
        RevCommit revCommit = JGitCMD.getCommitMessage(repoAT);
        logger.info("\nCommit-Message: " + revCommit.getFullMessage());

//        jgit.JGitCMD.getUserConfig(jgit.Constants.REPOSITORIES_HOME + name);
//        jgit.JGitCMD.getUserConfig("/data/github/tend-sell/sell-web-automated-tests/");
//        jgit.JGitCMD.getCommitMessage("/data/github/tend-sell/sell-web-automated-tests/");
    }
}
