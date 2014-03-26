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

public class NewGatewayReceiver
{
    private String gatewayReceiverName;
    private String bindAddress;
    private String startPort;
    private String endPort;
    private String socketBufferSize;
    private String maxTimeBetweenPings;
    private String serverGroups;

    public NewGatewayReceiver() {
    }

    public NewGatewayReceiver(String gatewayReceiverName, String bindAddress, String startPort, String endPort, String socketBufferSize, String maxTimeBetweenPings, String serverGroups) {
        this.gatewayReceiverName = gatewayReceiverName;
        this.bindAddress = bindAddress;
        this.startPort = startPort;
        this.endPort = endPort;
        this.socketBufferSize = socketBufferSize;
        this.maxTimeBetweenPings = maxTimeBetweenPings;
        this.serverGroups = serverGroups;
    }

    public String getGatewayReceiverName() {
        return gatewayReceiverName;
    }

    public void setGatewayReceiverName(String gatewayReceiverName) {
        this.gatewayReceiverName = gatewayReceiverName;
    }

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
    }

    public String getStartPort() {
        return startPort;
    }

    public void setStartPort(String startPort) {
        this.startPort = startPort;
    }

    public String getEndPort() {
        return endPort;
    }

    public void setEndPort(String endPort) {
        this.endPort = endPort;
    }

    public String getSocketBufferSize() {
        return socketBufferSize;
    }

    public void setSocketBufferSize(String socketBufferSize) {
        this.socketBufferSize = socketBufferSize;
    }

    public String getMaxTimeBetweenPings() {
        return maxTimeBetweenPings;
    }

    public void setMaxTimeBetweenPings(String maxTimeBetweenPings) {
        this.maxTimeBetweenPings = maxTimeBetweenPings;
    }

    public String getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(String serverGroups) {
        this.serverGroups = serverGroups;
    }

    @Override
    public String toString() {
        return "NewGatewayReceiver{" +
                "gatewayReceiverName='" + gatewayReceiverName + '\'' +
                ", bindAddress='" + bindAddress + '\'' +
                ", startPort='" + startPort + '\'' +
                ", endPort='" + endPort + '\'' +
                ", socketBufferSize='" + socketBufferSize + '\'' +
                ", maxTimeBetweenPings='" + maxTimeBetweenPings + '\'' +
                ", serverGroups='" + serverGroups + '\'' +
                '}';
    }
}
