import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Created by annguyen on 10/14/16.
 */
public class Util {
    public static Repository createRepository(String repoName) {
        try {
            Repository repository = FileRepositoryBuilder.create(new File(repoName, ".git"));
            repository.create();
            System.out.println(repoName + " created");
            return repository;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Repository openRepository(String path) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        File repo = new File(path + "/.git");
        try {
            Repository repository = builder.setGitDir(repo).readEnvironment().findGitDir().build();
            System.out.println("Having repository: " + repository.getDirectory());
            return repository;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void readUserConfig(String repoName) {
        Repository repository = openRepository(repoName);
        if (repository != null) {
            StoredConfig config = repository.getConfig();
            String name = config.getString("user", "", "name");
            String email = config.getString("user", "", "email");
            String url = config.getString("remote", "origin", "url");
            System.out.println("User configuration is: " + name + " - " + email
                    + ". \\nFrom:" + url);

        }
    }

    public static void addFile(String repo, String fileName) {
        Repository repository = openRepository(repo);
        File newFile = new File(repository.getDirectory().getParent() + "/" + fileName);
        try {
            if (newFile.createNewFile()) {
                Git git = new Git(repository);
                git.add().addFilepattern(fileName).call();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void getStatus(String repo) {
        Repository repository = openRepository(repo);
        try (Git git = new Git(repository)) {
            Status status = git.status().call();
            System.out.println("Added: " + status.getAdded());
            System.out.println("Changed: " + status.getChanged());
            System.out.println("Missing: " + status.getMissing());
            System.out.println("Modified: " + status.getModified());
            System.out.println("Untracked: " + status.getUntracked());
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void GetCommitMessage(String repoName) throws IOException {
        try (Repository repository = openRepository(repoName)) {
            Ref head = repository.findRef("refs/heads/master");
            System.out.println("Found head: " + head);
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(head.getObjectId());
                System.out.println("\nCommit-Message: " + commit.getFullMessage());
                walk.dispose();
            }
        }
    }
}
