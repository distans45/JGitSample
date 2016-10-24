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
//        jgit.Command.createRepository(String.valueOf(currentTimeMillis()));
        Repository repoAT = Command.openRepository(Constants.GIT_HOME, "CI-0.1");

        File newFile = new File(Constants.GIT_HOME +  "hihi");

        Status status = Command.getStatus(repoAT);
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

        Command.getUserConfig(repoAT);
        RevCommit revCommit = Command.getCommitMessage(repoAT);
        logger.info("\nCommit-Message: " + revCommit.getFullMessage());

//        jgit.Command.getUserConfig(jgit.Constants.REPOSITORIES_HOME + name);
//        jgit.Command.getUserConfig("/data/github/tend-sell/sell-web-automated-tests/");
//        jgit.Command.getCommitMessage("/data/github/tend-sell/sell-web-automated-tests/");
    }
}
