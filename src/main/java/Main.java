import java.io.IOException;

/**
 * Created by annguyen on 10/14/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String repoPath = Constants.REPOSITORIES_HOME + String.valueOf(System.currentTimeMillis() + "/");
        Util.createRepository(repoPath);
        Util.openRepository(repoPath);
        Util.addFile(repoPath, "hihi");
        Util.getStatus(repoPath);
//        Util.readUserConfig(Constants.REPOSITORIES_HOME + name);
//        Util.readUserConfig("/data/github/tend-sell/sell-web-automated-tests/");
//        Util.GetCommitMessage("/data/github/tend-sell/sell-web-automated-tests/");
    }
}
