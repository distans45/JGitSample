package jgit;

import java.io.File;
import java.nio.file.Files;

import org.eclipse.jgit.api.Git;

/**
 * Created by annguyen on 10/14/16.
 */
public class JGitActionWrapper {

	/*
	 * Repository
	 */
	public static Git git;

	public static boolean checkoutBranch(String repoName, String branchName) {
		File f = new File(repoName);// TODO: update right path
		// Check if repo exist, if not clone new Repo
		if (!f.exists()) {
			git = JGitAction.cloneRepository(repoName);
		} else { // else access this repo
			git = JGitAction.accessRepository(repoName);
		}

		// Check if 
		if (git == null) {
			System.out.println("Git error");
			return false;
		}
		// Check out specific Branch
		JGitAction.checkoutBranch(git, branchName);
		return true;
	}
}
