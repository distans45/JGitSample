package jgit;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Created by annguyen on 26/10/16.
 */
public class Credentials {
    public static CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("token", "d9e4f0ffb8bfe16433def3d25d1ce170fd1488e6");
}
