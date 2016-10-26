package jgit;

import org.eclipse.jgit.api.*;
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
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Created by annguyen on 10/14/16.
 */
public class JGitFunctions {

    /*
    * Repository
    * */

    public static Repository accessRepository(String path, String repoName) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            File repo = new File(path + repoName + "/.git");
            Repository repository = builder.setGitDir(repo).readEnvironment().findGitDir().build();
            return repository;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean initRepository(String path, String repoName) {
        try {
            File repo = new File(path + repoName + "/.git");
            InitCommand initCmd = Git.init();
            initCmd.setDirectory(repo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean clonePublicRepository(String REMOTE_URL, String path) {
        File targetDir = new File(path);
        CloneCommand cloneCommand = Git.cloneRepository();
        try {
            cloneCommand.setURI(REMOTE_URL).
                    setDirectory(targetDir)
                    .call();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void listRepository(String REMOTE_URL) {
        LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository();
        try {
            Collection<Ref> refs = lsRemoteCommand.setHeads(true)
                    .setTags(true)
                    .setRemote(REMOTE_URL)
                    .call();
            for (Ref ref : refs) {
                System.out.println("Ref: " + ref);
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Iterable<RevCommit> getBranchLog(Repository repository, String branch) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> log = git.log().add(repository.resolve(branch)).call();
            return log;
        } catch (GitAPIException | IncorrectObjectTypeException | MissingObjectException | AmbiguousObjectException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Iterable<RevCommit> getFileLog(Repository repository, String fileName) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> log = git.log().addPath(fileName).call();
            return log;
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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
            System.out.println("User configuration is: " + name + " - " + email
                    + ". \\nFrom:" + url);

        }
    }

    public static Status getStatus(Repository repository) {
        try (Git git = new Git(repository)) {
            Status status = git.status().call();
            return status;
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }


    /*
    * Commit
    * */
    public static RevCommit getCommitMessage(Repository repository) throws IOException {
        Ref head = repository.findRef(Constants.HEAD_MASTER);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            walk.dispose();
            return commit;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void commitFile(Repository repository, File file, String message) {
        try {
            Git git = new Git(repository);
            git.add().addFilepattern(file.getName()).call();
            git.commit().setMessage(message).call();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void commitAll(Repository repository, String message) {
        try {
            Git git = new Git(repository);
            git.add().addFilepattern(".").call();
            git.commit().setMessage(message).call();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    /*
    * Branch
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

    public static void createBranch(Repository repository, String branchName) {
        Git git = new Git(repository);
        try {
            git.branchCreate().setName(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBranch(Repository repository, String branchName) {
        Git git = new Git(repository);
        try {
            git.branchDelete().setBranchNames(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkoutBranch(Repository repository, String branchName) {
        Git git = new Git(repository);
        try {
            CheckoutCommand checkoutCmd = git.checkout();
            Ref ref = checkoutCmd.setCreateBranch(true)
                    .setName(branchName)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                    .setStartPoint("origin/" + branchName)
                    .call();
            System.out.println(ref.getName());
            return true;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static boolean pushToRemote(Repository repository, String REMOTE_URL) {

        Git git = new Git(repository);
        CredentialsProvider credentialsProvider = Credentials.credentialsProvider;
        try {
            PushCommand pushCmd = git.push();
            pushCmd.setRemote(REMOTE_URL).setCredentialsProvider(credentialsProvider).call();
            return true;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return false;
        }
    }
}
