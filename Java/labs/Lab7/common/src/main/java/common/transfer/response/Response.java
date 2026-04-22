package common.transfer.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import common.transfer.Status;

public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<T> body = new ArrayList<>();
    private Status status = Status.OK;
    
    private boolean chunked = false;
    private String streamId = null;
    private int chunkNumber = 0;
    private int totalChunks = 0;
    
    public Response() {}
    
    public Response(List<T> body) {
        this.body = body;
    }
    
    public Response(List<T> body, Status status) {
        this.body = body;
        this.status = status;
    }
    
    public Response(Status status) {
        this.status = status;
    }
    
    public static <T> List<Response<T>> split(List<T> data, int chunkSize) {
        List<Response<T>> chunks = new ArrayList<>();
        
        if (data == null || data.isEmpty()) {
            Response<T> emptyChunk = new Response<>(new ArrayList<>());
            emptyChunk.chunked = true;
            emptyChunk.streamId = UUID.randomUUID().toString();
            emptyChunk.chunkNumber = 1;
            emptyChunk.totalChunks = 1;
            chunks.add(emptyChunk);
            return chunks;
        }
        
        String streamId = UUID.randomUUID().toString();
        int totalChunks = (int) Math.ceil((double) data.size() / chunkSize);
        
        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, data.size());
            List<T> chunkData = new ArrayList<>(data.subList(start, end));
            
            Response<T> chunk = new Response<>(chunkData);
            chunk.chunked = true;
            chunk.streamId = streamId;
            chunk.chunkNumber = i + 1;
            chunk.totalChunks = totalChunks;
            
            chunks.add(chunk);
        }
        
        return chunks;
    }
    
    public List<T> getBody() { return body; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public void put(T element) { body.add(element); }
    public boolean isEmpty() { return body.isEmpty(); }
    
    public boolean isChunked() { return chunked; }
    public String getStreamId() { return streamId; }
    public int getChunkNumber() { return chunkNumber; }
    public int getTotalChunks() { return totalChunks; }
    
    @Override
    public String toString() {
        if (chunked) {
            return String.format("Response{chunk %d/%d, size=%d}", 
                chunkNumber, totalChunks, body.size());
        }
        return String.format("Response{status=%s, body=%s}", status, body);
    }
}