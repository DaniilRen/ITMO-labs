package common.transfer.request.standart;

import common.transfer.request.Request;


public class NextChunkRequest extends Request {
    private static final long serialVersionUID = 7815544L;
    
    private final String streamId;
    private final int chunkNumber;
    
    public NextChunkRequest(String streamId, int chunkNumber) {
        this.streamId = streamId;
        this.chunkNumber = chunkNumber;
    }
    
    public String getStreamId() { return streamId; }
    public int getChunkNumber() { return chunkNumber; }
}