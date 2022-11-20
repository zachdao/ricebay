import React, { useCallback } from 'react';
import { Flex, Grid, Heading, ProgressBar, Text } from '@adobe/react-spectrum';
import { usePostWithToast } from '../../http-query/use-post-with-toast';
import { Bid } from './bid/Bid';
import { StarRating } from '../../star-rating/StarRating';
import { CategoryTagGroup } from '../../category-tag-group/CategoryTagGroup';
import styled from 'styled-components';
import { DateTime, Interval } from 'luxon';
import { ImagePlaceholder } from '../../image-placeholder/ImagePlaceholder';

export const AuctionView = ({ auction, refresh }) => {
    const startDate = DateTime.fromISO(auction.startDate);
    const endDate = DateTime.fromISO(auction.endDate);

    // Handle bad calls to make the bid, Handle bad calls to increase max bid
    const makeBid = usePostWithToast(
        `/auction/${auction.id}/bid`,
        null,
        [auction],
        { message: 'Bid placed!' },
        { message: 'Failed to place bid!' },
        () => refresh(),
    );

    const getTimeRemaining = useCallback(() => {
        const total = Interval.fromDateTimes(startDate, endDate).length();
        const amount = Interval.fromDateTimes(
            startDate,
            DateTime.now(),
        ).length();
        return (amount / total) * 100;
    }, [auction]);

    // Render data if found
    return (
        <Grid
            maxWidth="960px"
            areas={['image details', 'description description']}
            columns={['3fr', '2fr']}
            rows={['40%', 'auto']}
            height="calc(100% - 40px)"
            gap="size-250"
        >
            <Flex
                gridArea="image"
                alignItems="center"
                justifyContent="center"
                width="100%"
                height="100%"
            >
                {auction.images.length ? (
                    <img src={auction.images[0]} alt="image of auction item" />
                ) : (
                    <ImagePlaceholder size="XXL" />
                )}
            </Flex>

            <Flex direction="column" gridArea="details">
                <Heading alignSelf="center" level={1}>
                    {auction.title}
                </Heading>
                <Grid
                    columns={['2fr', '2fr']}
                    autoRows="size-400"
                    height="100%"
                    justifyContent="space-evenly"
                    alignItems="center"
                    rowGap="size-100"
                >
                    <FancyLabel>Current Bid</FancyLabel>
                    <Text>{auction.currentBid || auction.minimumBid}</Text>
                    <FancyLabel>Status</FancyLabel>
                    <ProgressBar
                        label="Time Remaining"
                        valueLabel={`Ends ${endDate.toRelative()}`}
                        value={getTimeRemaining()}
                    />
                    <FancyLabel>Seller</FancyLabel>
                    <Text>{auction.seller.alias}</Text>
                    <FancyLabel>Seller Rating</FancyLabel>
                    <StarRating
                        rating={auction.seller.rating || 0}
                        alignItems="start"
                        justifyContent="start"
                    />
                    <FancyLabel>Categories</FancyLabel>
                    <Text>
                        <CategoryTagGroup categories={auction.categories} />
                    </Text>
                    <Bid
                        auction={auction}
                        makeBid={makeBid}
                        width="100%"
                        gridColumnStart="1"
                        gridColumnEnd="3"
                        marginTop="5px"
                    />
                </Grid>
            </Flex>

            <Flex
                direction="column"
                space="size-50"
                width="100%"
                alignItems="start"
                gridArea="description"
                borderWidth="thin"
                borderColor="dark"
            >
                <Heading level={3}>Description</Heading>
                <Text>{auction.description}</Text>
            </Flex>
        </Grid>
    );
};

const FancyLabel = styled.div`
    font-size: 125%;
    font-weight: lighter;
`;
