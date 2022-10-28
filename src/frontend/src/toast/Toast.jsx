import React from 'react';
import {
    ActionButton,
    Button,
    Flex,
    Grid,
    Text,
    View,
} from '@adobe/react-spectrum';
import Close from '@spectrum-icons/workflow/Close';
import styled from 'styled-components';
import Alert from '@spectrum-icons/workflow/Alert';

export const Toast = ({
    message,
    type = 'informative',
    actionTitle,
    actionFn,
    dismissFn,
}) => {
    return (
        <View
            backgroundColor={type}
            padding="size-300"
            position="absolute"
            bottom="size-400"
            borderRadius="regular"
        >
            <Grid
                areas={['message action dismiss']}
                columns={['3fr', '1fr', '1fr']}
                alignItems="center"
            >
                <Message>
                    {type === 'negative' && <Alert />}
                    <Text marginStart="10px" marginEnd="5px">
                        {message}
                    </Text>
                </Message>
                {actionTitle && actionFn && (
                    <Button
                        marginEnd="10px"
                        variant="overBackground"
                        onPress={actionFn}
                        gridArea="action"
                    >
                        {actionTitle}
                    </Button>
                )}
                <View
                    gridArea="dismiss"
                    borderStartWidth="thick"
                    borderStartColor={type}
                >
                    <Flex justifyContent="center">
                        <ActionButton
                            staticColor="white"
                            isQuiet
                            onPress={dismissFn}
                        >
                            <Close />
                        </ActionButton>
                    </Flex>
                </View>
            </Grid>
        </View>
    );
};

const Message = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: white;
    font-size: 120%;
    grid-area: message;
`;
