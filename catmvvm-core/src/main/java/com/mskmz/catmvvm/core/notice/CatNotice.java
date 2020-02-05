package com.mskmz.catmvvm.core.notice;

import java.util.HashMap;

public class CatNotice extends HashMap<Integer, Object> {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        CatNotice catNotice;

        Builder() {
            catNotice = new CatNotice();
        }

        public Builder put(Integer key, Object obj) {
            catNotice.put(key, obj);
            return this;
        }

        public Builder remove(Integer key) {
            catNotice.remove(key);
            return this;
        }

        public CatNotice build() {
            return catNotice;
        }
    }
}
