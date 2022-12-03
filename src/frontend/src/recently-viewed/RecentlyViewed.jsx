import { Button, Flex, Heading, Text } from '@adobe/react-spectrum';
import React, { useContext, useEffect } from 'react';
import { AuctionList } from '../auction-list/AuctionList';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';
import { useHttpQuery } from '../http-query/use-http-query';

export const RecentlyViewed = () => {
    const { appResponse, error } = useHttpQuery(`/auctions/recentlyViewed`);

    useEffect(() => {
        if (error) {
            toast.custom((t) => (
                <Toast
                    message={'Failed to load auctions! Try again later'}
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [error]);

    return (
        <Flex direction="column" alignItems="center" justifyContent="center">
            <Heading level="1">Recently Viewed</Heading>
            <AuctionList auctions={appResponse?.data || []} />
        </Flex>
    );
};
