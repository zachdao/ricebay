import React from 'react';
import PublishCheck from '@spectrum-icons/workflow/PublishCheck';
import { Badge, Button, Flex, StatusLight, Text } from '@adobe/react-spectrum';
import Cancel from '@spectrum-icons/workflow/Cancel';
import Copy from '@spectrum-icons/workflow/Copy';
import User from '@spectrum-icons/workflow/User';

const getLastBid = (bids) => {
    if (!bids) {
        return undefined;
    }

    return bids[bids.length - 1];
};

export const EditState = ({
    auction,
    published,
    setPublished,
    saveAuction,
    copyAuction,
}) => {
    return (
        <Flex
            gridArea="status"
            direction="row"
            alignItems="center"
            justifyContent="start"
        >
            {auction && published === false && auction.bids?.length === 0 && (
                <RelistAuction onPress={() => setPublished(true)} />
            )}
            {auction &&
                auction.published === false &&
                auction.bids?.length > 0 && (
                    <CopyAuction onPress={() => copyAuction()} />
                )}
            {auction && auction.published === true && (
                <CancelAuction
                    isDisabled={auction?.bids?.length > 0}
                    onPress={() => {
                        saveAuction({ ...auction, published: false });
                    }}
                />
            )}
            {auction && (
                <AuctionState
                    isPublished={auction?.published}
                    lastBid={getLastBid(auction?.bids)}
                    isRelisted={
                        auction?.published === false && published === true
                    }
                />
            )}
        </Flex>
    );
};

const RelistAuction = ({ onPress }) => {
    return (
        <Button variant="primary" maxWidth="max-content" onPress={onPress}>
            <PublishCheck />
            <Text>Re-list</Text>
        </Button>
    );
};

const CancelAuction = ({ onPress, isDisabled }) => {
    return (
        <Button
            variant="primary"
            maxWidth="max-content"
            onPress={onPress}
            isDisabled={isDisabled}
        >
            <Cancel />
            <Text>Cancel Auction</Text>
        </Button>
    );
};

const CopyAuction = ({ onPress }) => {
    return (
        <Button variant="primary" maxWidth="max-content" onPress={onPress}>
            <Copy />
            <Text>Copy Auction</Text>
        </Button>
    );
};

const AuctionState = ({ isPublished, lastBid, isRelisted }) => {
    let variant = 'positive';
    let statusText = `Published ${!lastBid ? '(No Bids)' : ''}`;
    if (isRelisted) {
        variant = 'neutral';
        statusText = 'Unpublished (Save to Publish)';
    } else if (!isPublished && !lastBid) {
        variant = 'negative';
        statusText = 'Expired';
    } else if (!isPublished && lastBid) {
        variant = 'info';
        statusText = 'Sold';
    }
    return (
        <Flex direction="row" alignItems="center">
            <StatusLight variant={variant}>{statusText}</StatusLight>
            {!isPublished && lastBid && (
                <Badge variant="positive" marginStart="size-100">
                    <User />
                    <Text>Buyer: {lastBid.alias}</Text>
                </Badge>
            )}
        </Flex>
    );
};
