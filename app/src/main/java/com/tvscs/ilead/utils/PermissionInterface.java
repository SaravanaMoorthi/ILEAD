package com.tvscs.ilead.utils;

import android.support.annotation.NonNull;

public interface PermissionInterface {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
