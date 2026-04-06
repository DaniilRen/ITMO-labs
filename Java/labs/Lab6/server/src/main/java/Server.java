import common.transfer.request.Request;
import common.transfer.response.Response;

public interface Server {
    void run();
    Response<?> proccessRequest(Request request);
}