package com.schilings.neiko.store;


import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;

public class DefaultStore {

    private MappedFileQueue mappedFileQueue;

    private ExecutorService executorService;
    
    
    public void appendInner(byte[] data){
        MappedFile mappedFile = mappedFileQueue.getLastMappedFile();
        AppendResult result = mappedFile.appendInner(data, (fileFromOffset, byteBuffer, maxBlank, data1) -> {
            //do nothing
            return null;
        });
        switch (result.getStatus()) {
            case PUT_OK -> {
                //do nothing
            }
            case END_OF_FILE -> flush();
            
            case MESSAGE_SIZE_EXCEEDED -> {
            }
            case PROPERTIES_SIZE_EXCEEDED -> {
            }
            case UNKNOWN_ERROR -> {
            }
            default -> {}
        }
    }


    public void flush() {
        executorService.submit(() -> {
            this.mappedFileQueue.commit(0);
            this.mappedFileQueue.flush(0);
        });
    }
}
