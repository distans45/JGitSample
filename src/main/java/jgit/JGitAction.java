package jgit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;

/**
 * Created by annguyen on 10/14/16.
 */
public class JGitAction {

	/*
	 * Repository
	 */

	// private static Git git;
	public static Git cloneRepository(String REMOTE_URL, String targetDir) {
		CredentialsProvider credentialsProvider = Credentials.credentialsProvider;
		File path = new File(targetDir);
		CloneCommand cloneCommand = Git.cloneRepository();
		try {
			Git ret = cloneCommand.setURI(REMOTE_URL).setDirectory(path).setCredentialsProvider(credentialsProvider).call();
			return ret;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static Git cloneRepository(String repoName) {
		return cloneRepository(Constants.GIT_ENDPOINT + "/" + repoName, Constants.GIT_HOME + "/" + repoName);
	}

	public static Git accessRepository(String pathToRepo) {
		try {
			Git git = Git.open(new File(pathToRepo + "/.git"));
			return git;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Branch
	 */

	public static List<String> listBranches(Git git) {
		List<String> listBranchNames = new ArrayList<>();
		try {
			List<Ref> listBranches = git.branchList().call();
			for (Ref ref : listBranches) {
				listBranchNames.add(ref.getName());
			}
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return listBranchNames;
	}

	public static boolean checkoutBranch(Git git, String branchName) {
		try {
			CheckoutCommand checkoutCmd = git.checkout();
			checkoutCmd.setCreateBranch(true).setName(branchName)
					.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).setStartPoint("origin/" + branchName)
					.call();
			return true;
		} catch (GitAPIException e) {
			e.printStackTrace();
			return false;
		}
	}
}
