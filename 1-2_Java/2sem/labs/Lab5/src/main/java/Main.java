import runtime.LocalRuntime;
import runtime.RemoteRuntime;
import util.LocalEnvironment;
import util.controller.DefaultRecursionController;
import util.exceptions.RuntimeInitException;


public class Main {
    public static void main(String... args) {
        String filePath = LocalEnvironment.getCollectionPath();
        if (filePath == null) {
            System.exit(0);
        }

        try {
            RemoteRuntime remoteRuntime = new RemoteRuntime(filePath); 
            LocalRuntime localRuntime = new LocalRuntime(remoteRuntime, new DefaultRecursionController());
            localRuntime.run("interactive");  
        } catch (RuntimeInitException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
