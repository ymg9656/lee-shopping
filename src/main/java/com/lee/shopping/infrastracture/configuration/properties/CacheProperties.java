package com.lee.shopping.infrastracture.configuration.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CacheProperties {
    List<CacheSpec> caffeine;
    @Getter
    @Setter
    public static class CacheSpec{
        String name;
        String spec;

    }

}
