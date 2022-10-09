import React from 'react';
import {
    Content,
    Flex,
    Heading,
    IllustratedMessage,
    Button,
} from '@adobe/react-spectrum';
import NotFound from '@spectrum-icons/illustrations/NotFound';

export const NotFoundError = () => {
    return (
        <Flex
            width="100vh"
            height="100vh"
            alignItems="center"
            justifyContent="center"
        >
            <IllustratedMessage>
                <NotFound />
                <Heading>Error 404: Page not found</Heading>
                <Content>
                    This page isn't available. Try checking the URL or visit a
                    different page.
                </Content>
                <Button
                    marginTop="size-100"
                    variant="secondary"
                    onPress={() => window.location.reload()}
                >
                    Reload
                </Button>
            </IllustratedMessage>
        </Flex>
    );
};
