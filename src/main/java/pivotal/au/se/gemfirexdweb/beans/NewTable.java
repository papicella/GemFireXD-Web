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

public class NewTable
{
    private String schemaName;
    private String tableName;
    private String diskStore;
    private String persistant;
    private String persistenceType;
    private String hdfsStore;
    private String writeonly;
    private String evictionbycriteria;
    private String evictionfrequency;
    private String evictincoming;
    private String dataPolicy;
    private String serverGroups;
    private String partitionBy;
    private String colocateWith;
    private String redundancy;
    private String gatewaysender;
    private String asynceventlistener;
    private String offheap;
    private String other;

    public NewTable()
    {
    }

    public NewTable(String schemaName, String tableName, String diskStore, String persistant, String persistenceType, String hdfsStore, String writeonly, String evictionbycriteria, String evictionfrequency, String evictincoming, String dataPolicy, String serverGroups, String partitionBy, String colocateWith, String redundancy, String gatewaysender, String asynceventlistener, String offheap, String other) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.diskStore = diskStore;
        this.persistant = persistant;
        this.persistenceType = persistenceType;
        this.hdfsStore = hdfsStore;
        this.writeonly = writeonly;
        this.evictionbycriteria = evictionbycriteria;
        this.evictionfrequency = evictionfrequency;
        this.evictincoming = evictincoming;
        this.dataPolicy = dataPolicy;
        this.serverGroups = serverGroups;
        this.partitionBy = partitionBy;
        this.colocateWith = colocateWith;
        this.redundancy = redundancy;
        this.gatewaysender = gatewaysender;
        this.asynceventlistener = asynceventlistener;
        this.offheap = offheap;
        this.other = other;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDiskStore() {
        return diskStore;
    }

    public void setDiskStore(String diskStore) {
        this.diskStore = diskStore;
    }

    public String getPersistant() {
        return persistant;
    }

    public void setPersistant(String persistant) {
        this.persistant = persistant;
    }

    public String getPersistenceType() {
        return persistenceType;
    }

    public void setPersistenceType(String persistenceType) {
        this.persistenceType = persistenceType;
    }

    public String getHdfsStore() {
        return hdfsStore;
    }

    public void setHdfsStore(String hdfsStore) {
        this.hdfsStore = hdfsStore;
    }

    public String getWriteonly() {
        return writeonly;
    }

    public void setWriteonly(String writeonly) {
        this.writeonly = writeonly;
    }

    public String getEvictionbycriteria() {
        return evictionbycriteria;
    }

    public void setEvictionbycriteria(String evictionbycriteria) {
        this.evictionbycriteria = evictionbycriteria;
    }

    public String getEvictionfrequency() {
        return evictionfrequency;
    }

    public void setEvictionfrequency(String evictionfrequency) {
        this.evictionfrequency = evictionfrequency;
    }

    public String getEvictincoming() {
        return evictincoming;
    }

    public void setEvictincoming(String evictincoming) {
        this.evictincoming = evictincoming;
    }

    public String getDataPolicy() {
        return dataPolicy;
    }

    public void setDataPolicy(String dataPolicy) {
        this.dataPolicy = dataPolicy;
    }

    public String getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(String serverGroups) {
        this.serverGroups = serverGroups;
    }

    public String getPartitionBy() {
        return partitionBy;
    }

    public void setPartitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
    }

    public String getColocateWith() {
        return colocateWith;
    }

    public void setColocateWith(String colocateWith) {
        this.colocateWith = colocateWith;
    }

    public String getRedundancy() {
        return redundancy;
    }

    public void setRedundancy(String redundancy) {
        this.redundancy = redundancy;
    }

    public String getGatewaysender() {
        return gatewaysender;
    }

    public void setGatewaysender(String gatewaysender) {
        this.gatewaysender = gatewaysender;
    }

    public String getAsynceventlistener() {
        return asynceventlistener;
    }

    public void setAsynceventlistener(String asynceventlistener) {
        this.asynceventlistener = asynceventlistener;
    }

    public String getOffheap() {
        return offheap;
    }

    public void setOffheap(String offheap) {
        this.offheap = offheap;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "NewTable{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", diskStore='" + diskStore + '\'' +
                ", persistant='" + persistant + '\'' +
                ", persistenceType='" + persistenceType + '\'' +
                ", hdfsStore='" + hdfsStore + '\'' +
                ", writeonly='" + writeonly + '\'' +
                ", evictionbycriteria='" + evictionbycriteria + '\'' +
                ", evictionfrequency='" + evictionfrequency + '\'' +
                ", evictincoming='" + evictincoming + '\'' +
                ", dataPolicy='" + dataPolicy + '\'' +
                ", serverGroups='" + serverGroups + '\'' +
                ", partitionBy='" + partitionBy + '\'' +
                ", colocateWith='" + colocateWith + '\'' +
                ", redundancy='" + redundancy + '\'' +
                ", gatewaysender='" + gatewaysender + '\'' +
                ", asynceventlistener='" + asynceventlistener + '\'' +
                ", offheap='" + offheap + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
