package tendsell;

import jgit.JGitAction;

public class Test {
	public static void main(String[] args) {
		String REMOTE_URL = "git@github.com:tendag/continuous-integration.git";
		String targetDir = System.getProperty("user.dir") + "/git/";
		System.out.println(targetDir);
		boolean cloneRepository = JGitAction.cloneRepository(REMOTE_URL, targetDir);
		System.out.println(cloneRepository);
	}
}
