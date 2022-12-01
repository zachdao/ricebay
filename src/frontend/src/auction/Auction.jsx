import React, { useEffect } from 'react';
import { Route, Routes, useParams } from 'react-router-dom';
import { AuctionView } from './auction-view/AuctionView';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';
import { useHttpQuery } from '../http-query/use-http-query';
import { Facebook } from 'react-content-loader';
import { Flex, View } from '@adobe/react-spectrum';
import { DateTime } from 'luxon';
import { EditAuction } from './edit-auction/EditAuction';

// TODO: Once auctions are up E2E, rip this out as it serves only to get the UI up and running
const placeholderAuction = {
    id: 1234,
    title: 'Obnoxiously Long Title Blah Blah Blah',
    minimumBid: 20.0,
    published: 'Published',
    categories: ['Furniture'],
    description:
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ' +
        'labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut ' +
        'aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ' +
        'eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt ' +
        'mollit anim id est laborum.',
    seller: {
        id: 12345,
        alias: 'coolguy25',
        rating: 3,
    },
    startDate: DateTime.now().minus({ days: 1 }).toISODate(),
    endDate: DateTime.now().plus({ days: 4 }).toISODate(),
    images: [],
    bidIncrement: 2.0,
};

export const Auction = () => {
    // Get our single auction listing, where appResponse is AppResponse<Auction>
    const { auctionId } = useParams();
    const { appResponse, error, status, refetch } = useHttpQuery(
        `/auctions/${auctionId}`,
    );
    const auction =
        appResponse?.data ||
        (auctionId == placeholderAuction.id ? placeholderAuction : null);

    // Display a toast if we have an error
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
                        'Failed to find auction! Try again later'
                    }
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [appResponse, error, status]);

    const Loading = () => (
        <View margin="size-100">
            <Facebook backgroundColor="#dddddd" />
        </View>
    );

    // Render data if found
    return (
        <Flex
            direction="row"
            alignItems="start"
            justifyContent="center"
            margin="size-400"
            height="calc(100% - 80px)"
        >
            <Routes>
                <Route
                    index
                    element={
                        auction ? (
                            <AuctionView auction={auction} refresh={refetch} />
                        ) : (
                            <Loading />
                        )
                    }
                />
                <Route
                    path="/edit"
                    element={
                        auction ? (
                            <EditAuction auction={auction} refresh={refetch} />
                        ) : (
                            <Loading />
                        )
                    }
                />
            </Routes>
        </Flex>
    );
};
