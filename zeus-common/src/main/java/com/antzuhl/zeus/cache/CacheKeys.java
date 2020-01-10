package com.antzuhl.zeus.cache;

public enum CacheKeys {

    SERVER_NAMESPACE("SERVER_NAMESPACE"),
    SERVER_NAMESPACE_ALL_SERVER("SERVER_NAMESPACE_ALL_SERVER_%s"),

    SERVER_ADDRESS("SERVER_ADDRESS_%s"),
    ;

    private String id;
    public String getId() {
        return id;
    }
    CacheKeys(String id) {
        this.setId(id);
    }
    public void setId(String id) {
        this.id = id;
    }


}
