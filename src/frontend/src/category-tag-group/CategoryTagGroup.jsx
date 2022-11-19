import React from 'react';
import { ActionButton, Badge, Flex, Text } from '@adobe/react-spectrum';
import Close from '@spectrum-icons/workflow/Close';

export const CategoryTagGroup = ({ categories, onRemove }) => {
    return (
        <Flex direction="row" alignItems="start" gap="size-100" maxWidth="100%">
            {Array.from(categories).map((category) => (
                <Badge variant="indigo" align>
                    <Flex
                        direction="row"
                        gap="size-100"
                        alignItems="center"
                        justifyContent="center"
                    >
                        <Text>{category}</Text>
                        {onRemove && (
                            <ActionButton
                                isQuiet
                                staticColor="white"
                                onPress={() => onRemove(category)}
                            >
                                <Close size="XS" />
                            </ActionButton>
                        )}
                    </Flex>
                </Badge>
            ))}
        </Flex>
    );
};
