package com.schilings.neiko.store.manage;

public enum StoreStatus {
    PUT_OK,
    FLUSH_DISK_TIMEOUT,
    SERVICE_NOT_AVAILABLE,
    CREATE_MAPEDFILE_FAILED,
    MESSAGE_ILLEGAL,
    PROPERTIES_SIZE_EXCEEDED,
    OS_PAGECACHE_BUSY,
    UNKNOWN_ERROR,
}
