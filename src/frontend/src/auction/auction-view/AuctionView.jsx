import React, { useCallback, useContext } from 'react';
import {
    Content,
    ContextualHelp,
    Flex,
    Grid,
    Heading,
    ProgressBar,
    Text,
} from '@adobe/react-spectrum';
import { usePostWithToast } from '../../http-query/use-post-with-toast';
import { Bid } from './view-state/Bid';
import { StarRating } from '../../star-rating/StarRating';
import { CategoryTagGroup } from '../../category-tag-group/CategoryTagGroup';
import styled from 'styled-components';
import { DateTime, Interval } from 'luxon';
import { ImagePlaceholder } from '../../image-placeholder/ImagePlaceholder';
import { ImageCarousel } from '../../image-carousel/ImageCarousel';
import { UserContext } from '../../user.context';
import { Owner } from './view-state/Owner';
import { UserBid } from './view-state/UserBid';
import { WonBid } from './view-state/WonBid';

export const AuctionView = ({ auction, refresh }) => {
    const startDate = DateTime.fromFormat(auction.startDate, 'DD');
    const endDate = DateTime.fromFormat(auction.endDate, 'DD');
    const { user } = useContext(UserContext);

    // Handle bad calls to make the bid, Handle bad calls to increase max bid

    const getTimeRemaining = useCallback(() => {
        const total = Interval.fromDateTimes(startDate, endDate).length();
        const amount = Interval.fromDateTimes(
            startDate,
            DateTime.now(),
        ).length();
        return (amount / total) * 100;
    }, [auction]);

    const getCatSpan = (count) => {
        if (count < 3) {
            return 1;
        } else if (count < 6) {
            return 2;
        } else if (count < 9) {
            return 3;
        } else if (count < 12) {
            return 4;
        }
        // Jump two here for extra padding on long lists
        return 6;
    };

    let bidState = null;
    if (auction?.seller?.alias === user?.alias) {
        bidState = (
            <Owner
                auctionId={auction.id}
                width="100%"
                gridColumnStart="1"
                gridColumnEnd="3"
                marginTop="5px"
            />
        );
    } else if (auction.userBid && auction.published) {
        bidState = (
            <UserBid
                auction={auction}
                width="100%"
                gridColumnStart="1"
                gridColumnEnd="3"
                marginTop="5px"
                refresh={refresh}
            />
        );
    } else if (
        !auction.published &&
        auction.userBid?.bid === auction.currentBid
    ) {
        bidState = (
            <>
                <WonBid
                    auction={auction}
                    refresh={refresh}
                    width="100%"
                    gridColumnStart="1"
                    gridColumnEnd="3"
                    marginTop="5px"
                />
                <div
                    style={{
                        color: 'green',
                        width: '100%',
                        gridColumnStart: '1',
                        gridColumnEnd: '3',
                        marginTop: '5px',
                        textAlign: 'center',
                    }}
                >
                    You won!
                </div>
            </>
        );
    } else if (auction.published) {
        bidState = (
            <Bid
                auction={auction}
                width="100%"
                gridColumnStart="1"
                gridColumnEnd="3"
                marginTop="5px"
                refresh={refresh}
            />
        );
    } else {
        bidState = (
            <div
                style={{
                    color: 'red',
                    width: '100%',
                    gridColumnStart: '1',
                    gridColumnEnd: '3',
                    marginTop: '5px',
                    textAlign: 'center',
                }}
            >
                Bid is closed and you did not win
            </div>
        );
    }

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
                    <ImageCarousel images={auction.images} />
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
                    <FancyLabel>
                        <Text>Current Bid</Text>
                        {auction?.taxPercent > 0 ? (
                            <ContextualHelp variant="info">
                                <Heading>Sales Tax</Heading>
                                <Content>
                                    <Text>
                                        The seller has set a Sales Tax of{' '}
                                        {auction.taxPercent * 100}%
                                    </Text>
                                </Content>
                            </ContextualHelp>
                        ) : null}
                    </FancyLabel>
                    <Text>
                        ${(auction.currentBid || auction.minimumBid).toFixed(2)}
                    </Text>
                    <FancyLabel>Status</FancyLabel>
                    <ProgressBar
                        label="Time Remaining"
                        valueLabel={`Ends ${endDate.toRelative()}`}
                        value={getTimeRemaining()}
                    />
                    <FancyLabel>Seller</FancyLabel>
                    <Text>{auction?.seller?.alias}</Text>
                    <FancyLabel>Seller Rating</FancyLabel>
                    <StarRating
                        rating={auction?.seller?.rating || 0}
                        alignItems="start"
                        justifyContent="start"
                    />
                    <FancyLabel>Categories</FancyLabel>
                    <Text
                        gridRow={`span ${getCatSpan(
                            auction.categories.length,
                        )}`}
                    >
                        <CategoryTagGroup categories={auction.categories} />
                    </Text>
                    {bidState}
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
    display: flex;
    align-items: end;
`;
