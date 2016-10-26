
import jgit.Constants;
import jgit.JGitFunctions;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;

/**
 * Created by annguyen on 10/14/16.
 */
public class Test {

    public static void main(String[] args) throws IOException {
//        jgit.JGitFunctions.initRepository(String.valueOf(currentTimeMillis()));
        Repository repoAT = JGitFunctions.accessRepository(Constants.GIT_HOME, "CI-0.1");

        File newFile = new File(Constants.GIT_HOME + "hihi");

        Status status = JGitFunctions.getStatus(repoAT);
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
        System.out.println(strStatus);

        JGitFunctions.getUserConfig(repoAT);
        RevCommit revCommit = JGitFunctions.getCommitMessage(repoAT);
        System.out.println("\nCommit-Message: " + revCommit.getFullMessage());

//        jgit.JGitFunctions.getUserConfig(jgit.Constants.REPOSITORIES_HOME + name);
//        jgit.JGitFunctions.getUserConfig("/data/github/tend-sell/sell-web-automated-tests/");
//        jgit.JGitFunctions.getCommitMessage("/data/github/tend-sell/sell-web-automated-tests/");
    }
}
