import React from "react";

import { FlexContainer } from "components/ui/Flex";
import { Switch } from "components/ui/Switch";

import { useCurrentWorkspace, useUpdateConnection } from "core/api";
import { ConnectionId, ConnectionStatus, SchemaChange } from "core/api/types/AirbyteClient";
import { useIntent } from "core/utils/rbac";
import { useAnalyticsTrackFunctions } from "hooks/services/ConnectionEdit/useAnalyticsTrackFunctions";

interface StateSwitchCellProps {
  connectionId: ConnectionId;
  enabled?: boolean;
  schemaChange?: SchemaChange;
}

export const StateSwitchCell: React.FC<StateSwitchCellProps> = ({ connectionId, enabled, schemaChange }) => {
  const { trackConnectionStatusUpdate } = useAnalyticsTrackFunctions();
  const { workspaceId } = useCurrentWorkspace();
  const canEditConnection = useIntent("EditConnection", { workspaceId });
  const { mutateAsync: updateConnection, isLoading } = useUpdateConnection();

  const onChange = async ({ target: { checked } }: React.ChangeEvent<HTMLInputElement>) => {
    const updatedConnection = await updateConnection({
      connectionId,
      status: checked ? ConnectionStatus.active : ConnectionStatus.inactive,
    });
    trackConnectionStatusUpdate(updatedConnection);
  };

  const isDisabled = schemaChange === SchemaChange.breaking || !canEditConnection || isLoading;

  return (
    <FlexContainer justifyContent="center">
      <Switch
        size="sm"
        checked={enabled}
        onChange={onChange}
        disabled={isDisabled}
        loading={isLoading}
        data-testid={`connection-state-switch-${connectionId}`}
      />
    </FlexContainer>
  );
};
