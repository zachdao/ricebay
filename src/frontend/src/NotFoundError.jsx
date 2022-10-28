import React from 'react';
import {
    Content,
    Flex,
    Heading,
    IllustratedMessage,
    Button,
} from '@adobe/react-spectrum';
import NotFound from '@spectrum-icons/illustrations/NotFound';
import { useNavigate } from 'react-router-dom';

export const NotFoundError = () => {
    const navigate = useNavigate();
    return (
        <Flex
            width="100vw"
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
                    onPress={() => navigate(-1)}
                >
                    Go Back
                </Button>
            </IllustratedMessage>
        </Flex>
    );
};
