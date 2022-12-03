import React, { useEffect } from 'react';
import { Grid, repeat, View } from '@adobe/react-spectrum';
import { AuctionListItem } from './auction-list-item/AuctionListItem';
import { useHttpQuery } from '../http-query/use-http-query';
import { List } from 'react-content-loader';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const AuctionList = ({ auctions }) => {
    // If we have data, render it! Else render out ContentLoader.List component
    return (
        <View margin="20px auto" maxWidth="1200px">
            {auctions ? (
                <Grid
                    columns={repeat('auto-fit', '360px')}
                    autoRows="106px"
                    justifyContent="center"
                    gap="size-100"
                >
                    {auctions.map((auction) => (
                        <AuctionListItem key={auction.id} auction={auction} />
                    ))}
                </Grid>
            ) : (
                <List />
            )}
        </View>
    );
};
