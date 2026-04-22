package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.transfer.Status;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;

public class ChunkUtil {
	private static Map<String, List<Response<?>>> chunkStorage = new HashMap<>();
	private static final int MAX_CHUNK_SIZE_ITEMS = 1000;
	private static final int MAX_CHUNK_SIZE_BYTES = 1024 * 64;

	public static boolean shouldChunkify(Response<?> response) {
			List<?> body = response.getBody();
			if (body == null || body.isEmpty()) {
					return false;
			}
			
			if (body.size() <= MAX_CHUNK_SIZE_ITEMS) {
					return false;
			}
			
			long estimatedSize = getTotalSize(body);
			return estimatedSize > MAX_CHUNK_SIZE_BYTES;
	}

	@SuppressWarnings("unchecked")
	public static Response<?> chunkify(Response<?> response) {
		List<Object> body = (List<Object>) response.getBody();
		
		int dynamicChunkSize = calculateChunkSize(body);
		
		List<Response<Object>> chunks = Response.split(body, dynamicChunkSize);
		
		if (chunks.isEmpty()) {
				return response;
		}
		
		String streamId = chunks.get(0).getStreamId();
		chunkStorage.put(streamId, new ArrayList<>(chunks));
		
		return chunks.get(0);
	}

	private static int calculateChunkSize(List<?> body) {
		long totalSize = getTotalSize(body);
		if (totalSize <= 0) {
				return MAX_CHUNK_SIZE_ITEMS;
		}
		
		int targetChunks = Math.max(2, (int) (totalSize / MAX_CHUNK_SIZE_BYTES) + 1);
		int chunkSize = Math.max(1, body.size() / targetChunks);
		
		return Math.min(chunkSize, MAX_CHUNK_SIZE_ITEMS);
	}

	private static long getTotalSize(List<?> list) {
		if (list == null || list.isEmpty()) {
				return 0;
		}
		try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(list);
				oos.close();
				return baos.size();
		} catch (IOException e) {
				return list.size() * 500;
		}
	}


	public static Response<?> handleNextChunk(NextChunkRequest request) {
		String streamId = request.getStreamId();
		int chunkNumber = request.getChunkNumber();
		List<Response<?>> chunks = chunkStorage.get(streamId);
		if (chunks == null) {
				return new Response<>(List.of("Invalid chunk stream"), Status.ERROR);
		}
		
		if (chunkNumber < 1 || chunkNumber > chunks.size()) {
				return new Response<>(List.of("Invalid chunk number"), Status.ERROR);
		}
		
		Response<?> chunk = chunks.get(chunkNumber - 1);
		
		if (chunkNumber == chunks.size()) {
				chunkStorage.remove(streamId);
		}
		
		return chunk;
	}

}
