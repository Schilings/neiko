package com.schilings.neiko.store.manage;

import com.schilings.neiko.store.AppendResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class PutStoreResult {

    private StoreStatus storeStatus;
    private AppendResult appendResult;
}
