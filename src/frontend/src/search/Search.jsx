import { Flex, Heading, ListBox, Item, ComboBox } from '@adobe/react-spectrum';
import React, { useContext, useEffect, useState } from 'react';
import { AuctionList } from '../auction-list/AuctionList';
import { CategoriesContext } from '../categories.context';
import { SearchContext } from '../search.context';
import { useHttpQuery } from '../http-query/use-http-query';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const Search = () => {
    const [sort, setSort] = useState();
    const [categories, setCategories] = useState(new Set([]));
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
        if (searchText && categories.size > 0) {
            setSearchURL(
                `/auctions/search?title:like=${searchText}&description:like=${searchText}&hasCategories=${[
                    ...categories,
                ].join(',')}`,
            );
        } else if (searchText) {
            setSearchURL(
                `/auctions/search?title:like=${searchText}&description:like=${searchText}`,
            );
        } else if (categories.size > 0) {
            setSearchURL(
                `/auctions/search?hasCategories=${[...categories].join(',')}`,
            );
        } else {
            setSearchURL('/auctions/search');
        }
    }, [searchText, categories]);

    return (
        <Flex
            direction="row"
            alignItems="start"
            justifyContent="start"
            width="100%"
        >
            <Flex direction="column" justifyContent="center">
                <Heading level="1">Categories</Heading>
                <ListBox
                    selectionMode="multiple"
                    items={categoryOptions.map((opt) => ({ name: opt }))}
                    selectedKeys={categories}
                    onSelectionChange={(selected) =>
                        setCategories(new Set([...selected]))
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
