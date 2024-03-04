/*
 * Copyright (c) 2020-2024 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.commons.temporal.scheduling;

import io.airbyte.config.ConnectorJobOutput;
import io.airbyte.config.StandardDiscoverCatalogInput;
import io.airbyte.persistence.job.models.IntegrationLauncherConfig;
import io.airbyte.persistence.job.models.JobRunConfig;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Run airbyte discover catalog in temporal.
 */
@WorkflowInterface
public interface DiscoverCatalogWorkflow {

  @WorkflowMethod
  ConnectorJobOutput run(JobRunConfig jobRunConfig,
                         IntegrationLauncherConfig launcherConfig,
                         StandardDiscoverCatalogInput config);

}
