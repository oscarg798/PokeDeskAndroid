package com.core.model.utils;

/**
 * Created by oscargallon on 7/13/16.
 */

public class CoupleParams {
    private final String key;
    private final String param;
    private final Object object;

    private CoupleParams(String key, String param, Object object) {
        this.key = key;
        this.param = param;
        this.object = object;
    }

    public String getKey() {
        return key;
    }


    public String getParam() {
        return param;
    }


    public Object getObject() {
        return object;
    }

    public static class CoupleParamBuilder {

        private String nestedKey;
        private String nestedParam;
        private Object nestedObject;

        public CoupleParamBuilder(String nestedKey) {
            this.nestedKey = nestedKey;
        }

        public CoupleParamBuilder nestedParam(String nestedParam) {
            this.nestedParam = nestedParam;
            return this;
        }

        public CoupleParamBuilder nestedObject(Object nestedObject) {
            this.nestedObject = nestedObject;
            return this;
        }

        public CoupleParams createCoupleParam() {
            return new CoupleParams(this.nestedKey,
                    this.nestedParam, this.nestedObject);
        }

    }
}
