package com.schilings.neiko.store;


public enum AppendStatus {
    PUT_OK,
    END_OF_FILE,
    MESSAGE_SIZE_EXCEEDED,
    PROPERTIES_SIZE_EXCEEDED,
    UNKNOWN_ERROR,
}
