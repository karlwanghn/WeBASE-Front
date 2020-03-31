/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.webase.front.event;

import com.webank.webase.front.event.entity.NewBlockEventInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewBlockEventInfoRepository extends CrudRepository<NewBlockEventInfo, Long>,
		JpaSpecificationExecutor<NewBlockEventInfo> {

	NewBlockEventInfo findById(Long id);

	List<NewBlockEventInfo> findByAppId(String appId);

	List<NewBlockEventInfo> findByGroupId(int groupId);

	List<NewBlockEventInfo> findByQueueName(String queueName);

	List<NewBlockEventInfo> findByExchangeNameAndRoutingKey(String exchangeName, String routingKey);

	/**
	 * check unique by appId exchangeName queueName
	 */
	NewBlockEventInfo findByAppIdAndExchangeNameAndQueueName(
			String appId, String exchangeName, String queueName);
}
