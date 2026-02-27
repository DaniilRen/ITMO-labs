import runtime.client.LocalRuntime;
import runtime.server.RemoteRuntime;
import util.LocalEnvironment;


public class Main {
    public static void main(String... args) {
        String filePath = LocalEnvironment.getCollectionPath();
        if (filePath == null) {
            System.exit(0);
        }

        RemoteRuntime remoteRuntime = new RemoteRuntime(filePath);
        LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);

        localRuntime.run("interactive");
    }
}
