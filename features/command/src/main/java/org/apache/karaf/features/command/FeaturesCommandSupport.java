/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.features.command;

import java.util.EnumSet;

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.lifecycle.Reference;

public abstract class FeaturesCommandSupport implements Action {
    protected EnumSet<FeaturesService.Option> options = EnumSet.noneOf(FeaturesService.Option.class);  

    @Reference
    private FeaturesService featuresService;

    @Override
    public Object execute() throws Exception {
        if (featuresService == null) {
            throw new IllegalStateException("FeaturesService not found");
        }
        doExecute(featuresService);
        return null;
    }

    protected abstract void doExecute(FeaturesService admin) throws Exception;

    public void setFeaturesService(FeaturesService featuresService) {
        this.featuresService = featuresService;
    }
    
    protected void addOption(FeaturesService.Option option, boolean shouldAdd) {
        if (shouldAdd) {
            options.add(option);
        }
    }
    
    protected String getFeatureId(FeaturesService admin, String featureName) throws Exception {
        String[] parts = featureName.split("/");
        String name = parts.length > 0 ? parts[0] : featureName;
        String version = parts.length > 1 ? parts[1] : null;
        Feature[] matchingFeatures = admin.getFeatures(name, version);
        if (matchingFeatures.length == 0) {
            throw new IllegalArgumentException("No matching feature found for " + featureName);
        }
        if (matchingFeatures.length > 1) {
            throw new IllegalArgumentException("More than one matching feature found for " + featureName);
        }
        return matchingFeatures[0].getId();
    }
}
