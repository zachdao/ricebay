import React, { useState } from 'react';
import { ActionButton, Flex, Grid, Text } from '@adobe/react-spectrum';
import VisibilityOff from '@spectrum-icons/workflow/VisibilityOff';
import Asterisk from '@spectrum-icons/workflow/Asterisk';
import Visibility from '@spectrum-icons/workflow/Visibility';

export const HiddenValue = ({ value }) => {
    const [hidden, setHidden] = useState(true);
    return (
        <Grid
            areas={['value button']}
            columns={['180px', 'auto']}
            alignItems="center"
        >
            {hidden ? (
                <Flex alignItems="center" gridArea="value">
                    <Asterisk />
                    <Asterisk />
                    <Asterisk />
                    <Asterisk />
                    <Asterisk />
                    <Asterisk />
                </Flex>
            ) : (
                <Text gridArea="value">{value}</Text>
            )}
            <ActionButton
                gridArea="button"
                isQuiet
                onPress={() => setHidden((prev) => !prev)}
            >
                {hidden ? <VisibilityOff /> : <Visibility />}
            </ActionButton>
        </Grid>
    );
};
