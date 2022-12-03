import React, { useContext, useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';
import { AuctionList } from '../auction-list/AuctionList';
import { useHttpQuery } from '../http-query/use-http-query';
import { SearchContext } from '../search.context';

export const Home = () => {
    // Potential extensions:
    //   1. useContext to get the value typed in the search bar
    //   2. have a prop to provide a category (or list of categories) to filter on

    // Get our list of auctions, where appResponse is AppResponse<List<Auction>>
    const [searchURL, setSearchURL] = useState('/auctions/search');
    const searchText = useContext(SearchContext);
    const { appResponse, error } = useHttpQuery(searchURL);

    useEffect(() => {
        if (searchText) {
            setSearchURL(
                `/auctions/search?filterBy=title&filterByValue=${searchText}`,
            );
        }
    }, [searchText]);

    // We want to display a toast if we have an error.
    // However, this will change the "state" of React. Therefore,
    // we'll want to run this in a 'useEffect' hook that runs whenever
    // appResponse, error, or status change
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

    return <AuctionList auctions={appResponse?.data || []} />;
};
