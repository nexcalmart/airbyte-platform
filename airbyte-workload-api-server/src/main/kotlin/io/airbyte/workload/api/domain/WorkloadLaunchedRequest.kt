package io.airbyte.workload.api.domain

import io.swagger.v3.oas.annotations.media.Schema
import java.time.OffsetDateTime

class WorkloadLaunchedRequest(
  @Schema(required = true)
  var workloadId: String = "",
  var deadline: OffsetDateTime? = null,
)
