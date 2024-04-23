package io.airbyte.server.apis.publicapi.mappers

import io.airbyte.api.model.generated.AirbyteCatalog
import io.airbyte.api.model.generated.ConnectionScheduleType
import io.airbyte.api.model.generated.ConnectionStatus
import io.airbyte.api.model.generated.ConnectionUpdate
import io.airbyte.api.model.generated.Geography
import io.airbyte.api.model.generated.NamespaceDefinitionType
import io.airbyte.api.model.generated.NonBreakingChangesPreference
import io.airbyte.public_api.model.generated.AirbyteApiConnectionSchedule
import io.airbyte.public_api.model.generated.ConnectionPatchRequest
import io.airbyte.public_api.model.generated.ConnectionStatusEnum
import io.airbyte.public_api.model.generated.GeographyEnumNoDefault
import io.airbyte.public_api.model.generated.NamespaceDefinitionEnumNoDefault
import io.airbyte.public_api.model.generated.NonBreakingSchemaUpdatesBehaviorEnumNoDefault
import io.airbyte.public_api.model.generated.ScheduleTypeEnum
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class ConnectionUpdateMapperTest {
  @Test
  fun testConnectionUpdateMapper() {
    val connectionId = UUID.randomUUID()
    val catalogId = UUID.randomUUID()

    val catalog =
      AirbyteCatalog().apply {
        this.streams = emptyList()
      }

    val connectionPatchRequest =
      ConnectionPatchRequest().apply {
        this.name = "test"
        this.nonBreakingSchemaUpdatesBehavior = NonBreakingSchemaUpdatesBehaviorEnumNoDefault.DISABLE_CONNECTION
        this.namespaceDefinition = NamespaceDefinitionEnumNoDefault.DESTINATION
        this.namespaceFormat = "test"
        this.prefix = "test"
        this.dataResidency = GeographyEnumNoDefault.US
        this.schedule =
          AirbyteApiConnectionSchedule().apply {
            this.scheduleType = ScheduleTypeEnum.CRON
            this.cronExpression = "0 0 0 0 0 0"
          }
        this.status = ConnectionStatusEnum.INACTIVE
      }

    val expectedOssConnectionUpdateRequest =
      ConnectionUpdate().apply {
        this.name = connectionPatchRequest.name
        this.nonBreakingChangesPreference = NonBreakingChangesPreference.DISABLE
        this.namespaceDefinition = NamespaceDefinitionType.DESTINATION
        this.namespaceFormat = "test"
        this.prefix = "test"
        this.geography = Geography.US
        this.scheduleType = ConnectionScheduleType.CRON
        this.sourceCatalogId = catalogId
        this.syncCatalog = catalog
        this.status = ConnectionStatus.INACTIVE
        val connectionScheduleDataCron =
          io.airbyte.api.model.generated.ConnectionScheduleDataCron().apply {
            this.cronExpression = "0 0 0 0 0 0"
            this.cronTimeZone = "UTC"
          }
        val connectionScheduleData =
          io.airbyte.api.model.generated.ConnectionScheduleData().apply {
            this.cron = connectionScheduleDataCron
          }
        this.scheduleData = connectionScheduleData
        this.connectionId = connectionId
      }
    Assertions.assertEquals(
      expectedOssConnectionUpdateRequest,
      ConnectionUpdateMapper.from(connectionId, connectionPatchRequest, catalogId, catalog),
    )
  }
}
