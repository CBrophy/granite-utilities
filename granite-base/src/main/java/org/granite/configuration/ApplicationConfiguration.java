/*
 * Copyright (C) 2016 Charles Brophy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.granite.configuration;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ApplicationConfiguration implements Serializable {

    private final Map<String, String> configMap;

    ApplicationConfiguration(final Map<String, String> configMap){
        this.configMap = checkNotNull(configMap, "configMap");
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public long getInt(final String configKey){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        checkNotNull(configValueString ,"Required config key %s not present", configKey);

        return Integer.valueOf(configValueString);
    }

    public int getInt(final String configKey, final int defaultValue){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        return configValueString == null ? defaultValue : Integer.valueOf(configValueString);
    }

    public long getLong(final String configKey){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        checkNotNull(configValueString ,"Required config key %s not present", configKey);

        return Long.valueOf(configValueString);
    }

    public long getLong(final String configKey, final long defaultValue){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        return configValueString == null ? defaultValue : Long.valueOf(configValueString);
    }

    public double getDouble(final String configKey){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        checkNotNull(configValueString ,"Required config key %s not present", configKey);

        return Double.valueOf(configValueString);
    }

    public double getDouble(final String configKey, final double defaultValue){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        return configValueString == null ? defaultValue : Double.valueOf(configValueString);
    }


    public String getString(final String configKey){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        checkNotNull(configValueString ,"Required config key %s not present", configKey);

        return configValueString;
    }

    public String getString(final String configKey, final String defaultValue){
        checkNotNull(configKey, "configKey");

        final String configValueString = configMap.get(configKey);

        return configValueString == null ? defaultValue : configValueString;
    }

    public void printConfig(){
        if(configMap.isEmpty()){
            Logger.getGlobal().info("No configuration loaded");
        }

        configMap
                .keySet()
                .forEach(key -> Logger.getGlobal().info(String.format("%s = %s", key, configMap.get(key))));
    }

}
