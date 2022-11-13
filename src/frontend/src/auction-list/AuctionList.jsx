import React, { useEffect } from 'react';
import { Grid, repeat, View } from '@adobe/react-spectrum';
import { AuctionListItem } from './auction-list-item/AuctionListItem';
import { useHttpQuery } from '../http-query/use-http-query';
import { List } from 'react-content-loader';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const AuctionList = () => {
    // Potential extensions:
    //   1. useContext to get the value typed in the search bar
    //   2. have a prop to provide a category (or list of categories) to filter on

    // Get our list of auctions, where appResponse is AppResponse<List<Auction>>
    const { appResponse, error, status } = useHttpQuery('/auctions/search');

    // We want to display a toast if we have an error.
    // However, this will change the "state" of React. Therefore,
    // we'll want to run this in a 'useEffect' hook that runs whenever
    // appResponse, error, or status change
    useEffect(() => {
        if (
            error ||
            (status && status !== 200) ||
            (appResponse && !appResponse.success)
        ) {
            toast.custom((t) => (
                <Toast
                    message={
                        appResponse?.msg ||
                        'Failed to load auctions! Try again later'
                    }
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [appResponse, error, status]);

    // If we have data, render it! Else render out ContentLoader.List component
    return (
        <View margin="20px auto" maxWidth="1200px">
            {appResponse?.data ? (
                <Grid
                    columns={repeat('auto-fit', '360px')}
                    autoRows="106px"
                    justifyContent="center"
                    gap="size-100"
                >
                    {appResponse.data.map((auction) => (
                        <AuctionListItem key={auction.id} auction={auction} />
                    ))}
                </Grid>
            ) : (
                <List />
            )}
        </View>
    );
};
