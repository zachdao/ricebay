import React, { useCallback, useEffect, useState } from 'react';
import {
    Flex,
    Grid,
    Heading,
    Text,
    View,
    Button,
    Dialog,
    DialogTrigger,
    Content,
    TextField,
} from '@adobe/react-spectrum';
import { useNavigate, useParams } from 'react-router-dom';
import { useHttpQuery } from '../http-query/use-http-query';
import axios from 'axios';

const auction = {
    title: 'Obnoxiously Long Title Blah Blah Blah',
    minimumBid: 20.0,
    published: 'Published',
    ownerId: 12345,
    categoryIds: [1, 2, 3],
    description:
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ' +
        'labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut ' +
        'aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ' +
        'eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt ' +
        'mollit anim id est laborum.',
    seller: 'coolguy25',
    images: [],
    maxBid: 500.0,
    bidIncrement: 2.0,
};

export const AuctionView = ({ auction }) => {
    // Get our single auction listing, where appResponse is AppResponse<Auction>
    // const { auctionId } = useParams();
    // const { appResponse, error, status } = useHttpQuery(
    //     `/auctions/${auctionId}`,
    // );
    // const auction = appResponse?.data;

    // Display a toast if we have an error
    // useEffect(() => {
    //     if (
    //         error ||
    //         (status && status !== 200) ||
    //         (appResponse && !appResponse.success)
    //     ) {
    //         toast.custom((t) => (
    //             <Toast
    //                 message={
    //                     appResponse?.msg ||
    //                     'Failed to find auction! Try again later'
    //                 }
    //                 type="negative"
    //                 dismissFn={() => toast.remove(t.id)}
    //             />
    //         ));
    //     }
    // }, [appResponse, error, status]);

    // Set up text field data
    const [yourBid, setBid] = useState('');
    const [maxBid, setMaxBid] = useState('');
    const [error, setError] = useState('');

    // const { appResponse, error1, status } = useHttpQuery('/accounts/me')
    // const userID = appResponse.data;

    // TODO Once the backend sends all the necessary data, map the fields to the (currently) missing variables

    // Handle bad calls to make the bid, Handle bad calls to increase max bid
    const makeBid = useCallback(async () => {
        try {
            setError('');
            await axios.put('/auction/:id', {
                // userID,
                yourBid,
                maxBid,
            });
        } catch (e) {
            setError('Error occurred while placing bid!');
        }
    }, [
        // userID
        yourBid,
        maxBid,
    ]);

    const isValid = useCallback(() => {
        return (
            yourBid >= auction.minimumBid + auction.bidIncrement &&
            yourBid <= maxBid
        );
    }, [yourBid, maxBid]);

    // TODO Disable button if user currently has highest bid
    // TODO re-enable button and give notification if they have been outbid

    // Render data if found
    return (
        <Grid
            margin="0 auto"
            marginTop="size-400"
            width="50%"
            areas={['image details', 'description description']}
            columns={['3fr', '2fr']}
            rows={['50%', '50%']}
            height="90%"
            gap="size-250"
        >
            <View gridArea="image">
                {auction.images.length ? (
                    <img src={auction.images[0]} alt="image of auction item" />
                ) : (
                    <View
                        width="100%"
                        height="100%"
                        backgroundColor="gray-800"
                    ></View>
                )}
            </View>

            <Flex direction="column" gridArea="details">
                <Heading alignSelf="center" level={1}>
                    {auction.title}
                </Heading>
                <Grid
                    areas={[
                        'currentbid bidvar',
                        'bidincrement incrementvar',
                        'status statusvar',
                        'seller sellervar',
                        'sellerrating ratingvar',
                        'categories catvar',
                    ]}
                    columns={['3fr', '2fr']}
                    rows={['1fr', '1fr', '1fr', '1fr', '1fr', '1fr']}
                    height="100%"
                    justifyContent="space-evenly"
                >
                    <Text gridArea="currentbid">Current Bid:</Text>
                    <Text gridArea="bidvar">{auction.minimumBid}</Text>
                    <Text gridArea="bidincrement">Minimum Bid Increment:</Text>
                    <Text gridArea="incrementvar">{auction.bidIncrement}</Text>
                    <Text gridArea="status">Status:</Text>
                    <Text gridArea="statusvar">{auction.published}</Text>
                    <Text gridArea="seller">Seller:</Text>
                    <Text gridArea="sellervar">{auction.seller}</Text>
                    <Text gridArea="sellerrating">Seller Rating:</Text>
                    <Text gridArea="ratingvar">
                        {auction.sellerRating} star(s)
                    </Text>
                    <Text gridArea="categories">Categories:</Text>
                    <Text gridArea="catvar">{auction.categoryIds}</Text>
                </Grid>
                <DialogTrigger type="popover">
                    <Button alignSelf="center" variant="cta">
                        Increase Bid (Max: ${auction.maxBid})
                    </Button>
                    <Dialog>
                        <Heading>Place your bid</Heading>
                        <Content>
                            <Flex
                                direction="rows"
                                justifyContent="space-evenly"
                                alignContent="center"
                                marginTop="size-100"
                                gap="size-100"
                            >
                                <TextField
                                    validationState={
                                        error ? 'invalid' : undefined
                                    }
                                    label="Your bid"
                                    value={yourBid}
                                    onChange={setBid}
                                    type="double"
                                    onBlur={() => setError('')}
                                />
                                <TextField
                                    validationState={
                                        error ? 'invalid' : undefined
                                    }
                                    label="Max bid"
                                    value={maxBid}
                                    onChange={setMaxBid}
                                    type="double"
                                    onBlur={() => setError('')}
                                />
                                <Button
                                    alignSelf="center"
                                    variant="cta"
                                    isDisabled={!isValid()}
                                    onPress={makeBid}
                                >
                                    Make bid
                                </Button>
                            </Flex>
                        </Content>
                    </Dialog>
                </DialogTrigger>
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
