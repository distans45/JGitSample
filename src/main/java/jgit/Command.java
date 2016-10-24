package jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by annguyen on 10/14/16.
 */
public class Command {

    final static Logger logger = Logger.getLogger(Command.class);

    /*
    * Repository - Read
    * */

    public static Repository openRepository(String path, String repoName) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            File repo = new File(path + repoName + "/.git");
            Repository repository = builder.setGitDir(repo).readEnvironment().findGitDir().build();
            return repository;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Repository openRepository(String repoName) {
        return openRepository(Constants.GIT_HOME_SAMPLE, repoName);
    }

    /*
    * Repository - Write
    * */
    public static Repository createRepository(String path, String repoName) {
        try {
            Repository repository = FileRepositoryBuilder.create(new File(path + "/" + repoName, ".git"));
            repository.create();
            logger.info(repoName + " created");
            return repository;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Repository createRepository(String repoName) {
        return createRepository(Constants.GIT_HOME_SAMPLE, repoName);
    }


    /*
    * Log - Read
    * */
    public static Iterable<RevCommit> getAllLog(Repository repository) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> log = git.log().all().call();
            return log;
        } catch (GitAPIException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Iterable<RevCommit> getCurrentBranchLog(Repository repository) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> log = git.log().call();
            return log;
        } catch (GitAPIException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Iterable<RevCommit> getBranchLog(Repository repository, String branch) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> log = git.log().add(repository.resolve(branch)).call();
            return log;
        } catch (GitAPIException | IncorrectObjectTypeException | MissingObjectException | AmbiguousObjectException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Iterable<RevCommit> getFileLog(Repository repository, String fileName) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> log = git.log().addPath(fileName).call();
            return log;
        } catch (GitAPIException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    /*
    * Config - Read
    * */
    public static void getUserConfig(Repository repository) {
        if (repository != null) {
            StoredConfig config = repository.getConfig();
            String name = config.getString("user", "", "name");
            String email = config.getString("user", "", "email");
            String url = config.getString("remote", "origin", "url");
            logger.info("User configuration is: " + name + " - " + email
                    + ". \\nFrom:" + url);

        }
    }

    public static Status getStatus(Repository repository) {
        try (Git git = new Git(repository)) {
            Status status = git.status().call();
            return status;
        } catch (GitAPIException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /*
    * Commit - Read
    * */
    public static RevCommit getCommitMessage(Repository repository) throws IOException {
        Ref head = repository.findRef("refs/heads/master");
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            walk.dispose();
            return commit;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /*
    * Commit - Write
    * */
    public static void commitFile(Repository repository, File file, String message) {
        try {
            Git git = new Git(repository);
            git.add().addFilepattern(file.getName()).call();
            git.commit().setMessage(message).call();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (GitAPIException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    public static void commitAll(Repository repository, String message) {
        try {
            Git git = new Git(repository);
            git.add().addFilepattern(".").call();
            git.commit().setMessage(message).call();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (GitAPIException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }


    /*
    * Branch - Read
    * */
    public static List<Ref> listBranches(Repository repository) {
        Git git = new Git(repository);
        List<Ref> listBranches = null;
        try {
            listBranches = git.branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return listBranches;

    }

    /*
    * Branch - Write
    * */
    public static void createBranch(Repository repository, String branchName) {
        Git git = new Git(repository);
        try {
            git.branchCreate().setName(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
