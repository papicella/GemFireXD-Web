/*
Copyright (C) MARCH-2014 Pivotal Software, Inc.

All rights reserved. This program and the accompanying materials
are made available under the terms of the under the Apache License,
Version 2.0 (the "License”); you may not use this file except in compliance
with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pivotal.au.se.gemfirexdweb.beans;

public class NewDiskStore
{
    private String diskStoreName;
    private String maxLogSize;
    private String autoCompact;
    private String allowForceCompaction;
    private String compactionThreshold;
    private String timeInterval;
    private String writeBufferSize;
    private String additionalParams;

    public NewDiskStore() {
    }

    public NewDiskStore(String diskStoreName, String maxLogSize, String autoCompact, String allowForceCompaction, String compactionThreshold, String timeInterval, String writeBufferSize, String additionalParams) {
        this.diskStoreName = diskStoreName;
        this.maxLogSize = maxLogSize;
        this.autoCompact = autoCompact;
        this.allowForceCompaction = allowForceCompaction;
        this.compactionThreshold = compactionThreshold;
        this.timeInterval = timeInterval;
        this.writeBufferSize = writeBufferSize;
        this.additionalParams = additionalParams;
    }

    public String getDiskStoreName() {
        return diskStoreName;
    }

    public void setDiskStoreName(String diskStoreName) {
        this.diskStoreName = diskStoreName;
    }

    public String getMaxLogSize() {
        return maxLogSize;
    }

    public void setMaxLogSize(String maxLogSize) {
        this.maxLogSize = maxLogSize;
    }

    public String getAutoCompact() {
        return autoCompact;
    }

    public void setAutoCompact(String autoCompact) {
        this.autoCompact = autoCompact;
    }

    public String getAllowForceCompaction() {
        return allowForceCompaction;
    }

    public void setAllowForceCompaction(String allowForceCompaction) {
        this.allowForceCompaction = allowForceCompaction;
    }

    public String getCompactionThreshold() {
        return compactionThreshold;
    }

    public void setCompactionThreshold(String compactionThreshold) {
        this.compactionThreshold = compactionThreshold;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getWriteBufferSize() {
        return writeBufferSize;
    }

    public void setWriteBufferSize(String writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    @Override
    public String toString() {
        return "NewDiskStore{" +
                "diskStoreName='" + diskStoreName + '\'' +
                ", maxLogSize='" + maxLogSize + '\'' +
                ", autoCompact='" + autoCompact + '\'' +
                ", allowForceCompaction='" + allowForceCompaction + '\'' +
                ", compactionThreshold='" + compactionThreshold + '\'' +
                ", timeInterval='" + timeInterval + '\'' +
                ", writeBufferSize='" + writeBufferSize + '\'' +
                ", additionalParams='" + additionalParams + '\'' +
                '}';
    }
}
