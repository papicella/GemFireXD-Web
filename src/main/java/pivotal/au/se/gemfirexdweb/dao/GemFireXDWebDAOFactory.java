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
package pivotal.au.se.gemfirexdweb.dao;

import pivotal.au.se.gemfirexdweb.dao.asyncevent.AsynceventDAO;
import pivotal.au.se.gemfirexdweb.dao.asyncevent.AsynceventDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.constraints.ConstraintDAO;
import pivotal.au.se.gemfirexdweb.dao.constraints.ConstraintDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStoreDAO;
import pivotal.au.se.gemfirexdweb.dao.diskstores.DiskStoreDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.gatewayrecievers.GatewayReceiverDAO;
import pivotal.au.se.gemfirexdweb.dao.gatewayrecievers.GatewayReceiverDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.gatewaysenders.GatewaySenderDAO;
import pivotal.au.se.gemfirexdweb.dao.gatewaysenders.GatewaySenderDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.hdfsstores.HdfsStoreDAO;
import pivotal.au.se.gemfirexdweb.dao.hdfsstores.HdfsStoreDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.indexes.IndexDAO;
import pivotal.au.se.gemfirexdweb.dao.indexes.IndexDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.jars.JarDAO;
import pivotal.au.se.gemfirexdweb.dao.jars.JarDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.members.MemberDAO;
import pivotal.au.se.gemfirexdweb.dao.members.MemberDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.stored.StoredProcDAO;
import pivotal.au.se.gemfirexdweb.dao.stored.StoredProcDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAO;
import pivotal.au.se.gemfirexdweb.dao.tables.TableDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.triggers.TriggerDAO;
import pivotal.au.se.gemfirexdweb.dao.triggers.TriggerDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.types.TypeDAO;
import pivotal.au.se.gemfirexdweb.dao.types.TypeDAOImpl;
import pivotal.au.se.gemfirexdweb.dao.views.ViewDAO;
import pivotal.au.se.gemfirexdweb.dao.views.ViewDAOImpl;

public class GemFireXDWebDAOFactory
{
	 public static TableDAO getTableDAO()
	 {
		 return new TableDAOImpl();
	 }

	 public static ViewDAO getViewDAO()
	 {
		 return new ViewDAOImpl();
	 }

	 public static ConstraintDAO getConstraintDAO()
	 {
		 return new ConstraintDAOImpl();
	 }
	 
	 public static IndexDAO getIndexDAO()
	 {
		 return new IndexDAOImpl();
	 }

	 public static TriggerDAO getTriggerDAO()
	 {
		 return new TriggerDAOImpl();
	 }
	 
	 public static AsynceventDAO getAsynceventDAO()
	 {
		 return new AsynceventDAOImpl();
	 }
	 
	 public static StoredProcDAO getStoredProcDAO()
	 {
		 return new StoredProcDAOImpl();
	 }

	 public static GatewaySenderDAO getGatewaySenderDAO()
	 {
		 return new GatewaySenderDAOImpl();
	 }
	 
	 public static GatewayReceiverDAO getGatewayRecieverDAO()
	 {
		 return new GatewayReceiverDAOImpl();
	 }
	 
	 public static TypeDAO getTypeDAO()
	 {
		 return new TypeDAOImpl();
	 }

	 public static DiskStoreDAO getDiskStoreDAO()
	 {
		 return new DiskStoreDAOImpl();
	 }
	 
	 public static MemberDAO getMemberDAO()
	 {
		 return new MemberDAOImpl();
	 }

     public static HdfsStoreDAO getHdfsStoreDAO()
     {
        return new HdfsStoreDAOImpl();
     }

     public static JarDAO getJarStoreDAO()
    {
        return new JarDAOImpl();
    }

}
