import React, { useEffect } from 'react';
import {
    Flex,
    Heading,
    ListView,
    Item,
    Image,
    Text,
    View,
} from '@adobe/react-spectrum';
import toast from 'react-hot-toast';
import { useHttpQuery } from '../http-query/use-http-query';
import { Toast } from '../toast/Toast';
import { useNavigate } from 'react-router-dom';
import ImageIcon from '@spectrum-icons/workflow/Image';
import Alert from '@spectrum-icons/workflow/Alert';

export const MyBids = () => {
    // Get our list of won auctions, where appResponse is AppResponse<List<Auction>>
    const { appResponse, error, status } = useHttpQuery('/auctions/myBids');
    const navigate = useNavigate();

    // Display a toast if we have an error
    useEffect(() => {
        if (error) {
            toast.custom((t) => (
                <Toast
                    message={
                        appResponse?.msg ||
                        'Failed to find bids! Try again later'
                    }
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [appResponse, error, status]);

    const hasHighestBid = (auction) =>
        auction.userBid?.bid >= auction.currentBid;

    return (
        <Flex
            margin="auto"
            marginTop="size-400"
            height="80%"
            width="40%"
            direction="column"
            gap="size-200"
            data-testid="auctions-area"
        >
            <Heading level={1} alignSelf="center">
                Your Active Bids
            </Heading>
            <ListView
                items={appResponse?.data || []}
                minHeight="size-6000"
                renderEmptyState={() => <em>You have no active Bids!</em>}
                selectionMode="none"
                onAction={(id) => navigate(`/auction/${id}`)}
            >
                {(auction) => (
                    <Item key={auction.id} hasChildItems>
                        <View gridArea="thumbnail" marginEnd="size-100">
                            {auction.images.length > 0 ? (
                                <Image src={auction.images[0]} width="48px" />
                            ) : (
                                <ImageIcon size="XL" />
                            )}
                        </View>
                        <Text>{auction.title}</Text>
                        <Text slot="description">
                            {auction.description.length > 500
                                ? auction.description.substring(0, 500) + '...'
                                : auction.description}
                        </Text>
                        <Flex
                            alignItems="center"
                            gap="size-100"
                            gridArea="actions"
                        >
                            <Alert
                                color={
                                    hasHighestBid(auction)
                                        ? 'positive'
                                        : 'negative'
                                }
                            />
                            <Text>
                                {hasHighestBid(auction)
                                    ? 'You have the highest bid'
                                    : 'You have been outbid'}
                            </Text>
                        </Flex>
                    </Item>
                )}
            </ListView>
        </Flex>
    );
};
