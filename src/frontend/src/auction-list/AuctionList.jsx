import React from 'react';
import { Grid, repeat, View } from '@adobe/react-spectrum';
import { AuctionListItem } from './auction-list-item/AuctionListItem';
import { List } from 'react-content-loader';

export const AuctionList = ({ auctions }) => {
    // If we have data, render it! Else render out ContentLoader.List component
    return (
        <View margin="20px auto" maxWidth="1200px" width="100%">
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
