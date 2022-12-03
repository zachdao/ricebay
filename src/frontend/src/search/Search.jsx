import {
    Flex,
    Heading,
    Text,
    ListBox,
    Item,
    ComboBox,
} from '@adobe/react-spectrum';
import React, { useContext, useEffect, useState } from 'react';
import { AuctionList } from '../auction-list/AuctionList';
import { CategoriesContext } from '../categories.context';
import { SearchContext } from '../search.context';
import { useHttpQuery } from '../http-query/use-http-query';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const Search = () => {
    const [sort, setSort] = React.useState();
    const [category, setCategory] = React.useState(new Set([]));
    const [searchURL, setSearchURL] = useState('/auctions/search');
    const searchText = useContext(SearchContext);
    const { appResponse, error } = useHttpQuery(searchURL);

    const categoryOptions = useContext(CategoriesContext);

    const sortOptions = [
        { name: 'Price Lowest to Highest' },
        { name: 'Price Highest to Lowest' },
        { name: 'Least Time Remaining' },
        { name: 'Most Time Remaining' },
    ];

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

    useEffect(() => {
        if (searchText && category) {
            setSearchURL(
                `/auctions/search?filterBy=title&filterByValue=${searchText}&hasCategory=${[
                    ...category,
                ].join(',')}`,
            );
        } else if (searchText) {
            setSearchURL(
                `/auctions/search?filterBy=title&filterByValue=${searchText}`,
            );
        } else if (category) {
            setSearchURL(
                `/auctions/search?hasCategory=${[...category].join(',')}`,
            );
        }
    }, [searchText, category]);

    return (
        <Flex direction="row" alignItems="start" justifyContent="start">
            <Flex direction="column">
                <Heading level="1">Categories</Heading>
                <ListBox
                    selectionMode="multiple"
                    items={categoryOptions.map((opt) => ({ name: opt }))}
                    selectedKeys={category}
                    onSelectionChange={(selected) =>
                        setCategory(Array.from(selected))
                    }
                    width="size-2400"
                >
                    {(item) => <Item key={item.name}>{item.name}</Item>}
                </ListBox>
                <Heading level="1">Sort</Heading>
                <ComboBox
                    items={sortOptions}
                    selectedKeys={sort}
                    onSelectionChange={(selected) =>
                        setOptions(Array.from(selected))
                    }
                    width="size-2400"
                >
                    {(item) => <Item key={item.name}>{item.name}</Item>}
                </ComboBox>
            </Flex>

            <AuctionList auctions={appResponse?.data || []} />
        </Flex>
    );
};
