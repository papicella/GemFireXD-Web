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
package pivotal.au.se.gemfirexdweb.dao.hdfsstores;

public class HdfsStore
{
    private String name;
    private String namenode;
    private String homedir;

    public HdfsStore()
    {
    }

    public HdfsStore(String name, String namenode, String homedir) {
        this.name = name;
        this.namenode = namenode;
        this.homedir = homedir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamenode() {
        return namenode;
    }

    public void setNamenode(String namenode) {
        this.namenode = namenode;
    }

    public String getHomedir() {
        return homedir;
    }

    public void setHomedir(String homedir) {
        this.homedir = homedir;
    }

    @Override
    public String toString() {
        return "HdfsStore{" +
                "name='" + name + '\'' +
                ", namenode='" + namenode + '\'' +
                ", homedir='" + homedir + '\'' +
                '}';
    }
}
