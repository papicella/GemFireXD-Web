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

public class NewHDFSStore
{
    private String storeName;
    private String nameNode;
    private String homeDir;
    private String batchSize;
    private String batchTimeInterval;
    private String maxQueueMemory;
    private String minorCompact;
    private String maxInputFileSize;
    private String minInputFileCount;
    private String maxInputFileCount;
    private String minorCompactionThreads;
    private String majorCompact;
    private String majorCompactionInterval;
    private String majorCompactionThreads;
    private String maxWriteOnlyFileSize;
    private String writeOnlyRolloverInterval;
    private String additionalParams;

    public NewHDFSStore() {
    }

    public NewHDFSStore(String storeName, String nameNode, String homeDir, String batchSize, String batchTimeInterval, String maxQueueMemory, String minorCompact, String maxInputFileSize, String minInputFileCount, String maxInputFileCount, String minorCompactionThreads, String majorCompact, String majorCompactionInterval, String majorCompactionThreads, String maxWriteOnlyFileSize, String writeOnlyRolloverInterval, String additionalParams) {
        this.storeName = storeName;
        this.nameNode = nameNode;
        this.homeDir = homeDir;
        this.batchSize = batchSize;
        this.batchTimeInterval = batchTimeInterval;
        this.maxQueueMemory = maxQueueMemory;
        this.minorCompact = minorCompact;
        this.maxInputFileSize = maxInputFileSize;
        this.minInputFileCount = minInputFileCount;
        this.maxInputFileCount = maxInputFileCount;
        this.minorCompactionThreads = minorCompactionThreads;
        this.majorCompact = majorCompact;
        this.majorCompactionInterval = majorCompactionInterval;
        this.majorCompactionThreads = majorCompactionThreads;
        this.maxWriteOnlyFileSize = maxWriteOnlyFileSize;
        this.writeOnlyRolloverInterval = writeOnlyRolloverInterval;
        this.additionalParams = additionalParams;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getNameNode() {
        return nameNode;
    }

    public void setNameNode(String nameNode) {
        this.nameNode = nameNode;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getBatchTimeInterval() {
        return batchTimeInterval;
    }

    public void setBatchTimeInterval(String batchTimeInterval) {
        this.batchTimeInterval = batchTimeInterval;
    }

    public String getMaxQueueMemory() {
        return maxQueueMemory;
    }

    public void setMaxQueueMemory(String maxQueueMemory) {
        this.maxQueueMemory = maxQueueMemory;
    }

    public String getMinorCompact() {
        return minorCompact;
    }

    public void setMinorCompact(String minorCompact) {
        this.minorCompact = minorCompact;
    }

    public String getMaxInputFileSize() {
        return maxInputFileSize;
    }

    public void setMaxInputFileSize(String maxInputFileSize) {
        this.maxInputFileSize = maxInputFileSize;
    }

    public String getMinInputFileCount() {
        return minInputFileCount;
    }

    public void setMinInputFileCount(String minInputFileCount) {
        this.minInputFileCount = minInputFileCount;
    }

    public String getMaxInputFileCount() {
        return maxInputFileCount;
    }

    public void setMaxInputFileCount(String maxInputFileCount) {
        this.maxInputFileCount = maxInputFileCount;
    }

    public String getMinorCompactionThreads() {
        return minorCompactionThreads;
    }

    public void setMinorCompactionThreads(String minorCompactionThreads) {
        this.minorCompactionThreads = minorCompactionThreads;
    }

    public String getMajorCompact() {
        return majorCompact;
    }

    public void setMajorCompact(String majorCompact) {
        this.majorCompact = majorCompact;
    }

    public String getMajorCompactionInterval() {
        return majorCompactionInterval;
    }

    public void setMajorCompactionInterval(String majorCompactionInterval) {
        this.majorCompactionInterval = majorCompactionInterval;
    }

    public String getMajorCompactionThreads() {
        return majorCompactionThreads;
    }

    public void setMajorCompactionThreads(String majorCompactionThreads) {
        this.majorCompactionThreads = majorCompactionThreads;
    }

    public String getMaxWriteOnlyFileSize() {
        return maxWriteOnlyFileSize;
    }

    public void setMaxWriteOnlyFileSize(String maxWriteOnlyFileSize) {
        this.maxWriteOnlyFileSize = maxWriteOnlyFileSize;
    }

    public String getWriteOnlyRolloverInterval() {
        return writeOnlyRolloverInterval;
    }

    public void setWriteOnlyRolloverInterval(String writeOnlyRolloverInterval) {
        this.writeOnlyRolloverInterval = writeOnlyRolloverInterval;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    @Override
    public String toString() {
        return "NewHDFSStore{" +
                "storeName='" + storeName + '\'' +
                ", nameNode='" + nameNode + '\'' +
                ", homeDir='" + homeDir + '\'' +
                ", batchSize='" + batchSize + '\'' +
                ", batchTimeInterval='" + batchTimeInterval + '\'' +
                ", maxQueueMemory='" + maxQueueMemory + '\'' +
                ", minorCompact='" + minorCompact + '\'' +
                ", maxInputFileSize='" + maxInputFileSize + '\'' +
                ", minInputFileCount='" + minInputFileCount + '\'' +
                ", maxInputFileCount='" + maxInputFileCount + '\'' +
                ", minorCompactionThreads='" + minorCompactionThreads + '\'' +
                ", majorCompact='" + majorCompact + '\'' +
                ", majorCompactionInterval='" + majorCompactionInterval + '\'' +
                ", majorCompactionThreads='" + majorCompactionThreads + '\'' +
                ", maxWriteOnlyFileSize='" + maxWriteOnlyFileSize + '\'' +
                ", writeOnlyRolloverInterval='" + writeOnlyRolloverInterval + '\'' +
                ", additionalParams='" + additionalParams + '\'' +
                '}';
    }
}
