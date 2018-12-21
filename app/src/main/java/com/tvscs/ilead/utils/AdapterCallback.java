package com.tvscs.ilead.utils;

import com.tvscs.ilead.model.ProductResponse;

public interface AdapterCallback {
    void onClickCallback(int pos, ProductResponse itemModel);
}
