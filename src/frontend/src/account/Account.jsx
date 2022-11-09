import React, { useEffect } from 'react';
import { Flex, Grid, Heading } from '@adobe/react-spectrum';
import styled from 'styled-components';
import { useHttpQuery } from '../http-query/use-http-query';
import ContentLoader from 'react-content-loader';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';
import { AccountDetail } from './account-detail/AccountDetail';
import { Route, Routes } from 'react-router-dom';

export const Account = () => {
    // Get the current user from the API (AppResponse<Account>)
    const { appResponse, error, status } = useHttpQuery('/accounts/me');

    // Check for errors and raise a toast to the user if we have one
    useEffect(() => {
        if (
            error ||
            (status && status !== 200) ||
            (appResponse && !appResponse.success)
        ) {
            toast.custom((t) => (
                <Toast
                    message="Failed to load account details, try again later"
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [appResponse, error, status]);

    // Render the account views
    return (
        <Grid areas={['form  image']} columns={['2fr', '1fr']} height="100%">
            <Flex
                gridArea="form"
                alignItems="center"
                justifyContent="center"
                direction="column"
            >
                <Heading level="1">Account Details</Heading>
                <Card>
                    {appResponse && appResponse.success ? (
                        <Routes>
                            <Route
                                index
                                element={
                                    <AccountDetail account={appResponse.data} />
                                }
                            />
                        </Routes>
                    ) : (
                        <Flex direction="column" gap="size-400">
                            <ContentLoader foregroundColor="#253267" />
                        </Flex>
                    )}
                </Card>
            </Flex>
            <ImageDiv />
        </Grid>
    );
};

const ImageDiv = styled.div`
    grid-area: image;
    height: 100%;
    width: 100%;
    background-image: url('images/account.png');
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
`;

const Card = styled.div`
    min-width: 500px;
    padding: 20px;
    border-radius: 5px;
    box-shadow: grey 2px 2px 4px;
`;
